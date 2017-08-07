package com.lianyitech.modules.report.web;

import com.lianyitech.common.persistence.Page;
import com.lianyitech.common.web.ApiController;
import com.lianyitech.common.web.ResponseData;
import com.lianyitech.core.utils.CsvUtils;
import com.lianyitech.modules.catalog.service.NewbookNotifiyService;
import com.lianyitech.modules.common.ExcelTransformer;
import com.lianyitech.modules.report.entity.ReaderRank;
import com.lianyitech.modules.report.service.ReaderRankService;
import com.lianyitech.modules.sys.service.FileService;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.lianyitech.core.utils.CommonUtils.setCvsConfig;

/**
 * Created by zcx on 2016/11/8.
 * 读者借阅排行controller
 */
@Controller
@RequestMapping(value = "/api/report/readerRanking")
public class ReaderRankController extends ApiController {
    @Autowired
    private ReaderRankService readerRankService;
    @Autowired
    FileService fileService;

    //读者借阅排行
    @RequestMapping(value = "/reader/ranking", method = RequestMethod.GET)
    public ResponseEntity<ResponseData> findList (ReaderRank readerRank, HttpServletRequest request, HttpServletResponse response) {
        Page<ReaderRank> page = readerRankService.findPage(new Page<>(request, response),readerRank);
        return new ResponseEntity<>(success(page), HttpStatus.OK);
    }

    //读者借阅排行导出
    @RequestMapping(value = "/reader/ranking/export", method = RequestMethod.GET)
    public void exportReaderRank(ReaderRank readerRank, HttpServletResponse response) throws IOException {
        List<ReaderRank> detailList = readerRankService.readerRanking(readerRank);
        setCvsConfig(response,"读者借阅排行榜");
        List<String> resultList = new ArrayList<>();
        resultList.add("借阅排名,读者姓名,读者类型,读者组织,借阅次数");
        int ranking=1;
        for (ReaderRank readerRank1 : detailList) {
            resultList.add(CsvUtils.parseReaderRank(readerRank1,ranking));
            ranking++;
        }
        fileService.writeCsvFile(resultList,fileService.generateFilePath(),response);
    }
}
