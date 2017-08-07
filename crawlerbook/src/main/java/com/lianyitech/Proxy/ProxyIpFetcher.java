package com.lianyitech.Proxy;

import com.lianyitech.cnclawler.CnImportTxt;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection.KeyVal;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by jordan jiang on 2017/2/27.
 */
public class ProxyIpFetcher {

    Logger logger = LoggerFactory.getLogger(ProxyIpFetcher.class);

    private static ExecutorService threadPool = Executors.newFixedThreadPool(15);

    /**
     *
     * @author xxx
     * 2014年12月24日 下午1:41:51
     * @version V1.0
     */
    private static class ValidateCallable implements Callable<KeyVal> {

        String ip;
        String port;
        String targetUrl;
        String[] keyWords;

        public ValidateCallable(String ip, String port, String targetUrl, String[] keyWords) {
            this.ip = ip;
            this.port = port;
            this.targetUrl = targetUrl;
            this.keyWords = keyWords;
        }

        @Override
        public KeyVal call() throws Exception {
            //使用代理请求页面源代码
            String htmlStr = "";
//            GzipUtil.proxyGet(targetUrl, ip, port);
            Document document = Jsoup.parse(htmlStr);
            Elements kwMetas = document.head().select("meta[name=keywords]");
            if (kwMetas != null && !kwMetas.isEmpty()) {
                Element kwMate = kwMetas.first();
                String kwCont = kwMate.attr("content");

                boolean contains = false;
                for (String kw : keyWords) {
                    if (contains = kwCont.contains(kw)) {
                        break ;
                    }
                }
                if (contains) {
                    //包含关键时认为此代理可用
                    return KeyVal.create(ip, port);
                }
            }
            return null;
        }

    }

    /**
     * 获取一个可用的代理ip
     * @param targetUrl 代理要访问的网站
     * @param keyWords 目标页面包含的关键字(用以验证代理确实请求到目标页面)
     * @return ip port pair
     */
    public static KeyVal fetchOne(final String targetUrl, final String[] keyWords) {
        String dlWebUrl = "http://www.kuaidaili.com/free/inha/1";
        Document doc = null;
        try {
            doc = Jsoup.connect(dlWebUrl)
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.64 Safari/537.31")
                    .header("User-Agent",
                            "Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.8.1b2) Gecko/20060823 SeaMonkey/1.1a")
                    .timeout(5000).get();
        } catch (Exception e) {
            e.printStackTrace();
//            logger.info("connet {} fail", dlWebUrl + e.getMessage());
            return null;
        }
        Element listDiv = doc.getElementById("list");
        Elements trs = listDiv.select("table tbody tr");

        List<Future<KeyVal>> futureList = new ArrayList<Future<KeyVal>>(trs.size());
        for (final Element tr : trs) {
            String ip = tr.child(0).text();
            String port = tr.child(1).text();
            futureList.add(threadPool.submit(new ValidateCallable(ip, port,
                    targetUrl, keyWords)));
        }
        for (int i = 0; i < 3; i++) {
            Thread.yield();//尝试启动验证线程
        }

        for (Future<KeyVal> future : futureList) {
            try {
                KeyVal kv = future.get();
                if (kv == null) {
                    //继续验证,直到获取一个可用的代理
                    continue ;
                }
                return kv;
            } catch (InterruptedException e) {
//                logger.error("操作失败",e);
                //i don't what happened, so let it go.

            } catch (ExecutionException e) {
                //call执行异常, 记录日志
//                logger.error("操作失败",e);
            }
        }
        return null;
    }

    public static void readISO(){
        File dir = new File("/opt/crawler/iso");
        File isoFile = new File("/opt/crawler/isoAll.txt");

        if(dir.exists() && dir.isDirectory()) {
            File[] dirFiles = dir.listFiles();
            //循环文件夹里面的文件
            for(File temp : dirFiles != null ? dirFiles : new File[0]) {

                if (temp.isFile() && temp.exists()) { //判断文件是否存在

                    String isoName = temp.getName().substring(temp.getName().lastIndexOf("_") + 1,
                            temp.getName().length());

                    if (isoFile.length() >= 500000000) {  //文件大于100兆
                        File bakFile = new File("/opt/crawler/isoAll_" + isoName);
                        isoFile.renameTo(bakFile);
                    }

                    try {
//                        String encoding="GBK";
                        InputStreamReader read = new InputStreamReader(new FileInputStream(temp));//考虑到编码格式
                        BufferedReader bufferedReader = new BufferedReader(read);
                        String lineTxt;
                        while ((lineTxt = bufferedReader.readLine()) != null) {
                            FileWriter out = new FileWriter(isoFile, true);
                            try {
                                out.write(lineTxt);
                                out.write("\r\n");
                                out.flush();
                            } catch (Exception e) {
                            } finally {
                                out.close();
                            }
                        }
                        read.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static void main(String[] args) throws  Exception{

//        String[] str = new String[]{"jordan"};
//        ProxyIpFetcher.fetchOne("",str);
        readISO();
    }

}
