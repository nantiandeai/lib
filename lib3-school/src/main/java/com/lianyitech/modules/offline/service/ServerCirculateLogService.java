package com.lianyitech.modules.offline.service;
import com.lianyitech.common.service.CrudService;
import com.lianyitech.common.utils.StringUtils;
import com.lianyitech.modules.offline.dao.ServerCirculateLogDao;
import com.lianyitech.modules.offline.dao.ServerSyncDao;
import com.lianyitech.modules.offline.dao.ServerTimeDao;
import com.lianyitech.modules.offline.entity.ServerCirculateLog;
import com.lianyitech.modules.offline.entity.ServerSync;
import com.lianyitech.modules.offline.entity.ServerTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


@Service
public class ServerCirculateLogService extends CrudService<ServerCirculateLogDao,ServerCirculateLog> {

    @Autowired
    private Environment environment;
    @Autowired
    private ServerCirculateLogDao serverCirculateLogDao;
    @Autowired
    private ServerTimeDao serverTimeDao;
    @Autowired
    private ServerAppCacheService serverAppCacheService;
    @Autowired
    private CirculateLogSyncService circulateLogSyncService;

    /**
     * 执行同步
     * 1：解析文本到离线流通数据表
     * 2：将离线流通数据表数据跟web端流通进行同步
     * 3：将同步结果生成文件
     * @param orgId 机构
     * @param isVerify 是否验证存在文件处理的时候才进行流通数据同步处理
     */
    @Async
    public void executeSync(String orgId,boolean isVerify){
        try {
            setRedisValue(orgId + "_handle_circulate_data", "1");
            Date parseDate = parseCirculateFile(orgId);
            if (parseDate == null && isVerify) {
                return;
            }
            //目前先不考虑唯一时间（怕处理流通同步的时候出错了，这个时间点的数据永远不会处理了）
            SyncCirculateDate(orgId, null);
        } catch (Exception e) {
            logger.error(orgId + "异步处理流通数据同步出现异常" + isVerify, e);
        } finally {
            deleteRedis(orgId + "_handle_circulate_data");
        }
    }

    /**
     * 设置key-value到redis缓存
     * @param key key值
     * @param value 放入内容
     */
    private void setRedisValue(String key,String value){
        try {
            serverAppCacheService.set(key,3600, value);//一个小时失效
        }catch (Exception e){
            logger.error("放入"+key+"到redis的值为"+value+"出错",e);
        }
    }

    /**
     * 根据key删除redis对应的缓存
     * @param key key
     */
    public void deleteRedis(String key){
        if (StringUtils.isBlank(key)) {
            return;
        }
        try {
            serverAppCacheService.delete(key);
        } catch (Exception e) {
            logger.error("删除redis中" + key + "出现异常", e);
        }
    }

    /**
     * 解析流通数据文件插入到对应的数据库表
     * @param orgId 机构id
     * 返回处理时间---下面同步根据此处理时间同步
     */
    @Transactional
    private Date parseCirculateFile(String orgId) {
        boolean  isReturn = false;
        Date date = new Date();
        String uploadPath = String.join(File.separator, new String[]{environment.getProperty("upload.linux.path"), "offline",environment.getProperty("config.area"),orgId, "circulateData"});
        File file = new File(uploadPath);
        ServerCirculateLog serverCirculateLog = new ServerCirculateLog();
        serverCirculateLog.setOrgId(orgId);
        serverCirculateLog.setCreateBy(orgId);
        serverCirculateLog.setStatus("0");
        if (!file.isDirectory()) {
            return null;
        } else {
            File[] files = file.listFiles();
            if (files == null) {
                return null;
            }
            for (File cFile : files) {
                String fileName = cFile.getName();
                if(!StringUtils.lowerCase(fileName).endsWith(".txt")){
                    continue;
                }
                boolean isDel = true;
                try {
                    if (cFile.isFile() && cFile.exists()) {
                        //根据文件名称查找对应的上传时间记录（文件名等于上传时间 id）
                        long time = 0;
                        String timeId = fileName.substring(0,fileName.lastIndexOf("."));
                        ServerTime serverTime = serverTimeDao.get(timeId);
                        if(serverTime!=null && serverTime.getId()!=null){
                            serverCirculateLog.setServerTimeId(serverTime.getId());
                        }
                     /* 暂时屏蔽时间差
                       try {
                            time = serverTime.getCreateDate().getTime() - serverTime.getClientDate().getTime();
                        }catch (Exception e){
                            logger.error(e.getMessage());
                        }
                        */
                       if(!cFile.renameTo(cFile)){//文件占用无须继续
                           continue;
                       }
                        try (InputStreamReader read = new InputStreamReader(new FileInputStream(cFile), "UTF-8")) {
                            BufferedReader bufferedReader = new BufferedReader(read);
                            String lineTxt;
                            while ((lineTxt = bufferedReader.readLine()) != null) {
                                lineTxt+="@td@---";
                                String [] lines = lineTxt.split("@td@");
                                if(lines.length>0){
                                    serverCirculateLog =  parserToObj(serverCirculateLog,lines,time);
                                    serverCirculateLog.setUpdateDate(date);
                                    serverCirculateLog.setCreateDate(date);
                                    handleCirculateDate(serverCirculateLog);
                                    isReturn = true;
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage());
                    isDel = false;
                }
                if(isDel) {
                    cFile.delete();
                }
            }
        }
        if(isReturn){
            return date;
        }
        return null;
    }
    private ServerCirculateLog parserToObj(ServerCirculateLog serverCirculateLog,String[] lines,long time){
        String id = lines[0]!=null?lines[0]:"";
        String card = lines[1]!=null?lines[1]:"";
        String barCode = lines[2]!=null?lines[2]:"";
        //String orgId = lines[3]!=null?lines[3]:"";
        String opType = lines[4]!=null?lines[4]:"";
        String borrowDateStr = lines[5]!=null?lines[5]:"";
        String returnDateStr = lines[6]!=null?lines[6]:"";
        String shouldReturnDateStr = lines[7]!=null?lines[7]:"";
        //String createBy = lines[8]!=null?lines[8]:"";
        String createDateStr = lines[9]!=null?lines[9]:"";
        String dirType = lines[14]!=null?lines[14]:"0";//默认馆藏
        serverCirculateLog.setId(id);
        serverCirculateLog.setCard(card);
        serverCirculateLog.setBarCode(barCode);
        //serverCirculateLog.setOrgId(orgId);
        serverCirculateLog.setOpType(opType);
        serverCirculateLog.setBorrowDate(parseDate(borrowDateStr,time));
        serverCirculateLog.setReturnDate(parseDate(returnDateStr,time));
        serverCirculateLog.setShouldReturnDate(parseDate(shouldReturnDateStr,time));
        //serverCirculateLog.setCreateBy(createBy);
        serverCirculateLog.setCreateDate(parseDate(createDateStr,time));
        serverCirculateLog.setDirType(dirType);
        return serverCirculateLog;
    }
     private Date parseDate(String dateStr, Long time) {
        if (StringUtils.isNotEmpty(dateStr)) {
            try {
                Date d =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateStr);
                d.setTime(d.getTime()+time);
                return d;
            } catch (ParseException e) {
                return null;
            }
        }
        return null;
    }
    private void handleCirculateDate(ServerCirculateLog log){
         log.setStatus("0");
         this.validateDate(log);
         try {
             //借的流通存在归还的情况下需要修改已有的状态(借归还的情况下可能传多次)
             int updateNum= 0;
             if ("0".equals(log.getOpType()) && log.getReturnDate() != null) {
                 updateNum = serverCirculateLogDao.update(log);
             }
             if (updateNum <= 0) {//否则就是插入
                 serverCirculateLogDao.insert(log);
             }
         }catch (DuplicateKeyException e){
             logger.error("从离线客户端上传了重复流通数据");
         }catch (Exception e){
             logger.error("插入离线流通数据异常");
         }
    }
    private void validateDate(ServerCirculateLog log){
        //插入的时候就需要验证下数据是否正确，验证不通过则直接改成同步存失败
        if(!(StringUtils.isNotEmpty(log.getOpType())&& ( "0".equals(log.getOpType())) || "1".equals(log.getOpType())) ){
            log.setStatus("2");
            log.setErrorInfo("操作类型不能为空并且必须是0或1");
            return;
        }
        boolean validateBorrow = StringUtils.isNotEmpty(log.getCard())&&StringUtils.isNotEmpty(log.getBarCode())&&log.getBorrowDate()!=null;
        boolean validateReturn = StringUtils.isNotEmpty(log.getBarCode())&&log.getBorrowDate()==null && log.getReturnDate()!=null;
        if(!(validateBorrow||validateReturn)){
            log.setStatus("2");
            log.setErrorInfo("数据不完整");

        }
    }

    /**
     * 同步流通数据
     * @param orgId 机构id
     * @param parseDate 入库时间（暂时不传）
     */
    @Transactional
    public void SyncCirculateDate(String orgId,Date parseDate){
        //第一步验证是否存在非同步的数据也就是状态为0的
        ServerCirculateLog serverCirculateLog = new ServerCirculateLog();
        serverCirculateLog.setOrgId(orgId);
        serverCirculateLog.setStatus("0");
        serverCirculateLog.setCreateDate(parseDate);//先屏蔽时间
        List<ServerCirculateLog> list = serverCirculateLogDao.getList(serverCirculateLog);
        ServerCirculateLog log = new ServerCirculateLog();
        log.setOrgId(orgId);
        log.setCreateDate(parseDate);//暂时不传处理时间（类似批次）
        if (list != null && list.size() > 0) {
            //初步验证数据
            Date date = new Date();
            log.setUpdateDate(date);
            circulateLogSyncService.validateSyncData(log);
            //处理只还未借的数据---2017-07-07之后改成借的（存在借也有还的）
            circulateLogSyncService.handleReturn(log);
            //处理历史数据以及有借无归还的数据
            circulateLogSyncService.handleHistoryOrBorrow(log);
            //9:将非同步失败的数据修改成同步成功
            serverCirculateLogDao.updateSyncData(log);
            //处理流通日志表
            circulateLogSyncService.handleCirculateLog(orgId);
            //验证同步时间
            circulateLogSyncService.updateSyncStartDate(orgId,date,"'2','3'");
        }
    }
}
