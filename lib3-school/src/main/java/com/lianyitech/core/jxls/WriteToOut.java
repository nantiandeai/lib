package com.lianyitech.core.jxls;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * Created by jordan jiang on 2016/9/14.
 * jordan jiang
 */
public class WriteToOut {
    private static Logger logger = LoggerFactory.getLogger(ReadReaderDirExcel.class);
    public static Boolean writeToResponse(String fileName, InputStream in, HttpServletResponse response) throws UnsupportedEncodingException {

        response.setContentType("application/octet-stream");

        fileName = new String(fileName.getBytes("gb2312"), "ISO8859-1");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("content-Disposition", "attachment;filename=" + fileName );

        response.setBufferSize(2048);

        long size = 0l;
        int len = 0;
        byte[] buffer = new byte[1024];
        OutputStream out = null;
        try {
            out = response.getOutputStream();
            while((len = in.read(buffer)) > 0) {
                out.write(buffer,0,len);
                size = size + len ;
            }
            response.setHeader("Content-Length",String.valueOf(size));
            out.flush();
        }catch(Exception e) {
            return false;
        }finally {
            try {
                in.close();
            } catch (IOException e) {
                logger.error("操作失败",e);
                return false;
            }
        }
        return true;
    }

    public static Boolean writeToResponse2(String fileName, String filePath, HttpServletResponse response) throws UnsupportedEncodingException {
        File file = new File(filePath);
        response.setContentType("application/octet-stream");
        fileName = new String(fileName.getBytes("gb2312"), "ISO8859-1");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("content-Disposition", "attachment;filename=" + fileName );
        response.setHeader("Content-Length",String.valueOf(file.length()));
        response.setBufferSize(2048);
        OutputStream out = null;
        InputStream in = null;
        try {
            in = new FileInputStream(filePath);
            int len = 0;
            byte[] buffer = new byte[1024];
            out = response.getOutputStream();
            while((len = in.read(buffer)) > 0) {
                out.write(buffer,0,len);
            }
            out.flush();
        }catch(Exception e) {
            logger.error("操作失败",e);
            return false;
        }finally {
            try {
                in.close();
            } catch (IOException e) {
                logger.error("操作失败",e);
                return false;
            }
        }
        return true;
    }
}
