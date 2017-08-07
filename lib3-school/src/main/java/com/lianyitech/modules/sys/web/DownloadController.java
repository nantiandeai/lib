package com.lianyitech.modules.sys.web;

import com.lianyitech.common.utils.JsonMapper;
import org.apache.http.*;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
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

@Controller
@RequestMapping(value = "/api")
public class DownloadController {
    @RequestMapping(value = "download", method = RequestMethod.GET)
    public void download(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, URISyntaxException {
        CloseableHttpClient client = null;
        CloseableHttpResponse res = null;
        String url = request.getParameter("url").replace(" ", "%20");
     //   url = encodParams(url);
        try {
            HttpGet httpGet = new HttpGet(url);
            client = HttpClients.createMinimal();
            httpGet.addHeader("Authorization", "Bearer " + request.getParameter("token"));
            res = client.execute(httpGet);
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
        } finally {
            if (client != null) {
                client.close();
            }
            if (res != null) {
                res.close();
            }
        }

    }

    @RequestMapping(value="upload", method = RequestMethod.POST)
    public void upload(MultipartHttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        CloseableHttpClient client = null;
        CloseableHttpResponse res = null;
        try {
            HttpPost httpPost = new HttpPost(request.getParameter("url"));
            httpPost.addHeader("Authorization", "Bearer " + request.getParameter("token"));

            Map<String, MultipartFile> fileMap = request.getFileMap();
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
            client = HttpClients.createMinimal();
            res = client.execute(httpPost);
            for(Header h :res.getAllHeaders()) {
                response.setHeader(h.getName(), h.getValue());
            }
            int statusCode = res.getStatusLine().getStatusCode();
            response.setStatus(statusCode);
            if(statusCode == 200) {
                String msg = EntityUtils.toString(res.getEntity());
                Map map = (Map)JsonMapper.fromJsonString(msg,Map.class);
                if(!map.get("code").equals("200")){
                    response.setStatus(400);
                }
                response.getOutputStream().write(msg.getBytes("UTF-8"));
            }
            response.setContentType("text/html");
            response.flushBuffer();
        } finally {
            if (client != null) {
                client.close();
            }
            if (res != null) {
                res.close();
            }
        }
    }

}
