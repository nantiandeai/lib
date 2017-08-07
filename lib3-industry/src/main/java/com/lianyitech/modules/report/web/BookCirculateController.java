/**
 *
 */
package com.lianyitech.modules.report.web;

import com.lianyitech.common.utils.StringUtils;
import com.lianyitech.common.web.ApiController;
import com.lianyitech.common.web.ResponseData;
import com.lianyitech.modules.common.ExcelTransformer;
import com.lianyitech.modules.common.ExportExcel;
import com.lianyitech.modules.report.entity.BookCirculte;
import com.lianyitech.modules.report.service.BookCirculateService;
import com.lianyitech.modules.sys.entity.User;
import com.lianyitech.modules.sys.utils.UserUtils;
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
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 藏书流通有关统计报表（生均借阅统计和读者借阅率统计、图书流通率统计）Controller
 *
 * @author null
 * @version 2016-10-28
 */
@RestController
@RequestMapping(value = "/api/report/bookCirculate")
public class BookCirculateController extends ApiController {
    @Autowired
    private BookCirculateService bookCirculateService;

    /**
     * 生均借阅统计
     *
     * @param bookCirculte 实体
     * @return list
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<ResponseData> listStuStatistics(BookCirculte bookCirculte) throws Exception{
        return new ResponseEntity<>(success(bookCirculateService.listStuStatistics(bookCirculte)), HttpStatus.OK);
    }

    /**
     * 生均借阅统计导出
     *
     * @param bookCirculte 实体
     * @param response     s
     * @return null
     * @throws Exception
     */
    @RequestMapping(value = "/exportNewBooksReports", method = RequestMethod.GET)
    public ResponseEntity<ResponseData> exportNewBooksReports(BookCirculte bookCirculte, HttpServletResponse response)
            throws Exception {
        ExportExcel exportExcel = new ExportExcel();
        InputStream is = null;

        String unitName = bookCirculte.getUnitName();
        if(StringUtils.isNotEmpty(unitName)){
            bookCirculte.setUnitName(URLDecoder.decode(unitName, "utf-8"));
        }

        try {
            exportExcel.preProcessing(response, "生均借阅统计");
            is = this.getClass().getClassLoader().getResourceAsStream("template" + File.separator + "生均借阅统计.xls");
            Transformer transformer = TransformerFactory.createTransformer(is, response.getOutputStream());
            JexlExpressionEvaluator evaluator = (JexlExpressionEvaluator) transformer.getTransformationConfig().getExpressionEvaluator();
            Map<String, Object> functionMap = new HashMap<>();
            functionMap.put("et", new ExcelTransformer());
            evaluator.getJexlEngine().setFunctions(functionMap);
            List<BookCirculte> detailList = bookCirculateService.exportNewBooksReports(bookCirculte);
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
     * 读者借阅率统计
     *
     * @param bookCirculte 实体
     * @return list
     */
    @RequestMapping(value = "readStatistics", method = RequestMethod.GET)
    public ResponseEntity<ResponseData> readStatistics(BookCirculte bookCirculte) throws Exception{
        return new ResponseEntity<>(success(bookCirculateService.readStatistics(bookCirculte)), HttpStatus.OK);
    }

    /**
     * 读者借阅率统计导出
     *
     * @param bookCirculte 实体
     * @return list
     */
    @RequestMapping(value = "/exportReadStaReports", method = RequestMethod.GET)
    public ResponseEntity<ResponseData> exportReadStaReports(BookCirculte bookCirculte, HttpServletResponse response)
            throws Exception {
        ExportExcel exportExcel = new ExportExcel();
        InputStream is = null;

        String unitName = bookCirculte.getUnitName();
        if(StringUtils.isNotEmpty(unitName)){
            bookCirculte.setUnitName(URLDecoder.decode(unitName, "utf-8"));
        }

        try {
            exportExcel.preProcessing(response, "读者借阅率统计");
            is = this.getClass().getClassLoader().getResourceAsStream("template" + File.separator + "读者借阅率统计.xls");
            Transformer transformer = TransformerFactory.createTransformer(is, response.getOutputStream());
            JexlExpressionEvaluator evaluator = (JexlExpressionEvaluator) transformer.getTransformationConfig().getExpressionEvaluator();
            Map<String, Object> functionMap = new HashMap<>();
            functionMap.put("et", new ExcelTransformer());
            evaluator.getJexlEngine().setFunctions(functionMap);
            List<BookCirculte> detailList = bookCirculateService.exportReadStaReports(bookCirculte);
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
     * 图书流通率统计
     *
     * @param bookCirculte 实体
     * @return list
     */
    @RequestMapping(value = "bookCirStatistic", method = RequestMethod.GET)
    public ResponseEntity<ResponseData> bookCirStatistic(BookCirculte bookCirculte) throws Exception{
        return new ResponseEntity<>(success(bookCirculateService.bookCirStatistic(bookCirculte)), HttpStatus.OK);
    }

    /**
     * 图书流通率统计导出
     *
     * @param bookCirculte 实体
     * @return list
     */
    @RequestMapping(value = "/exportBookCirReports", method = RequestMethod.GET)
    public ResponseEntity<ResponseData> exportBookCirReports(BookCirculte bookCirculte, HttpServletResponse response)
            throws Exception {
        ExportExcel exportExcel = new ExportExcel();
        InputStream is = null;

        String unitName = bookCirculte.getUnitName();
        if(StringUtils.isNotEmpty(unitName)){
            bookCirculte.setUnitName(URLDecoder.decode(unitName, "utf-8"));
        }

        try {
            exportExcel.preProcessing(response, "图书流通率统计");
            is = this.getClass().getClassLoader().getResourceAsStream("template" + File.separator + "图书流通率统计.xls");
            Transformer transformer = TransformerFactory.createTransformer(is, response.getOutputStream());
            JexlExpressionEvaluator evaluator = (JexlExpressionEvaluator) transformer.getTransformationConfig().getExpressionEvaluator();
            Map<String, Object> functionMap = new HashMap<>();
            functionMap.put("et", new ExcelTransformer());
            evaluator.getJexlEngine().setFunctions(functionMap);
            List<BookCirculte> detailList = bookCirculateService.exportBookCirReports(bookCirculte);
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
     * 首页流通率统计
     *
     * @param bookCirculte 实体
     * @return list
     */
    @RequestMapping(value = "/circulationRate", method = RequestMethod.GET)
    public ResponseEntity<ResponseData> circulationRate(BookCirculte bookCirculte) {
        try {
            User user=UserUtils.getUser();
            if (user!=null){
                bookCirculte.setYear(user.getCreateDate().substring(0,4));
                return new ResponseEntity<>(success(bookCirculateService.circulationRate(bookCirculte)), HttpStatus.OK);
            }else {
                return new ResponseEntity<>(fail("用户信息错误，请重新刷新"), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }catch (Exception e){
            logger.error("操作失败",e);
            return new ResponseEntity<>(fail("请求失败，请重新刷新"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}