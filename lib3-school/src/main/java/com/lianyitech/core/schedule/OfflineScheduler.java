package com.lianyitech.core.schedule;
import com.lianyitech.modules.offline.entity.ServerApp;
import com.lianyitech.modules.offline.service.ServerAppService;
import com.lianyitech.modules.offline.service.ServerCirculateLogService;
import com.lianyitech.modules.offline.service.ServerSyncService;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.List;

@Component
@PropertySource("classpath:project.properties")
public class OfflineScheduler {
    private static Logger logger = LoggerFactory.getLogger(OfflineScheduler.class);
    @Resource
    private ServerAppService serverAppService;
    @Resource
    private ServerCirculateLogService serverCirculateLogService;
    //@Scheduled(cron = "${offline.task.file.create}")
    public void execute(){
        logger.info("定时执行离线客户端-读者数据以及馆藏数据生成文本文件!");
        ServerApp serverApp = new ServerApp();
        serverApp.setStatus("0");//设置可用
        List<ServerApp> list = serverAppService.listServerApp(serverApp);
        for (ServerApp sApp : list) {
            try {
                //生成读者数据(全量生成,因为这里是需要)
                //serverSyncService.createRedisData(sApp.getOrgId());
            }catch (Exception e){
                logger.error(e.getMessage());
            }
        }
    }
    //@Scheduled(cron = "${offline.task.circulate.create}")
    public void executeSync() {
        logger.info("定时任务执行离线客户端上传上来的数据到web端");
        ServerApp serverApp = new ServerApp();
        serverApp.setStatus("0");//设置可用
        List<ServerApp> list = serverAppService.listServerApp(serverApp);
        for (ServerApp sApp : list) {
            try {
                serverCirculateLogService.SyncCirculateDate(sApp.getOrgId(),null);
            }catch (Exception e){
                logger.error("定时任务执行离线客户端出现异常",e);
            }
        }
    }


}
