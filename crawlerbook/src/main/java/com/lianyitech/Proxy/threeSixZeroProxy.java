package com.lianyitech.Proxy;

import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.plugin.berkeley.BreadthCrawler;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by jordan jiang on 2017/3/1.
 */
public class threeSixZeroProxy {

    private static class  threeSixZeroProxyCrawler extends BreadthCrawler {

        threeSixZeroProxyCrawler(int currentIndex) throws Exception {
            super("proxyCrawler", true);
            super.addSeed("http://www.youdaili.net/Daili/http/list_" + currentIndex + ".html");
            super.addRegex(".*/www.youdaili.net/Daili.*");
            super.setResumable(false);
            super.setThreads(5);//开5个线程
            super.setTopN(10000);
            super.start(2);

        }

        @Override
        public void visit(Page page, CrawlDatums next) {
            if(page.matchUrl(".*/www.youdaili.net/Daili.*")) {
                Elements listP = page.select("div[class=content]");
                for (Element p : listP){
                     for (int i = 0; i<p.childNodeSize(); i++){
                         String tdText=null;  //218.92.220.80:8080@HTTP#江苏省盐城市 网宿科技电信CDN节点
                         try {
                             tdText = p.child(i).text();
                             tdText = tdText.substring(0,tdText.indexOf("@HTTP"));
                             tdText = tdText.replace(":",",");
                         }catch (Exception e){
                         }

                         if (tdText==null||tdText.trim().equals("")){
                             continue;
                         }
                         try {
                             IfCanUseProxy.writeProxy(tdText);
                         } catch (IOException e) {
                             e.printStackTrace();
                         }
                         System.out.println(tdText);
                     }
                }
            }
        }
    }

    public static void crawlerProxy()  throws  Exception{

        threeSixZeroProxy.threeSixZeroProxyCrawler myCrawler;
        for (int i = 1; i<=1; i++ ){
            TimeUnit.SECONDS.sleep(5); // sleeping for 4 SECONDS
            System.out.println("爬出threeSixZeroProxy页面：" + i);
            myCrawler = new threeSixZeroProxy.threeSixZeroProxyCrawler(i);
        }
    }

    public static void main(String[] args) throws  Exception{
        crawlerProxy();
    }
}
