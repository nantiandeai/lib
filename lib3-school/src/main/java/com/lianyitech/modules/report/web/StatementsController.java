package com.lianyitech.modules.report.web;

import com.lianyitech.common.persistence.Page;
import com.lianyitech.common.utils.StringUtils;
import com.lianyitech.common.web.ApiController;
import com.lianyitech.common.web.ResponseData;
import com.lianyitech.core.utils.CsvUtils;
import com.lianyitech.modules.catalog.service.NewbookNotifiyService;
import com.lianyitech.modules.common.ExcelTransformer;
import com.lianyitech.modules.report.entity.Statements;
import com.lianyitech.modules.report.service.StatementsService;
import com.lianyitech.modules.sys.service.FileService;
import org.jxls.area.Area;
import org.jxls.builder.AreaBuilder;
import org.jxls.builder.xls.XlsCommentAreaBuilder;
import org.jxls.common.CellRef;

import org.jxls.expression.JexlExpressionEvaluator;
import org.jxls.transform.Transformer;

import org.jxls.util.TransformerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.jxls.common.Context;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.lianyitech.core.utils.CommonUtils.setCvsConfig;


@RestController
@RequestMapping(value = "/api/report/Statements")
public class StatementsController extends ApiController {

    @Autowired
    private StatementsService statementsService;

    @Autowired
    FileService fileService;


    //文献流通统计
    @RequestMapping(value = "/find", method = RequestMethod.GET)
    public ResponseEntity<ResponseData> documentCirculate (HttpServletRequest request, HttpServletResponse response, Statements statements) {

        try {
            Page<Statements> page = statementsService.documentCirculate(new Page<>(request,response),statements);
            return new ResponseEntity<>(success(page), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(fail(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //文献借阅排行
    @RequestMapping(value = "rank", method = RequestMethod.GET)
    public ResponseEntity<ResponseData> literature(HttpServletRequest request, HttpServletResponse response, Statements statements) {

        try {
            Page<Statements> page = statementsService.literature(new Page<>(request,response),statements);
            return new ResponseEntity<>(success(page), HttpStatus.OK);
        } catch (Exception e){
            logger.error("操作失败",e);
            return new ResponseEntity<>(fail(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //读者借阅统计
    @RequestMapping(value = "borrowing", method = RequestMethod.GET)
    public ResponseEntity<ResponseData> borrowingStatistics(HttpServletRequest request, HttpServletResponse response,Statements statements) {
        try {
            Page<Statements> page = statementsService.borrowingStatistics(new Page<>(request,response),statements);
            return new ResponseEntity<>(success(page), HttpStatus.OK);
        } catch (Exception e){
            logger.error("操作失败",e);
            return new ResponseEntity<>(fail(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //文献借阅排行榜导出
    @RequestMapping(value = "/rank/export", method = RequestMethod.GET)
    public void exportLiterature(Statements statements, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String orderBy = request.getParameter("orderBy");
        if (StringUtils.isNotEmpty(orderBy)){
            Page<Statements> page = new Page<>();
            page.setOrderBy(orderBy);
            statements.setPage(page);
        }
        List<Statements> detailList = statementsService.exportLiterature(statements);

        setCvsConfig(response,"文献借阅排行榜");
        List<String> resultList = new ArrayList<>();
        resultList.add("借阅排名,题名,著者,出版社,出版日期,借阅册次");
        for(int i = 0 ; i < detailList.size() ; i++) {
            resultList.add(CsvUtils.parserLiterature(detailList.get(i),i));
        }
        fileService.writeCsvFile(resultList,fileService.generateFilePath(),response);

    }

    //文献流通统计导出
    @RequestMapping(value = "/find/export", method = RequestMethod.GET)
    public void exportDocumentCirculate(Statements statements, HttpServletResponse response) throws IOException {
        List<Statements> detailList = statementsService.exportDocumentCirculate(statements);

        setCvsConfig(response,"文献流通统计");
        List<String> resultList = new ArrayList<>();
        resultList.add("序号,条形码,题名,著者,出版社,出版时间,借阅册次,续借册次,预约册次,预借册次");
        for(int i = 0 ; i < detailList.size() ; i ++) {
            resultList.add(CsvUtils.parserDocumentCirculate(detailList.get(i),i));
        }
        fileService.writeCsvFile(resultList,fileService.generateFilePath(),response);

    }

    //读者借阅统计导出
    @RequestMapping(value = "/borrowing/export", method = RequestMethod.GET)
    public void exportBorrowingStatistics(Statements statements, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String orderBy = request.getParameter("orderBy");
        if (StringUtils.isNotEmpty(orderBy)){
            Page<Statements> page = new Page<>();
            page.setOrderBy(orderBy);
            statements.setPage(page);
        }
        List<Statements> detailList = statementsService.exportBorrowingStatistics(statements);

        setCvsConfig(response,"读者借阅统计");
        List<String> resultList = new ArrayList<>();
        resultList.add("序号,读者证号,读者姓名,读者类型,读者组织,借阅册次,续借册次,预约册次,预借册次,超期册次,罚款次数");
        for(int i = 0 ; i < detailList.size() ; i ++) {
            resultList.add(CsvUtils.parserBorrowingStatistics(detailList.get(i),i));
        }
        fileService.writeCsvFile(resultList,fileService.generateFilePath(),response);
    }

    @RequestMapping(value = "/group", method = RequestMethod.GET)
    public ResponseEntity<ResponseData> groupStatistics(HttpServletRequest request, HttpServletResponse response,Statements statements) {
        try {
            Page<Statements> page = statementsService.groupStatistics(new Page<>(request,response),statements);
            return new ResponseEntity<>(success(page), HttpStatus.OK);
        } catch (Exception e){
            logger.error("操作失败",e);
            return new ResponseEntity<>(fail(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/group/export", method = RequestMethod.GET)
    public void exportGroupStatistics(Statements statements, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String orderBy = request.getParameter("orderBy");
        if (StringUtils.isNotEmpty(orderBy)){
            Page<Statements> page = new Page<>();
            page.setOrderBy(orderBy);
            statements.setPage(page);
        }
        List<Statements> detailList = statementsService.exportGroupStatistics(statements);

        setCvsConfig(response,"组织借阅统计");
        List<String> resultList = new ArrayList<>();
        resultList.add("序号,读者组织,借阅册次,续借册次,预约册次,预借册次,超期册次,罚款次数");
        for(int i = 0 ; i < detailList.size() ; i ++) {
            resultList.add(CsvUtils.parserGroupStatistics(detailList.get(i),i));
        }
        fileService.writeCsvFile(resultList,fileService.generateFilePath(),response);
    }

}
