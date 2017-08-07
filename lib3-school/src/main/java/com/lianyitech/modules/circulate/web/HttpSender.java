package com.lianyitech.modules.circulate.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lianyitech.common.utils.JsonMapper;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import java.io.IOException;
import java.util.*;

import org.apache.http.NameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * HttpClient
 * @author jordan jiang
 * @version 2016-09-20
 */
public class HttpSender {
    private static Logger logger = LoggerFactory.getLogger(HttpSender.class);
    public static HashMap<String, Object> sendJson(String url, HashMap<String, Object> param) {
        HashMap<String, Object> result = new HashMap<>();

        if (param.size()<=0){
            result.put("code", -1);
            result.put("desc", "参数不能为空！");
            return result;
        }

        ObjectMapper mapper = JsonMapper.getInstance();
        //发送请求
        String paramStr;
        try {
            paramStr = mapper.writeValueAsString(param);
        } catch (Exception e) {

            result.put("code", -1);
            result.put("desc", "操作失败");
            return result;
        }

        StringEntity entity;
        try {
            entity = new StringEntity(paramStr, "utf-8");
        } catch (Exception e) {
            result.put("code", -1);
            result.put("desc", e.getMessage());
            return result;
        }
        entity.setContentEncoding("UTF-8");
        entity.setContentType("application/json");

        result = postExecute(url, entity);
        return result;
    }

    public static HashMap<String, Object> sendForm(String url, HashMap<String, String> param){
        HashMap<String, Object> result = new HashMap<>();

        if (param.size()<=0){
            result.put("code", -1);
            result.put("desc", "参数不能为空！");
            return result;
        }

        List<NameValuePair> formparams = new ArrayList<>();
        for(Map.Entry<String, String> me: param.entrySet()) {
            formparams.add(new BasicNameValuePair(me.getKey(), me.getValue()));
        }

        UrlEncodedFormEntity uefEntity;
        try {
            uefEntity = new UrlEncodedFormEntity(formparams, "utf-8");
        } catch (Exception e) {
            logger.error("操作失败",e);
            result.put("code", -1);
            result.put("desc", e.getMessage());
            return result;
        }

        result = postExecute(url, uefEntity);
        return result;
    }

    private static HashMap<String, Object> postExecute(String url, StringEntity entity){
        HashMap<String, Object> result = new HashMap<>();

        ObjectMapper mapper = JsonMapper.getInstance();
        CloseableHttpClient client = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        HttpPost post = new HttpPost(url);

        try {
            post.setEntity(entity);
            response = client.execute(post);
            //请求发送成功，并得到响应
            if (response.getStatusLine().getStatusCode() == 200) {
                //读取服务器返回过来的json字符串数据
                result = mapper.readValue(
                        EntityUtils.toString(response.getEntity()),
                        result.getClass());
            } else {
                result.put("code", response.getStatusLine().getStatusCode());
                result.put("desc", "发送请求失败");
            }
        } catch (Exception e) {
            result.put("code", -1);
            result.put("desc", e.getMessage());
        } finally {
            try {
                if (client != null) client.close();
                if (response != null) response.close();
            } catch (IOException e) {
                logger.error("操作失败",e);
            }
        }

        return result;
    }

}
