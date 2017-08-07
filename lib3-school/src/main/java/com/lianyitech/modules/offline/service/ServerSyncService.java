package com.lianyitech.modules.offline.service;

import com.lianyitech.common.service.CrudService;
import com.lianyitech.common.utils.BeanUtilsExt;
import com.lianyitech.common.utils.StringUtils;
import com.lianyitech.modules.offline.dao.ServerAppDao;
import com.lianyitech.modules.offline.dao.ServerCirculateLogDao;
import com.lianyitech.modules.offline.dao.ServerSyncDao;
import com.lianyitech.modules.offline.entity.ServerApp;
import com.lianyitech.modules.offline.entity.ServerCirculateLog;
import com.lianyitech.modules.offline.entity.ServerSync;
import com.lianyitech.modules.offline.utils.VerifyException;
import com.lianyitech.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
@Service
@Transactional(readOnly = false)
public class ServerSyncService extends CrudService<ServerSyncDao,ServerSync> {
    @Autowired
    private ServerAppDao serverAppDao;
    @Autowired
    private ServerCirculateLogDao serverCirculateLogDao;
    @Autowired
    private ServerTimeService serverTimeService;
    @Autowired
    private ServerSyncDao serverSyncDao;
    @Autowired
    private ServerAppCacheService serverAppCacheService;
    @Autowired
    private ServerCirculateLogService serverCirculateLogService;
    /**
     * 准备同步下载通知接口
     * @return map 各类型数据条数等
     * @throws Exception 异常
     */
    public Map<String,Object> preSync() throws Exception {
        String orgId = UserUtils.getOrgId();
        if(StringUtils.isEmpty(orgId)){
            return null;
        }
        ServerApp serverApp = new ServerApp();
        serverApp.setOrgId(UserUtils.getOrgId());
        serverApp.setStatus("0");//设置可用
        List<ServerApp> list = serverAppDao.getList(serverApp);
        if (!(list != null && list.size() > 0)) {
            throw new VerifyException("该学校没有绑定离线客户端!");
        }
        ServerSync serverSync = new ServerSync();
        serverSync.setOrgId(orgId);
        serverSync.setEndDate(new Date());//同步时间为当前系统时间
        Map<String,Date> mapDate = getStartDate(serverSync);
        Integer readerCount = getDatabaseCount(serverSync,"1",mapDate);
        Integer copyCount = getDatabaseCount(serverSync,"2",mapDate);
        ServerCirculateLog serverCirculateLog = new ServerCirculateLog();
        BeanUtilsExt.copyProperties(serverCirculateLog,serverSync);
        Integer count = getDatabaseCount(serverSync,"3",mapDate);
        Integer periCount = getDatabaseCount(serverSync,"4",mapDate);
        if(readerCount>0 || copyCount>0 || count>0 || periCount>0){
            //删除缓存标示
            serverTimeService.deleteRedisStatus(orgId);
            //异步调用生成服务数据接口
            serverTimeService.createRedisData(serverSync,mapDate);
        }else{//不生成数据的时候直接修改缓存状态
            serverTimeService.updateRedisStatus(orgId);
        }
        //看是否存在流通数据没有处理---------------开始
        if (count <= 0) {
            //如果通过上传的正在处理则无需进行
            String isHandle =  serverAppCacheService.get(orgId+"_handle_circulate_data");//放入redis来验证是否此机构正在处理流通数据
            if(!(StringUtils.isNotBlank(isHandle) && isHandle.equals("1"))) {
                serverCirculateLogService.executeSync(orgId, false);
            }
        }
        //看是否存在流通数据没有处理---------------结束
        Map<String,Object> map = new HashMap<>();
        map.put("readerCount",getCount(orgId,"1",readerCount));
        map.put("copyCount",getCount(orgId,"2",copyCount));
        map.put("circulateCount",getCount(orgId,"3",count));
        map.put("periCount",getCount(orgId,"4",periCount));
        return map;
    }
    /**
     * 根据机构id和类型时间区间
     * @param sync 机构等条件
     * @param type 类型
     * @param map 同步时间map
     * @return Integer 数据条数
     * @throws Exception 异常
     */
    private Integer getDatabaseCount(ServerSync sync,String type,Map<String,Date> map) throws Exception{
        ServerSync con = new ServerSync();
        BeanUtilsExt.copyProperties(con,sync);
        con.setStartDate(map.get(type));
        con.setType(type);
        Integer count;
        switch (type){
            case "1":
                count = serverAppDao.readerCount(con);
                break;
            case "2":
                count = serverAppDao.copyCount(con);
                break;
            case "3":
                ServerCirculateLog serverCirculateLog = new ServerCirculateLog();
                BeanUtilsExt.copyProperties(serverCirculateLog,con);
                serverCirculateLog.setType(null);
                serverCirculateLog.setStatus("'1','2'");
                count = serverCirculateLogDao.circulateLogCount(serverCirculateLog);
                break;
            default:
                count = serverAppDao.periodicalCount(con);
                break;
        }
       return count;
    }

    /**
     * 根据机构id得到各类型上次同步的时间
     * @param sync 参数条件
     * @return 时间map
     */
    private Map<String,Date> getStartDate(ServerSync sync){
        Map<String,Date> map = new HashMap<>();
        List<ServerSync> list = serverSyncDao.getList(sync);
        for(ServerSync s : list){
            Date d = s.getStartDate();
            if(s.getStartDate()!=null){
                d = new Date(d.getTime()-3*60*1000);//开始时间往前推3分钟，以避免同时跟导入数据进行插入数据会需要时间
            }
            map.put(s.getType(),d);
        }
        return map;
    }
    /**
     * 读取redis缓存的条数跟数据库条数相加
     * @param orgId 机构id
     * @param type 类型
     * @param count 数据库条数
     * @return 返回Long
     * @throws Exception 异常
     */
    private Long getCount(String orgId,String type,Integer count) throws Exception{
        Long c = 0L;
        try {
            c = serverAppCacheService.scard(orgId,type);
        }catch (Exception e){
            logger.error("根据机构类型读取缓存条数出现异常",e);
        }
        return count+c;
    }



}
