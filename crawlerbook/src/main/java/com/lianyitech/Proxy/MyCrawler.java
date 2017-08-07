package com.lianyitech.Proxy;

import cn.edu.hfut.dmic.webcollector.model.CrawlDatum;
import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.net.HttpRequest;
import cn.edu.hfut.dmic.webcollector.net.HttpResponse;
import cn.edu.hfut.dmic.webcollector.net.Proxys;
import cn.edu.hfut.dmic.webcollector.plugin.berkeley.BreadthCrawler;
import com.lianyitech.cnclawler.CnImportTxt;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * Created by jordan jiang on 2017/3/2.
 *
 */
public class MyCrawler {

    Logger logger = LoggerFactory.getLogger(MyCrawler.class);

    private static final int TASK_LIST_SIZE = 1000;
    private int maxIndex = 50000;
    private static final String start_path = "/opt/crawler/start.txt";
    private static int currentIndex = 1;
    private Map<String, String> IpPorts;

    public MyCrawler(Map<String, String> IpPorts){
        this.IpPorts = IpPorts;
    }

    public void process(BlockingQueue<ConcurrentNavigableMap<String, String>> rowTaskQueue) throws Exception {
        currentIndex = initStartIndex(start_path);

        while (currentIndex < maxIndex) {
            currentIndex = readIndex(start_path);

            System.out.println("(" + IpPorts.toString() + ")当前爬取页数:" + currentIndex);

            MyCrawler.ParsePage p = new MyCrawler.ParsePage(currentIndex, rowTaskQueue, IpPorts);
            if (!p.is_open) {
                break;
            }

            writeIndex(++currentIndex, start_path);
        }
    }

    private void writeIndex(int i,String path) throws IOException {
        FileWriter out;
        out = new FileWriter(path, false);
        try {
            out.write(String.valueOf(i));
            out.flush();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            out.close();
        }
    }

    private int initStartIndex(String path) throws IOException {
        File f = new File(path);
        if(!f.exists()) {
            f.createNewFile();
            writeIndex(1,path);
        }
        return readIndex(path);
    }

    private int readIndex(String path) throws IOException {
        FileReader in = new FileReader(path);
        try {
            StringBuffer sb = new StringBuffer();
            char[] b = new char[1];
            while (in.read(b) > 0) {
                sb.append(b);
            }
            return Integer.valueOf(sb.toString());
        }catch (Exception e){
            e.printStackTrace();
            return currentIndex;
        } finally{
            in.close();
        }
    }

    private static class ParsePage extends BreadthCrawler {

        BlockingQueue<ConcurrentNavigableMap<String, String>> rowTaskQueue;
        boolean is_open = false;
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
            super("proxyCrawler", true);

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
            super.start(2);

        }

        @Override
        public void visit(Page page, CrawlDatums next) {
            if(page.matchUrl(".*/BaseInfo/BookInfo.*")) {
                is_open = true;
                parseContent(page);
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
            if(StringUtils.isNotEmpty(page.select(".book>img", 0).attr("src"))) {
                map.put("path", page.select(".book>img", 0).attr("src"));
            }

            try {
                if (rowTaskQueue.size()>=TASK_LIST_SIZE) {
                    rowTaskQueueToFile(rowTaskQueue);
                }
            }catch (Exception e){
                e.printStackTrace();
            }

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

    public static void startCrawler(Map<String, String> IpPorts)  throws  Exception{
        BlockingQueue<ConcurrentNavigableMap<String, String>> rowTaskQueue =
                new ArrayBlockingQueue<>(TASK_LIST_SIZE);

        int count = 0;
        for (String Ip : IpPorts.keySet()) {

            rowTaskQueue.clear();

            String Port = IpPorts.get(Ip);
            Map<String, String> tmpIpPorts = new HashMap<>();
            tmpIpPorts.put(Ip, Port);

            System.out.println("当前第（" + count++ + "）代理：" + tmpIpPorts.toString());

            MyCrawler myCrawler = new MyCrawler(tmpIpPorts);
            myCrawler.process(rowTaskQueue);

            rowTaskQueueToFile(rowTaskQueue);
        }
    }

    public static void rowTaskQueueToFile(BlockingQueue<ConcurrentNavigableMap<String, String>> rowTaskQueue)
            throws Exception{

        File file = new File("/opt/crawler/iso.txt");
        if (file.length() >= 500000000 ){  //文件大于500兆
            File bakFile = new File("/opt/crawler/iso/iso_" + currentIndex + ".txt");
            file.renameTo(bakFile);
        }

        for (ConcurrentNavigableMap<String, String> map : rowTaskQueue) {
            FileWriter out = new FileWriter(file, true);
            try {
                out.write(CnImportTxt.maptostr(map));
                out.write("\r\n");
                out.flush();
            } catch (Exception e) {
            } finally {
                out.close();
            }
        }
    }

    public static void main(String[] args) throws  Exception{

//        File f = new File("/crawler/iso.txt");
//        if (!f.exists()) {
//            f.createNewFile();
//        }
//
//        Map<String, String> IpPorts = new HashMap<>();
//        IfCanUseProxy.readProxy(IpPorts);
//
//        startCrawler(IpPorts);

        BlockingQueue<ConcurrentNavigableMap<String, String>> rowTaskQueue = new ArrayBlockingQueue<>(2);
        ConcurrentNavigableMap<String, String> dataMap = new ConcurrentSkipListMap<>();
        dataMap.put("aaa","aaa");
        rowTaskQueue.add(dataMap);
        MyCrawler.rowTaskQueueToFile(rowTaskQueue);
    }
}
