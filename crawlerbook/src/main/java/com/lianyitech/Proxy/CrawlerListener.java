package com.lianyitech.Proxy;

import com.lianyitech.cnclawler.CnImportTxt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jordan jiang on 2017/3/1.
 */
public class CrawlerListener implements ServletContextListener {

    Logger logger = LoggerFactory.getLogger(CrawlerListener.class);

    private ServletContext context = null;

    public void contextDestroyed(ServletContextEvent event){
        //Output a simple message to the server's console
        System.out.println("The Simple Web App. Has Been Removed");
        this.context = null;
    }
    // 这个方法在Web应用服务做好接受请求的时候被调用。
    public void contextInitialized(ServletContextEvent event){
        this.context = event.getServletContext();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date dateOne = new Date();
        Date dateTwo = new Date();

        try {
            dateOne = formatter.parse(formatter.format(new Date()));
            dateTwo = dateOne;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int count=0;
        while (true) {
            try {
                dateTwo = formatter.parse(formatter.format(new Date()));
                //返回 0 表示时间日期相同
                //返回 1 表示日期1>日期2
                //返回 -1 表示日期1<日期2
                if( dateOne.compareTo(dateTwo)!=0 ){
                    dateOne = dateTwo;
                    //爬下代理服务器ip和port
                    IfCanUseProxy.crawlerProxy();
//                    threeSixZeroProxy.crawlerProxy();
                }

                Map<String, String> IpPorts = new HashMap<>();
                IfCanUseProxy.readProxy(IpPorts);

                if (IpPorts.size()<=0){
                    IfCanUseProxy.crawlerProxy();
                }

                IfCanUseProxy.readProxy(IpPorts);
                if (IpPorts.size()<=0){
                    threeSixZeroProxy.crawlerProxy();
                }

                count++;
                logger.info("循环次数：" + count);

                MyCrawler.startCrawler(IpPorts);

            }catch (Exception e){
                e.printStackTrace();
                continue;
            }
        }
    }
}
