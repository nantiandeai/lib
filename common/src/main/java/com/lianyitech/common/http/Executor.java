package com.lianyitech.common.http;

import lombok.AllArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

/**
 * HttpClient 封装
 */
public class Executor {

    private static final Logger LOGGER = LoggerFactory.getLogger(Executor.class);
    private static final int MS_TO_S_UNIT = 1000;
    private static final String HTTPS = "https";
    private static HttpsTrustManager httpsTrustManager = new HttpsTrustManager();
    private String url;
    @Setter
    protected int maxConnectionSeconds = 0;
    @Setter
    protected String contentType;
    @Setter
    protected String cookie;
    @Setter
    protected String basic;
    @Setter
    protected Map<String, String> requestParams;

    private Handler handler;

    /**
     * 构造方法，传入处理response的Handler实现
     * @param url 请求url
     * @param handler 处理response的实现类
     */
    public Executor(String url, Handler handler) {
        this.url = url;
        this.handler = handler;
        requestParams = new HashMap<>();
    }

    /**
     * 构造方法，传入HttpServletResponse以构造默认的handler对象
     * @param url url
     * @param response response
     */
    public Executor(String url, HttpServletResponse response) {
        this.url = url;
        this.handler = new DefaultHandler(response);
        requestParams = new HashMap<>();
    }

    /**
     * 处理httpClient请求的response对象的接口
     */
    public interface Handler {
        void handle(CloseableHttpResponse response) throws ParserConfigurationException, SAXException, IOException;
    }

    /**
     * Handler默认实现，将httpResponse写入mvc的HttpServletResponse
     */
    @AllArgsConstructor
    private class DefaultHandler implements Handler {

        private HttpServletResponse response;

        @Override
        public void handle(CloseableHttpResponse res) throws ParserConfigurationException, SAXException, IOException {
            for (Header h : res.getAllHeaders()) {
                response.setHeader(h.getName(), h.getValue());
            }
            int statusCode = res.getStatusLine().getStatusCode();
            response.setStatus(statusCode);
            res.getEntity().writeTo(response.getOutputStream());
            response.flushBuffer();
        }
    }

    /**
     * 启动post请求
     * @throws IOException e
     */
    public void executePost() throws IOException {
        try (CloseableHttpResponse response = send(RequestBuilder.post())) {
            handler.handle(response);
        } catch (Exception e) { e.printStackTrace(); }
    }

    /**
     * 启动get请求
     * @throws IOException e
     */
    public void executeGet() throws IOException {
        try (CloseableHttpResponse response = send(RequestBuilder.get())) {
            handler.handle(response);
        } catch (Exception e) { e.printStackTrace(); }
    }

    /**
     * 发送HttpClient请求
     * @param builder RequestBuilder
     * @return CloseableHttpResponse
     * @throws IOException e
     * @throws NoSuchAlgorithmException e
     * @throws KeyManagementException e
     */
    private CloseableHttpResponse send(final RequestBuilder builder) throws IOException, NoSuchAlgorithmException, KeyManagementException {
        for (String key : requestParams.keySet()) {
            builder.addParameter(key, requestParams.get(key));
        }

        HttpUriRequest request =  builder.setUri(url).build();
        if (StringUtils.isNotEmpty(this.contentType)) {
            request.setHeader(HttpHeaders.CONTENT_TYPE, contentType);
        }
        if (StringUtils.isNotEmpty(this.cookie)) {
            request.setHeader("cookie", cookie);
        }
        if (StringUtils.isNotEmpty(this.basic)) {
            request.setHeader(HttpHeaders.AUTHORIZATION, "Basic " + this.basic);
        }

        final int maxConnMillSeconds = this.maxConnectionSeconds * MS_TO_S_UNIT;
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(maxConnMillSeconds).setConnectTimeout(maxConnMillSeconds).build();
        HttpClientBuilder clientBuilder = HttpClients.custom().setDefaultRequestConfig(requestConfig);

        if (url.toLowerCase().startsWith(HTTPS)) {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new HttpsTrustManager[]{httpsTrustManager}, null);
            clientBuilder.setSSLSocketFactory(new SSLConnectionSocketFactory(sslContext));
        }

        return clientBuilder.build().execute(request);
    }


    /**
     * Default X509TrustManager implement
     */
    private static class HttpsTrustManager implements X509TrustManager {

        @Override
        public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
            //ignore
        }

        @Override
        public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
            //ignore
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    }
}