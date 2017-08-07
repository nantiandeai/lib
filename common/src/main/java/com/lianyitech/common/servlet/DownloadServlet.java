package com.lianyitech.common.servlet;

import com.lianyitech.common.utils.Global;
import com.lianyitech.common.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.util.UriUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zengy on 2016/9/5.
 * 文件下载Servlet
 */
public class DownloadServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private Logger logger = LoggerFactory.getLogger(getClass());

    public void fileOutputStream(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String filepath = req.getRequestURI();
        int index = filepath.indexOf(Global.UPLOADFILES_BASE_URL);
        if (index >= 0) {
            filepath = filepath.substring(index + Global.UPLOADFILES_BASE_URL.length());
        }
        try {
            filepath = UriUtils.decode(filepath, "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            logger.error(String.format("解释文件路径失败，URL地址为%s", filepath), e1);
        }

        resp.setHeader("Content-Type", "application/octet-stream");

        //处理图片情况
        Map<String, String> imgMap = new HashMap();
        imgMap.put("gif", "gif");
        imgMap.put("jpg", "jpeg");
        imgMap.put("jpeg", "jpeg");
        imgMap.put("png", "png");
        imgMap.put("bmp", "bitmap");
        String suffix = filepath.substring(filepath.lastIndexOf(".") + 1, filepath.length()).toLowerCase();
        if (null != imgMap.get(suffix)) {
            resp.setContentType("image/" + imgMap.get(suffix));
        }
        FileCopyUtils.copy(new FileInputStream(new File(Global.getUploadRootPath() + Global.UPLOADFILES_BASE_URL + filepath)), resp.getOutputStream());
        resp.flushBuffer();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        fileOutputStream(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        fileOutputStream(req, resp);
    }

}
