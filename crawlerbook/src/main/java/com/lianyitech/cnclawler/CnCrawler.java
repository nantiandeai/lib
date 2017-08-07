package com.lianyitech.cnclawler;

import cn.edu.hfut.dmic.webcollector.model.CrawlDatum;
import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.net.HttpRequest;
import cn.edu.hfut.dmic.webcollector.net.HttpResponse;
import cn.edu.hfut.dmic.webcollector.net.Proxys;
import cn.edu.hfut.dmic.webcollector.plugin.berkeley.BreadthCrawler;
import com.lianyitech.Proxy.IfCanUseProxy;
import com.lianyitech.file.importer.Engine;
import com.lianyitech.file.importer.EngineFactory;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * 中国可供书目爬出代码提出来
 */
public class CnCrawler implements Engine.Parser<ConcurrentNavigableMap<String, String>> {
    private int maxIndex = 24839;
    private static final String start_path = "/opt/crawler/start.txt";
    private static final String end_path = "/opt/crawler/end.txt";
    private int currentIndex = 1;
    private Map<String, String> IpPorts;

    public CnCrawler(Map<String, String> IpPorts){
        this.IpPorts = IpPorts;
    }

    @Override
    public void process(BlockingQueue<ConcurrentNavigableMap<String, String>> rowTaskQueue, boolean toQueue) throws Exception {
        currentIndex = intIndex(start_path);
        maxIndex = read_end_Index(end_path);

        while (currentIndex < maxIndex) {
            currentIndex = readIndex(start_path);

            for (String mapIp : IpPorts.keySet()) {
                String mapPort = IpPorts.get(mapIp);
                System.out.println("(" + IpPorts.toString() + ")当前爬取页数:" + currentIndex);
            }

            CnCrawler.ParsePage p = new CnCrawler.ParsePage(currentIndex, rowTaskQueue, IpPorts);
            if (!p.is_open) {//网页都进不去
                break;
            }

            writeIndex(++currentIndex, start_path);
        }
    }

    private void writeIndex(int i,String path) throws IOException {
        try (FileWriter out = new FileWriter(path, false)) {
            out.write(String.valueOf(i));
            out.flush();
        }
    }
    //查询结束条文件
    private int read_end_Index(String path) throws IOException {
        File f = new File(path);
        if(f.exists()) {
            return readIndex(path);
        }
        return maxIndex;
    }

    private int readIndex(String path) throws IOException {
        try (FileReader in = new FileReader(path)) {
            StringBuffer sb = new StringBuffer();
            char[] b = new char[1];
            while (in.read(b) > 0) {
                sb.append(b);
            }
            return Integer.valueOf(sb.toString());
        }
    }

    private int intIndex(String path) throws IOException {
        File f = new File(path);
        if(!f.exists()) {
            f.createNewFile();
            writeIndex(1,path);
        }
        return readIndex(path);
    }

    private static class ParsePage extends BreadthCrawler {
        //private int p_index=0;
        BlockingQueue<ConcurrentNavigableMap<String, String>> rowTaskQueue;
        boolean is_open = false;//判断网站是否没有被封--通过是否路径重定向判断
        int ifCanUseProxy = 0;//爬到过数据的代理标记下来
        private String[] cookies = {"ASP.NET_SessionId=fbh4sogwgqqmoy5tiqu42zcy", "ASP.NET_SessionId=soyiyn5mmpeoydvpxfy2ryl3", "ASP.NET_SessionId=ozmsa0eu0032531r0emda1bu"};
        private String[] agents = {"Mozilla/5.0 (Windows NT 10.0; WOW64; rv:50.0) Gecko/20100101 Firefox/50.0", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.63 Safari/537.36", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.79 Safari/537.36 Edge/14.14393"};
        private Proxys proxys;

        @Override
        public HttpResponse getResponse(CrawlDatum crawlDatum) throws Exception {
            HttpRequest request = new HttpRequest(crawlDatum);
            crawlDatum.setExecuteTime((int) (Math.random() * 1000) + 500);
            Random random = new Random();//创建随机对象
            int cookiesIdx = random.nextInt(cookies.length - 1);//随机数组索引，nextInt(len-1)表示随机整数[0,(len-1)]之间的值
            request.setCookie(cookies[cookiesIdx]);
            request.setUserAgent(agents[cookiesIdx]);
            request.setProxy(proxys.nextRandom());
            return request.getResponse();
        }

        ParsePage(int currentIndex, BlockingQueue<ConcurrentNavigableMap<String, String>> rowTaskQueue,
                  Map<String,String> IpPorts) throws Exception {
            super("crawler", true);

            proxys = new Proxys();
//            proxys.addEmpty();

            for (String mapIp : IpPorts.keySet()) {
                String mapPort = IpPorts.get(mapIp);
                proxys.add(mapIp,Integer.valueOf(mapPort));
            }

            this.rowTaskQueue = rowTaskQueue;
            super.addSeed("http://www.cnbip.cn/Stock/Search.aspx?Page=" + currentIndex);
            super.addRegex(".*/BaseInfo/BookInfo.*");
            super.setResumable(false);
            super.setThreads(5);//开5个线程
            super.setTopN(10000);
            // super.setExecuteInterval(2000);
            super.start(2);

        }

        @Override
        public void visit(Page page, CrawlDatums next) {
          /*  if(page.getResponse().getRealUrl()==null){//不会重定向别的路径则说明ip没有封
                is_open = true;
            }*/
            if(page.matchUrl(".*/BaseInfo/BookInfo.*")) {
                is_open = true;
                parseContent(page);
                ++ifCanUseProxy;
            }
        }

        private void parseContent(Page page) {
            ConcurrentNavigableMap<String, String> map = new ConcurrentSkipListMap<>();
            setText(map, "title", page, "td>h4");
            setNextText(map, "isbn", page, "td:containsOwn(ISBN)");
            setNextText(map, "price", page, "td:containsOwn(定价)");
            setNextText(map, "author", page, "td:containsOwn(作者)");
            setNextText(map, "publisher", page, "td:containsOwn(出版社)");
            setNextText(map, "pubAddr", page, "td:containsOwn(出版地)");
            setNextText(map, "pubTime", page, "td:containsOwn(出版时间)");
            setNextText(map, "size", page, "td:containsOwn(开本)");
            setNextText(map, "page", page, "td:containsOwn(页数)");
            setNextText(map, "ver", page, "td:containsOwn(版次)");
            setNextText(map, "form", page, "td:containsOwn(装帧)");
            setNextText(map, "sort", page, "td:containsOwn(中图法)");
            setText(map, "content", page, ".span");
            if(StringUtils.isNotEmpty(page.select(".book>img", 0).attr("src")))
                map.put("path", page.select(".book>img", 0).attr("src"));
            rowTaskQueue.add(map);
            System.out.println("爬到数据。。。" + map.get("title") + ":" + map.get("isbn") + ":" +map.get("author"));
        }

        private void setText(ConcurrentNavigableMap<String, String> map, String key, Page page, String s) {
            try {
                map.put(key, page.select(s, 0).text());
            } catch (Exception ignored) {

            }
        }

        private void setNextText(ConcurrentNavigableMap<String, String> map, String key, Page page, String s) {
            try {
                map.put(key, page.select(s, 0).nextElementSibling().text());
            } catch (Exception ignored) {

            }
        }
    }
}
