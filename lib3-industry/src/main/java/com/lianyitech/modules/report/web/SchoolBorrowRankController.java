package com.lianyitech.modules.report.web;

import com.lianyitech.common.web.ApiController;
import com.lianyitech.common.web.ResponseData;
import com.lianyitech.modules.common.ExcelTransformer;
import com.lianyitech.modules.common.ExportExcel;
import com.lianyitech.modules.report.entity.SchoolBorrowRank;
import com.lianyitech.modules.report.service.SchoolBorrowRankService;
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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 学校借阅排行榜 SchoolBorrowRankController
 * Created by zcx on 2016/11/4
 */
@Controller
@RequestMapping(value = "/api/report/schoolborrowrank")
public class SchoolBorrowRankController extends ApiController {
    @Autowired
    private SchoolBorrowRankService schoolBorrowRankService;

    /**
     * 学校借阅排行榜查询
     * @param schoolBorrowRank 学校借阅排行榜实体类
     * @return code
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<ResponseData> listSchoolBorrowRank(SchoolBorrowRank schoolBorrowRank) {
        return new ResponseEntity<>(success(schoolBorrowRankService.listSchoolBorrowRank(schoolBorrowRank)), HttpStatus.OK);
    }

    /**
     * 学校借阅排行榜导出
     * @param schoolBorrowRank 学校借阅排行榜实体类
     * @param response         response
     * @return schoolBorrowRank
     * @throws Exception
     */
    @RequestMapping(value = "/exportSchoolBorrowRank", method = RequestMethod.GET)
    public ResponseEntity<ResponseData> exportReaderBorrowRank(SchoolBorrowRank schoolBorrowRank, HttpServletResponse response) {
        ExportExcel exportExcel = new ExportExcel();
        InputStream is = null;
        try {
            exportExcel.preProcessing(response, "学校借阅排行榜");
            is = this.getClass().getClassLoader().getResourceAsStream("template" + File.separator + "学校借阅排行榜.xls");
            Transformer transformer = TransformerFactory.createTransformer(is, response.getOutputStream());
            JexlExpressionEvaluator evaluator = (JexlExpressionEvaluator) transformer.getTransformationConfig().getExpressionEvaluator();
            Map<String, Object> functionMap = new HashMap<>();
            functionMap.put("et", new ExcelTransformer());
            evaluator.getJexlEngine().setFunctions(functionMap);
            List<SchoolBorrowRank> detailList = schoolBorrowRankService.listSchoolBorrowRank(schoolBorrowRank);
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
