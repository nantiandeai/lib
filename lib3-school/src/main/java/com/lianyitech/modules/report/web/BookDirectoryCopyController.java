package com.lianyitech.modules.report.web;

import com.lianyitech.common.persistence.Page;
import com.lianyitech.common.utils.StringUtils;
import com.lianyitech.common.web.ApiController;
import com.lianyitech.core.utils.CsvUtils;
import com.lianyitech.modules.catalog.entity.BookDirectory;
import com.lianyitech.modules.catalog.service.BookDirectoryService;
import com.lianyitech.modules.catalog.service.NewbookNotifiyService;
import com.lianyitech.modules.catalog.utils.ExportCsv;
import com.lianyitech.modules.common.ExcelTransformer;
import com.lianyitech.modules.report.entity.BookDirectoryCopy;
import com.lianyitech.modules.report.service.BookDirectoryCopyService;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.lianyitech.core.config.ConfigurerConstants.LIB3_EXPORT_PAGESIZE;
import static com.lianyitech.core.utils.CommonUtils.setCvsConfig;
import static com.sun.tools.doclint.Entity.copy;

/*
 * Created by chenxiaoding on 2016/11/4.
 */
@RestController
@RequestMapping(value = "/api/report/bookDirectoryCopy")
public class BookDirectoryCopyController extends ApiController{
    @Autowired
    private BookDirectoryCopyService bdcService;
    @Autowired
    private Environment environment;
    @Autowired
    private BookDirectoryService bookDirectoryService;

    /**
     * 复本统计查询
     * @param request request
     * @param response response
     * @param bdCopy bdCopy
     * @return page
     */
    @RequestMapping(value = "",method = RequestMethod.GET)
    public ResponseEntity findAllBookDirectoryCopy(HttpServletRequest request, HttpServletResponse response,BookDirectoryCopy bdCopy){
        try {
            Page<BookDirectoryCopy> page = bdcService.findPage(new Page<>(request,response),bdCopy);
            return new ResponseEntity<>(success(page), HttpStatus.OK);
        } catch (Exception e){
            logger.error("操作失败",e);
            return new ResponseEntity<>(fail(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 复本统计导出
     * @param bdCopy bdCopy
     * @param response response
     * @throws IOException 异常
     */
    @RequestMapping(value = "export", method = RequestMethod.GET)
    public void findAllDirectoryCopy(BookDirectoryCopy bdCopy, HttpServletRequest request, HttpServletResponse response) throws IOException {
            String orderBy = request.getParameter("orderBy");
            if (StringUtils.isNotEmpty(orderBy)){
                Page<BookDirectoryCopy> page = new Page<>();
                page.setOrderBy(orderBy);
                bdCopy.setPage(page);
            }
            String uploadFileName = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_TIME).replaceAll(":", "") + "_" + UUID.randomUUID().toString().replaceAll("-", "")+".csv";
            String uploadPath = String.join(File.separator, new String[]{environment.getProperty("upload.linux.path"), LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE).replaceAll("-", ""), "exportTempDir"});
            File file = new File(uploadPath);
            if (!file.exists()) {
                file.mkdirs();
            }
            String path = uploadPath + File.separator + uploadFileName;
            setCvsConfig(response,"复本统计报表");
            int pageNo = 1;
            bdCopy.setPageSize(Integer.parseInt(environment.getProperty(LIB3_EXPORT_PAGESIZE)));
            List<String> resultList = new ArrayList<>();
            try(
                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter (new FileOutputStream(new File(path),true), "GBK"));
                    InputStream in = new FileInputStream(path);
            ) {
            resultList.add("ISBN,索取号,分类号,题名,著者,出版社,出版日期,馆藏册数");
            CsvUtils.exportBookReportCsv(bw,resultList);
            while (true) {
                bdCopy.setPageNo(pageNo);
                List<BookDirectoryCopy> pageList = bdcService.findAllDirectoryCopy(bdCopy);
                List<String> tmpList = new ArrayList<>();
                for (BookDirectoryCopy bookDirectoryCopy : pageList) {
                    tmpList.add(ExportCsv.parseCopyTotalLine(bookDirectoryCopy));
                }
                CsvUtils.exportBookReportCsv(bw,tmpList);
                if(pageList.size()<Integer.parseInt(environment.getProperty(LIB3_EXPORT_PAGESIZE))){
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

    /**
     * 获取书目信息详情
     * @param id 书目id
     * @return bookDirectory
     */
    @GetMapping("/get/bookDirectory")
    public ResponseEntity getBookDirectory(String id) {
        try {
            BookDirectory bookDirectory = bookDirectoryService.get(id);
            return new ResponseEntity<>(success(bookDirectory), HttpStatus.OK);
        } catch (Exception e){
            logger.error("操作失败",e);
            return new ResponseEntity<>(fail(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 获取书目流通统计
     * @param bdCopy  bdCopy
     * @return bookDirectoryCopy
     */
    @GetMapping("/count/circulate")
    public ResponseEntity countCirculate(BookDirectoryCopy bdCopy) {
        try {
            BookDirectoryCopy bookDirectoryCopy = bdcService.countCirculate(bdCopy);
            return new ResponseEntity<>(success(bookDirectoryCopy), HttpStatus.OK);
        } catch (Exception e){
            logger.error("操作失败",e);
            return new ResponseEntity<>(fail(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 复本明细统计查询
     * @param request  request
     * @param response response
     * @param bdCopy bdCopy
     * @return page
     */
    @RequestMapping(value = "/copyDetail",method = RequestMethod.GET)
    public ResponseEntity findCopyDetail(HttpServletRequest request,HttpServletResponse response,BookDirectoryCopy bdCopy){
        try {
            Page<BookDirectoryCopy> page = bdcService.findCopyDetail(new Page<>(request,response),bdCopy);
            return new ResponseEntity<>(success(page), HttpStatus.OK);
        } catch (Exception e){
            logger.error("操作失败",e);
            return new ResponseEntity<>(fail(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
