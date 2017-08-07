package com.lianyitech.modules.report.web;

import com.lianyitech.common.utils.StringUtils;
import com.lianyitech.common.web.ApiController;
import com.lianyitech.common.web.ResponseData;
import com.lianyitech.modules.common.ExportExcel;
import com.lianyitech.modules.report.entity.LibraryAssort;
import com.lianyitech.modules.report.service.LibraryAssortService;
import org.apache.commons.io.IOUtils;
import org.jxls.util.JxlsHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.jxls.common.Context;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;
import java.util.List;

/**
 * 藏书分类报表
 * Created by luzhihuai on 2016/11/10.
 */
@RestController
@RequestMapping(value = "/api/report/libraryAssort")
public class LibraryAssortController extends ApiController {
    @Autowired
    private LibraryAssortService libraryAssortService;

    /**
     * 22个基本部类查询
     * @param libraryAssort 实体
     * @return 。
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<ResponseData> listStuStatistics(LibraryAssort libraryAssort) {
        return new ResponseEntity<>(success(libraryAssortService.libraryStatis(libraryAssort)), HttpStatus.OK);
    }

    /**
     * 22个基本部类查询导出
     * @param libraryAssort 实体
     * @param response response
     * @return null
     * @throws Exception
     */
    @RequestMapping(value = "/exportBookCollection", method = RequestMethod.GET)
    public ResponseEntity<ResponseData> exportBookCollection(LibraryAssort libraryAssort, HttpServletResponse response)
            throws IOException {
        ExportExcel exportExcel= new ExportExcel();
        InputStream is = null;

        String unitName = libraryAssort.getUnitName();
        if(StringUtils.isNotEmpty(unitName)){
            libraryAssort.setUnitName(URLDecoder.decode(unitName, "utf-8"));
        }

        try {
            exportExcel.preProcessing(response, "馆藏分类统计22大类");
            is = this.getClass().getClassLoader().getResourceAsStream("template" + File.separator + "馆藏分类统计22大类.xls");
            List<LibraryAssort> detailList = libraryAssortService.exportBookCollection(libraryAssort);
            Context context = new Context();
            context.putVar("detailList", detailList);
            JxlsHelper.getInstance().processTemplateAtCell(is, response.getOutputStream(), context, "结果!A1");
        } finally {
            IOUtils.closeQuietly(is);
        }
        return null;
    }
    /**
     * 五大类查询
     * @param libraryAssort 实体
     * @return 。
     */
    @RequestMapping(value = "fiveClassStatis", method = RequestMethod.GET)
    public ResponseEntity<ResponseData> fiveClassStatis(LibraryAssort libraryAssort) {
        return new ResponseEntity<>(success(libraryAssortService.fiveClassStatis(libraryAssort)), HttpStatus.OK);
    }
    /**
     * 五个基本部类查询导出
     * @param libraryAssort 实体
     * @param response response
     * @return null
     * @throws Exception
     */
    @RequestMapping(value = "/exportFiveClass", method = RequestMethod.GET)
    public ResponseEntity<ResponseData> exportFiveClass(LibraryAssort libraryAssort, HttpServletResponse response)
            throws IOException {
        ExportExcel exportExcel= new ExportExcel();
        InputStream is = null;

        String unitName = libraryAssort.getUnitName();
        if(StringUtils.isNotEmpty(unitName)){
            libraryAssort.setUnitName(URLDecoder.decode(unitName, "utf-8"));
        }

        try {
            exportExcel.preProcessing(response, "馆藏分类统计五大类");
            is = this.getClass().getClassLoader().getResourceAsStream("template" + File.separator + "馆藏分类统计五大类.xls");
            List<LibraryAssort> detailList = libraryAssortService.exportFiveClass(libraryAssort);
            Context context = new Context();
            context.putVar("detailList", detailList);
            JxlsHelper.getInstance().processTemplateAtCell(is, response.getOutputStream(), context, "结果!A1");
        } finally {
            IOUtils.closeQuietly(is);
        }
        return null;
    }

}
