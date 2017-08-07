package com.lianyitech.modules.common;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 导出公用
 * Created by luzhihuai on 2016/11/3.
 */
public class ExportExcel {
    public void preProcessing(HttpServletResponse response, String fileName) throws UnsupportedEncodingException {
        response.setContentType("application/x-download");
        SimpleDateFormat t = new SimpleDateFormat("yyyyMMddHHmmss");
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("content-disposition", "attachment;filename=" + new String(fileName.getBytes("gb2312"), "ISO8859-1") + t.format(new Date()) + ".xls");
        response.setBufferSize(2048);
    }
}
