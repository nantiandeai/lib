package com.lianyitech.modules.report.web;

import com.lianyitech.common.web.ApiController;
import com.lianyitech.common.web.ResponseData;
import com.lianyitech.modules.common.ExcelTransformer;
import com.lianyitech.modules.common.ExportExcel;
import com.lianyitech.modules.report.entity.ReaderBorrowRank;
import com.lianyitech.modules.report.service.ReaderBorrowRankService;
import org.apache.commons.io.IOUtils;
import org.jxls.area.Area;
import org.jxls.builder.AreaBuilder;
import org.jxls.builder.xls.XlsCommentAreaBuilder;
import org.jxls.common.CellRef;
import org.jxls.common.Context;
import org.jxls.expression.JexlExpressionEvaluator;
import org.jxls.transform.Transformer;
import org.jxls.util.TransformerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 读者借阅排行榜 ReaderBorrowRankController
 * Created by zcx on 2016/11/2.
 */

@RestController
@RequestMapping(value = "/api/report/readerborrowrank")
public class ReaderBorrowRankController extends ApiController {
    @Autowired
    private ReaderBorrowRankService readerBorrowRankService;

    /**
     * 查询所有的读者借阅排行榜
     * @param readerBorrowRank 读者借阅排行榜实体
     * @return list
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<ResponseData> listReaderBorrowRank(ReaderBorrowRank readerBorrowRank) {
        return new ResponseEntity<>(success(readerBorrowRankService.listReaderBorrowRank(readerBorrowRank)), HttpStatus.OK);
    }

    /**
     * 读者借阅排行榜导出
     * @param readerBorrowRank 读者借阅排行榜实体
     * @param response         response
     * @return readerBorrowRank
     * @throws Exception
     */
    @RequestMapping(value = "/exportReaderBorrowRank", method = RequestMethod.GET)
    public ResponseEntity<ResponseData> exportReaderBorrowRank(ReaderBorrowRank readerBorrowRank, HttpServletResponse response) {
        ExportExcel exportExcel = new ExportExcel();
        InputStream is = null;
        try {
            exportExcel.preProcessing(response, "读者借阅排行榜");
            is = this.getClass().getClassLoader().getResourceAsStream("template" + File.separator + "读者借阅排行榜.xls");
            Transformer transformer = TransformerFactory.createTransformer(is, response.getOutputStream());
            JexlExpressionEvaluator evaluator = (JexlExpressionEvaluator) transformer.getTransformationConfig().getExpressionEvaluator();
            Map<String, Object> functionMap = new HashMap<>();
            functionMap.put("et", new ExcelTransformer());
            evaluator.getJexlEngine().setFunctions(functionMap);
            List<ReaderBorrowRank> detailList = readerBorrowRankService.listReaderBorrowRank(readerBorrowRank);
            Context context = new Context();
            context.putVar("detailList", detailList);
            AreaBuilder areaBuilder = new XlsCommentAreaBuilder(transformer);
            List<Area> xlsAreaList = areaBuilder.build();
            Area xlsArea = xlsAreaList.get(0);
            xlsArea.applyAt(new CellRef("结果!A1"), context);
            transformer.write();
        } catch (IOException e) {
            logger.error("操作失败",e);
        } finally {
            IOUtils.closeQuietly(is);
        }
        return null;
    }

}
