/**
 *
 */
package com.lianyitech.modules.report.web;

import com.lianyitech.common.utils.StringUtils;
import com.lianyitech.common.web.ApiController;
import com.lianyitech.common.web.ResponseData;
import com.lianyitech.modules.common.ExcelTransformer;
import com.lianyitech.modules.common.ExportExcel;
import com.lianyitech.modules.report.entity.BookReport;
import com.lianyitech.modules.report.service.BookReportService;
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
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 跟书目有关的统计Controller
 *
 * @author 1
 * @version 2016-10-28
 */
@RestController
@RequestMapping(value = "/api/report/bookreport")
public class BookReportController extends ApiController {
    @Autowired
    BookReportService bookReportService;

    /**
     * 藏书量统计报表
     *
     * @param bookReport 实体类
     * @return 返回
     */
    //@RequiresPermissions("catalog:bookDirectory:view")
    @RequestMapping(value = {""}, method = RequestMethod.GET)
    public ResponseEntity<ResponseData> getCollectionStatis(BookReport bookReport) throws Exception{
        return new ResponseEntity<>(success(bookReportService.listCollectionStat(bookReport)), HttpStatus.OK);
    }

    /**
     * 藏书种类
     *
     * @param bookReport 实体类
     * @return 返回
     */
    @RequestMapping(value = {"bookType"}, method = RequestMethod.GET)
    public ResponseEntity<ResponseData> bookType(BookReport bookReport) {
        return new ResponseEntity<>(success(bookReportService.bookType(bookReport)), HttpStatus.OK);
    }

    /**
     * 藏书量统计报表导出
     * @param bookReport 实体类
     * @param response   re
     * @return null
     * @throws Exception
     */
    @RequestMapping(value = "/exportNewBooksReports", method = RequestMethod.GET)
    public ResponseEntity<ResponseData> exportNewBooksReports(BookReport bookReport, HttpServletResponse response)
            throws IOException, InvocationTargetException, IllegalAccessException {
        ExportExcel exportExcel = new ExportExcel();
        InputStream is = null;

        String unitName = bookReport.getUnitName();
        if(StringUtils.isNotEmpty(unitName)){
            bookReport.setUnitName(URLDecoder.decode(unitName, "utf-8"));
        }

        try {
            exportExcel.preProcessing(response, "藏书量统计");
            is = this.getClass().getClassLoader().getResourceAsStream("template" + File.separator + "藏书量统计.xls");
            Transformer transformer = TransformerFactory.createTransformer(is, response.getOutputStream());
            JexlExpressionEvaluator evaluator = (JexlExpressionEvaluator) transformer.getTransformationConfig().getExpressionEvaluator();
            Map<String, Object> functionMap = new HashMap<>();
            functionMap.put("et", new ExcelTransformer());
            evaluator.getJexlEngine().setFunctions(functionMap);
            List<BookReport> detailList = bookReportService.exportCollectionStatis(bookReport);
            Context context = new Context();
            context.putVar("detailList", detailList);
            AreaBuilder areaBuilder = new XlsCommentAreaBuilder(transformer);
            List<Area> xlsAreaList = areaBuilder.build();
            Area xlsArea = xlsAreaList.get(0);
            xlsArea.applyAt(new CellRef("结果!A1"), context);
            transformer.write();
        } finally {
            IOUtils.closeQuietly(is);
        }
        return null;
    }

    /**
     * 生均及增量统计
     *
     * @param bookReport 实体类
     * @return 返回
     */
    //@RequiresPermissions("catalog:bookDirectory:view")
    @RequestMapping(value = {"listStuAddBookNum"}, method = RequestMethod.GET)
    public ResponseEntity<ResponseData> listStuAddBookNum(BookReport bookReport) throws Exception{
        return new ResponseEntity<>(success(bookReportService.listStuAddBookNum(bookReport)), HttpStatus.OK);
    }

    /**
     * 生均及增量统计
     *
     * @param bookReport 实体类
     * @return null
     * @throws Exception
     */
    @RequestMapping(value = "/exportStuAddBookNum", method = RequestMethod.GET)
    public ResponseEntity<ResponseData> exportStuAddBookNum(BookReport bookReport, HttpServletResponse response)
            throws IOException, InvocationTargetException, IllegalAccessException {
        ExportExcel exportExcel = new ExportExcel();
        InputStream is = null;

        String unitName = bookReport.getUnitName();
        if(StringUtils.isNotEmpty(unitName)){
            bookReport.setUnitName(URLDecoder.decode(unitName, "utf-8"));
        }

        try {
            exportExcel.preProcessing(response, "生均及增量统计");
            is = this.getClass().getClassLoader().getResourceAsStream("template" + File.separator + "生均及增量统计.xls");
            Transformer transformer = TransformerFactory.createTransformer(is, response.getOutputStream());
            JexlExpressionEvaluator evaluator = (JexlExpressionEvaluator) transformer.getTransformationConfig().getExpressionEvaluator();
            Map<String, Object> functionMap = new HashMap<>();
            functionMap.put("et", new ExcelTransformer());
            evaluator.getJexlEngine().setFunctions(functionMap);
            List<BookReport> detailList = bookReportService.exportStuAddBookNum(bookReport);
            Context context = new Context();
            context.putVar("detailList", detailList);
            AreaBuilder areaBuilder = new XlsCommentAreaBuilder(transformer);
            List<Area> xlsAreaList = areaBuilder.build();
            Area xlsArea = xlsAreaList.get(0);
            xlsArea.applyAt(new CellRef("结果!A1"), context);
            transformer.write();
        } finally {
            IOUtils.closeQuietly(is);
        }
        return null;
    }

    /**
     * 首页学校统计
     *
     * @return int
     */
    @RequestMapping(value = {"countSchool"}, method = RequestMethod.GET)
    public ResponseEntity<ResponseData> countSchool(BookReport bookReport) {
        return new ResponseEntity<>(success(bookReportService.countSchool(bookReport)), HttpStatus.OK);
    }
}