package com.lianyitech.Proxy;

import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.plugin.berkeley.BreadthCrawler;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by jordan jiang on 2017/2/28.
 */
public class IfCanUseProxy {

    private static class  MyCrawler extends BreadthCrawler {

        MyCrawler(int currentIndex) throws Exception {

            super("MyCrawler", true);
            super.addSeed("http://www.xicidaili.com/nn/" + currentIndex);
            super.setResumable(false);
            super.setThreads(1);//开5个线程
            super.setTopN(10000);
            super.start(2);

        }

        @Override
        public void visit(Page page, CrawlDatums next) {
            if(page.matchUrl(".*/www.xicidaili.com.*")) {
                Elements listOdd = page.select(".odd");

                String question=page.select("div[id=zh-question-detail]").text();

                for (final Element td : listOdd) {
                    String ip=null;
                    String port = null;

                    for (int i = 0 ; i<td.childNodeSize(); i++){

                        String tdText=null;
                        try {
                            tdText = td.child(i).text();
                        }catch (Exception e){
//                            e.printStackTrace();
                        }

                        if (tdText!=null) {
                            Pattern pIp = Pattern.compile("^[0-9]+[.]{1}[0-9]+[.]{1}[0-9]+[.]{1}[0-9].*");
                            Matcher mIp = pIp.matcher(tdText);
                            if (mIp.matches()) {
                                ip = tdText;
                            }

                            Pattern pPort = Pattern.compile("^[0-9]+");
                            Matcher mPort = pPort.matcher(tdText);
                            if (mPort.matches()) {
                                port = tdText;
                            }
                        }
                    }

                    try {
                        writeProxy(ip + "," + port);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.out.println(ip + "," + port);
                }

            }
        }
    }

    public static boolean IfCanUseProxy(String ip, int port)  throws  Exception{
        int i;
        //以下地址是代理服务器的地址
        try {
            Socket socket = new Socket(ip, port);
            //写与的内容就是遵循HTTP请求协议格式的内容，请求百度
            socket.getOutputStream().write(new String("GET http://www.baidu.com/ HTTP/1.1\r\n\r\n").getBytes());
            byte[] bs = new byte[1024];
            InputStream is = socket.getInputStream();
            while ((i = is.read(bs)) > 0) {
                return true;
            }
            is.close();
        }catch (Exception e){
//            e.printStackTrace();
        }

        System.setProperty("http.proxySet", "true");
        System.setProperty("http.proxyHost", ip);
        System.setProperty("http.proxyPort", String.valueOf(port));

        //直接访问目的地址
        URL url = new URL("http://www.baidu.com");
        URLConnection con = url.openConnection();
        InputStreamReader isr = new InputStreamReader(con.getInputStream());
        char[] cs = new char[1024];
        i = 0;
        while ((i = isr.read(cs)) > 0) {
            return true;
        }
        isr.close();

        return false;
    }

    public static void delProxy() throws IOException {
        try {
            File file = new File("/opt/crawler/proxy.txt");
            if(file.exists()) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                formatter.format(new Date());

                boolean success = file.renameTo(new File("/opt/crawler/proxy/",
                        formatter.format(new Date()) + "_" + file.getName()));

                if (success){
                    file.delete();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void writeProxy(String conent) throws IOException {
        BufferedWriter out = null;

        if ( ("".equals(conent)) || (conent==null)){
            return;
        }

        String file = "/opt/crawler/proxy.txt";
        try {
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true)));
            out.write(conent+"\r\n");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void readProxy(Map<String,String> IpPorts){
        try {
            String encoding="GBK";

            File file=new File("/opt/crawler/proxy.txt");
            if(file.isFile() && file.exists()){ //判断文件是否存在
                InputStreamReader read = new InputStreamReader(new FileInputStream(file),encoding);//考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while((lineTxt = bufferedReader.readLine()) != null){
                    try {
                        if (lineTxt.indexOf("#")>=0){
                            continue;
                        }
                        String[] sourceStrArray = lineTxt.split(",");
                        IpPorts.put(sourceStrArray[0].trim(),sourceStrArray[1].trim());
                    }catch (Exception e){
                        e.printStackTrace();
                        continue;
                    }

                }
                read.close();
            }
        } catch (Exception e) {
            System.out.println("读取文件内容出错");
            e.printStackTrace();
        }
    }

    public static void delOneLineProxy(String ProxyStr) {
        BufferedReader br=null;
        BufferedWriter bw=null;

        try {
            File file=new File("/opt/crawler/proxy.txt");
            br = new BufferedReader(new FileReader(file.getAbsolutePath()));
            String str;
            List list = new ArrayList();
            while ((str = br.readLine()) != null) {
                if ( str.trim().equals(ProxyStr.trim())) {
                    continue;
                }
                list.add(str);
            }

            bw = new BufferedWriter(new FileWriter(file.getAbsolutePath()));
            for (int i = 0; i < list.size(); i++) {
                bw.write(list.get(i).toString());
                bw.newLine();
            }

            bw.flush();
            System.out.println(ProxyStr + "删除成功!");
        }catch (Exception e){
        }finally {
            try {
                br.close();
                bw.close();
            } catch (IOException e) {
            }
        }
    }

    public static void crawlerProxy() throws Exception{

        delProxy();

        MyCrawler myCrawler;
        for (int i = 1; i<=20; i++ ){
            TimeUnit.SECONDS.sleep(5); // sleeping for 4 SECONDS
            System.out.println("爬出来crawlerProxy页面：" + i);
            myCrawler = new MyCrawler(i);
        }
    }

    public static void main(String[] args) throws  Exception{
        crawlerProxy();
    }
}
