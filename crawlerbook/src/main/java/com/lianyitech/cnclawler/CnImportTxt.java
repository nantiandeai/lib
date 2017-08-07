package com.lianyitech.cnclawler;

import com.lianyitech.Proxy.IfCanUseProxy;
import com.lianyitech.file.importer.EngineFactory;
import com.lianyitech.file.importer.Importer;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentNavigableMap;

/**
 * 将中国可供书目爬虫到txt文本（---这里之所以这么弄是中国可供书目访问太过频繁ip被封--可以直接通过多处爬到txt）
 */
@Service
public class CnImportTxt {

    //    @Async
    public void execute(Map<String, String> IpPorts) throws Exception {

        File f = new File("/opt/crawler/iso.txt");
        if (!f.exists()) {
            f.createNewFile();
        }

        for (String Ip : IpPorts.keySet()) {

            String Port = IpPorts.get(Ip);
            Map<String, String> tmpIpPorts = new HashMap<>();
            tmpIpPorts.put(Ip, Port);

            try (FileWriter out = new FileWriter("/opt/crawler/iso.txt", true)) {
                new Importer<>(
                        new CnCrawler(tmpIpPorts),
                        EngineFactory.concurrentSkipListMapDummy,
                        map -> {
                            out.write(maptostr(map));
                            out.write("\r\n");
                            out.flush();
                        }
                ).doImport();
            }
        }
    }

    public static String maptostr(ConcurrentNavigableMap<String, String> data){
        String split_str = "@td@ ";
        String isbn = (data.get("isbn")!=null?data.get("isbn").toString():"")+split_str;
        String title = (data.get("title")!=null?data.get("title").toString():"")+split_str;
        String author = (data.get("author")!=null?data.get("author").toString():"")+split_str;
        String price = (data.get("price")!=null?data.get("price").toString():"")+split_str;
        String publisher = (data.get("publisher")!=null?data.get("publisher").toString():"")+split_str;
        String pubAddr = (data.get("pubAddr")!=null?data.get("pubAddr").toString():"")+split_str;
        String pubTime = (data.get("pubTime")!=null?data.get("pubTime").toString():"")+split_str;
        String size = (data.get("size")!=null?data.get("size").toString():"")+split_str;
        String page = (data.get("page")!=null?data.get("page").toString():"")+split_str;
        String ver = (data.get("ver")!=null?data.get("ver").toString():"")+split_str;
        String form = (data.get("form")!=null?data.get("form").toString():"")+split_str;
        String sort = (data.get("sort")!=null?data.get("sort").toString():"")+split_str;
        String content = (data.get("content")!=null?data.get("content").toString():"")+split_str;
        String path = (data.get("path")!=null?data.get("path").toString():"")+split_str;
        return isbn+title+author+price+publisher+pubAddr+pubTime+size+page+ver+form+sort+path+content;
    }

    public static void main(String[] args) throws  Exception{

        Map<String, String> IpPorts = new HashMap<>();
        IfCanUseProxy.readProxy(IpPorts);

        int count=0;
        while (true) {
            count++;
            System.out.println("循环次数：" + count);
            CnImportTxt cnImportTxt = new CnImportTxt();
            cnImportTxt.execute(IpPorts);
        }

    }
}
