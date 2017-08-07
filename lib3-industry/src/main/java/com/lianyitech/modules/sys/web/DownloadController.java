package com.lianyitech.modules.sys.web;

import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.Map;

import static com.lianyitech.common.utils.StringUtils.encodParams;

/**
 * Created by tangwei on 2016/11/12.
 */
@Controller
@RequestMapping(value = "/api")
public class DownloadController {
    @RequestMapping(value = "download", method = RequestMethod.GET)
    public void download(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, URISyntaxException {
        String url = encodParams(request.getParameter("url"));
        HttpGet httpGet = new HttpGet(url);
        HttpClient client = HttpClients.createMinimal();

        httpGet.addHeader("Authorization", "Bearer " + request.getParameter("token"));
        HttpResponse res = client.execute(httpGet);
        for (Header h : res.getAllHeaders()) {
            response.setHeader(h.getName(), h.getValue());
        }
        int statusCode = res.getStatusLine().getStatusCode();
        Header[] headers = res.getHeaders("Content-Type");
        if(headers.length > 0)
            response.setContentType(headers[0].getValue());
        response.setStatus(statusCode);
        if (statusCode == 200)
            res.getEntity().writeTo(response.getOutputStream());
        response.flushBuffer();
    }

    @RequestMapping(value="upload", method = RequestMethod.POST)
    public void upload(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpPost httpPost = new HttpPost(request.getParameter("url"));
        httpPost.addHeader("Authorization", "Bearer " + request.getParameter("token"));

        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();

        for (String key: fileMap.keySet()) {
            builder.addBinaryBody(
                    key,
                    fileMap.get(key).getInputStream(),
                    ContentType.MULTIPART_FORM_DATA,
                    fileMap.get(key).getOriginalFilename());
            //new String(.getBytes(Charset.forName("GBK")), Charset.forName("ISO-8859-1")));
        }
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        builder.setCharset(Charset.forName("UTF-8"));
        httpPost.setEntity(builder.build());
        HttpClient client = HttpClients.createMinimal();
        HttpResponse res = client.execute(httpPost);
        for(Header h :res.getAllHeaders()) {
            response.setHeader(h.getName(), h.getValue());
        }
        int statusCode = res.getStatusLine().getStatusCode();
        response.setStatus(statusCode);
        if(statusCode == 200)
            res.getEntity().writeTo(response.getOutputStream());
        response.flushBuffer();

    }

}
