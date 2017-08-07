package com.lianyitech.schedule;

import com.lianyitech.cnclawler.CnImportDb;
import com.lianyitech.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

/**
 * 定时任务将中国可供书目爬虫到数据库
 */
@Component
@PropertySource("classpath:schedule.properties")
public class ScheduleCnToDb {
    @Autowired
    CnImportDb cnImportDb;
    //@Scheduled(cron = "0 35 18 * * ?")
     @Scheduled(cron = "${jobs.import_cn_db}")//每5分钟执行一次0 0/5 * * * ?
    public void execute(){
        try {
            cnImportDb.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
       /* String a = "@td@ 合肥工业大学校史展览馆更新维修后正式开馆展出 发布日期：2015-09-25  字号：大 中 小  【打印】@td@ @td@ @td@ @td@ @td@ @td@ @td@ @td@ @td@ @td@ @td@ @td@ @td@ ";
        String[] a1 = a.split("@td@");
        System.out.print("a1");
        System.out.print(StringUtils.isEmpty(a1[7].trim()));
        */
        CnImportDb cnImportDb = new CnImportDb();
        try {
            cnImportDb.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
