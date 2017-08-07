package com.lianyitech.schedule;

import com.lianyitech.Proxy.IfCanUseProxy;
import com.lianyitech.cnclawler.CnImportTxt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zengyuanmei on 2016/12/9.
 */
@Component
@PropertySource("classpath:schedule.properties")
public class SchedulaCnToTxt {
    @Autowired
    CnImportTxt cnImportTxt;
    //    @Scheduled(cron = "${jobs.import_cn_txt}")//每5分钟执行一次0 0/5 * * * ?
    public void execute(){
        try {

            IfCanUseProxy.crawlerProxy();

            Map<String, String> IpPorts = new HashMap<>();
            IfCanUseProxy.readProxy(IpPorts);

            cnImportTxt.execute(IpPorts);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
