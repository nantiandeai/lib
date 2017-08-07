package com.lianyitech.file.importer;

import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.plugin.berkeley.BreadthCrawler;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

public class Crawler implements Engine.Parser<ConcurrentNavigableMap<String, String>> {
    private static final String path = "/test";

    @Override
    public void process(BlockingQueue<ConcurrentNavigableMap<String, String>> rowTaskQueue, boolean toQueue) throws Exception {
        int currentIndex = initIndex();
        int maxIndex = 24839;
        while (currentIndex < maxIndex) {
            currentIndex = readIndex();
            System.out.println("当前爬取页数" + currentIndex);
            new ParsePage(1, rowTaskQueue);
            writeIndex(++currentIndex);
        }
    }

    private void writeIndex(int i) throws IOException {
        try (FileWriter out = new FileWriter(path, false)) {
            out.write(String.valueOf(i));
            out.flush();
        }
    }

    private int readIndex() throws IOException {
        try (FileReader in = new FileReader(path)) {
            StringBuffer sb = new StringBuffer();
            char[] b = new char[1];
            while (in.read(b) > 0) {
                sb.append(b);
            }
            return Integer.valueOf(sb.toString());
        }
    }

    private int initIndex() throws IOException {
        File f = new File(path);
        if(!f.exists()) {
            f.createNewFile();
            writeIndex(1);
        }
        return readIndex();
    }

    private static class ParsePage extends BreadthCrawler {
        BlockingQueue<ConcurrentNavigableMap<String, String>> rowTaskQueue;
        ParsePage(int currentIndex, BlockingQueue<ConcurrentNavigableMap<String, String>> rowTaskQueue) throws Exception {
            super("crawler", true);
            this.rowTaskQueue = rowTaskQueue;
            super.addSeed("http://www.cnbip.cn/Stock/Search.aspx?Page=" + currentIndex);
            super.addRegex("http://www.cnbip.cn/BaseInfo.*");
            super.setResumable(false);
            super.setThreads(1);
            super.setExecuteInterval(10000);
            super.start(2);
        }
        public void visit(Page page, CrawlDatums next) {
            if(page.matchUrl(".*/BaseInfo/BookInfo.*")) {
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
            if(StringUtils.isNotEmpty(page.select(".book>img", 0).attr("src")))
                map.put("path", page.select(".book>img", 0).attr("src"));
            rowTaskQueue.add(map);
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