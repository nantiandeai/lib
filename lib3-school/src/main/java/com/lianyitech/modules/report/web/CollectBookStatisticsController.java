package com.lianyitech.modules.report.web;

import com.lianyitech.common.persistence.Page;
import com.lianyitech.common.utils.StringUtils;
import com.lianyitech.common.web.ApiController;
import com.lianyitech.common.web.ResponseData;
import com.lianyitech.core.utils.CsvUtils;
import com.lianyitech.modules.catalog.entity.Copy;
import com.lianyitech.modules.catalog.service.NewbookNotifiyService;
import com.lianyitech.modules.common.ExcelTransformer;
import com.lianyitech.modules.report.entity.CollectBookStatistics;
import com.lianyitech.modules.report.service.CollectBookStatisticsService;
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
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.lianyitech.core.config.ConfigurerConstants.LIB3_EXPORT_PAGESIZE;
import static com.lianyitech.core.utils.CommonUtils.setCvsConfig;

/**
 * Created by zcx on 2016/11/10.
 * 藏书量分类统计 CollectBookStatisticsController
 */
@Controller
@RequestMapping(value = "/api/report/collectionsstatistics")
public class CollectBookStatisticsController extends ApiController {
    @Autowired
    private CollectBookStatisticsService collectBookStatisticsService;
    @Autowired
    FileService fileService;
    @Autowired
    private Environment environment;

    //藏书量分类统计
    @RequestMapping(value = "/collections/statistics", method = RequestMethod.GET)
    public ResponseEntity<ResponseData> findList(CollectBookStatistics collectBookStatistics, HttpServletRequest request, HttpServletResponse response) {
        Page<CollectBookStatistics> page = collectBookStatisticsService.findPage(new Page<>(request, response), collectBookStatistics);
        return new ResponseEntity<>(success(page), HttpStatus.OK);
    }

    //藏书量分类统计导出
    @RequestMapping(value = "/collections/statistics/export", method = RequestMethod.GET)
    public void collectBookExport(CollectBookStatistics collectBookStatistics, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String orderBy = request.getParameter("orderBy");
        if (StringUtils.isNotEmpty(orderBy)){
            Page<CollectBookStatistics> page = new Page<>();
            page.setOrderBy(orderBy);
            collectBookStatistics.setPage(page);
        }

        String path = fileService.generateFilePath();

        setCvsConfig(response,"藏书量分类统计");

        int pageNo = 1;
        collectBookStatistics.setPageSize(Integer.parseInt(environment.getProperty(LIB3_EXPORT_PAGESIZE)));
        List<String> resultList = new ArrayList<>();
        try(
                BufferedWriter bw = new BufferedWriter(
                        new OutputStreamWriter(new FileOutputStream(new File(path),true), "GBK"));
                InputStream in = new FileInputStream(path)
        ) {
            resultList.add("馆藏地,条形码,索取号,ISBN,题名,著者");
            CsvUtils.exportCommonCsv(bw,resultList);
            while (true) {
                collectBookStatistics.setPageNo(pageNo);
                List<CollectBookStatistics> pageList = collectBookStatisticsService.exportCollectBookByPage(collectBookStatistics);
                List<String> tmpList = new ArrayList<>();
                for (CollectBookStatistics collectBookStatistics1 : pageList) {
                    tmpList.add(CsvUtils.parsercollectBook(collectBookStatistics1));
                }
                CsvUtils.exportCommonCsv(bw,tmpList);
                if(pageList.size()<Integer.parseInt(environment.getProperty(LIB3_EXPORT_PAGESIZE))) {
                    break;
                }
                pageList = null ;
                tmpList = null ;
                pageNo++;
            }
            CsvUtils.writeResponse(in,response);
        }
        response.flushBuffer();

    }
}
