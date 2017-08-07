package com.lianyitech.modules.offline.service;
import com.lianyitech.common.service.CrudService;
import com.lianyitech.common.utils.BeanUtilsExt;
import com.lianyitech.common.utils.IdGen;
import com.lianyitech.modules.offline.dao.ServerAppDao;
import com.lianyitech.modules.offline.dao.ServerCirculateLogDao;
import com.lianyitech.modules.offline.dao.ServerSyncDao;
import com.lianyitech.modules.offline.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;
import java.util.Date;
import java.util.List;
import static com.lianyitech.core.config.ConfigurerConstants.LIB3_EXPORT_PAGESIZE;
@Service
public class CreateDataService extends CrudService<ServerSyncDao, ServerSync> {
    @Autowired
    private ServerSyncDao serverSyncDao;
    @Autowired
    private Environment environment;
    @Autowired
    private ServerAppDao serverAppDao;
    @Autowired
    private ServerCirculateLogDao serverCirculateLogDao;
    @Autowired
    private ServerAppCacheService serverAppCacheService;

    /**
     * 生成读者数据到redis服务
     * @param sync 基本参数
     * @param startDate 同步时间
     */
    @Async
    public void createReaderData(ServerSync sync, Date startDate) {
        ServerSync serverSync = new ServerSync();
        Jedis jedis= serverAppCacheService.getJedis();
        try{
            BeanUtilsExt.copyProperties(serverSync, sync);
            serverSync.setType("1");
            serverSync.setStartDate(startDate);
            updateDatabaseSync(serverSync,"0");
                int pageNo = 1;
                while (true) {//此代码跟导出代码类似
                    serverSync.setPageNo(pageNo);
                    List<ReaderVo> pageList = serverAppDao.queryReaderData(serverSync);
                    //放入缓存
                    serverAppCacheService.setReaderDataToRedis(jedis,serverSync.getOrgId(),pageList);
                    if (pageList.size() < Integer.parseInt(environment.getProperty(LIB3_EXPORT_PAGESIZE))) {
                        break;
                    }
                    pageList = null;
                    pageNo++;
                }
            } catch (Exception e){
                logger.error("生成读者出现异常",e);
                serverSync.setEndDate(null);//出现异常不更新下面的时间
            }finally {
               jedis.close();
                //更改状态为已生成
                updateDatabaseSync(serverSync,"1");
                updateRedisStatus(serverSync.getOrgId(),serverSync.getType());
            }
    }

    /**
     * 生成馆藏数据到redis服务
     * @param sync      基本参数
     * @param startDate 同步时间
     */
    public void createCopyData(ServerSync sync, Date startDate) {
        ServerSync serverSync = new ServerSync();
        Jedis jedis= serverAppCacheService.getJedis();
        try {
            BeanUtilsExt.copyProperties(serverSync, sync);
            serverSync.setType("2");
            serverSync.setStartDate(startDate);
            updateDatabaseSync(serverSync, "0");
            int pageNo = 1;
            long t2 = System.currentTimeMillis();
            while (true) {//此代码跟导出代码类似
                serverSync.setPageNo(pageNo);
                List<CopyVo> pageList = serverAppDao.queryCopyData(serverSync);
                //放入缓存
                serverAppCacheService.setCopyDataToRedis(jedis,serverSync.getOrgId(),pageList);
                if (pageList.size() < Integer.parseInt(environment.getProperty(LIB3_EXPORT_PAGESIZE))) {
                    break;
                }
                pageList = null;
                pageNo++;
            }
            long t = System.currentTimeMillis();
           logger.info("花费时间"+(t-t2));
            // logger.error(sync.getOrgId() + "2=========放进去" + i);
        } catch (Exception e) {
            logger.error("生成读者数据出现异常", e);
            serverSync.setEndDate(null);
        } finally {
            jedis.close();
            //更改状态为已生成
            updateDatabaseSync(serverSync, "1");
            updateRedisStatus(serverSync.getOrgId(),serverSync.getType());
        }
    }

    /**
     * 生成流通同步结果数据到redis服务
     * @param sync 基本参数
     * @param startDate 同步时间
     */
    public void createSyncData(ServerSync sync,Date startDate){
        ServerSync serverSync = new ServerSync();
        Jedis jedis= serverAppCacheService.getJedis();
        try {
            BeanUtilsExt.copyProperties(serverSync,sync);
            serverSync.setType("3");
            serverSync.setStartDate(startDate);
            updateDatabaseSync(serverSync,"0");
            //deleteRedisStatus(serverSync.getOrgId(),serverSync.getType());
            ServerCirculateLog serverCirculateLog = new ServerCirculateLog();
            try {
                BeanUtilsExt.copyProperties(serverCirculateLog, serverSync);//复制属性
                serverCirculateLog.setType(null);//查询条件中有type
            } catch (Exception e) {
                logger.error("复制属性出错", e);
            }
            serverCirculateLog.setStatus("'1','2'");
            int pageNo = 1;
            while (true) {//此代码跟导出代码类似
                serverSync.setPageNo(pageNo);
                List<ServerCirculateLog> pageList = serverCirculateLogDao.getCirculateLog(serverCirculateLog);
                //放入缓存
                serverAppCacheService.setSyncDataToRedis(jedis,serverSync.getOrgId(),pageList);
                if (pageList.size() < Integer.parseInt(environment.getProperty(LIB3_EXPORT_PAGESIZE))) {
                    break;
                }
                pageList = null;
                pageNo++;
            }
        }catch (Exception e){
            logger.error("流通同步结果数据生成异常",e);
            serverSync.setEndDate(null);
        }finally {
            jedis.close();
            //更改状态为已生成
            updateDatabaseSync(serverSync,"1");
            updateRedisStatus(serverSync.getOrgId(),serverSync.getType());
        }
    }
    /**
     * 生成期刊数据到redis服务
     * @param sync      基本参数
     * @param startDate 同步时间
     */
    public void createPeriodicalData(ServerSync sync, Date startDate) {
        ServerSync serverSync = new ServerSync();
        Jedis jedis= serverAppCacheService.getJedis();
        try {
            BeanUtilsExt.copyProperties(serverSync, sync);
            serverSync.setType("4");
            serverSync.setStartDate(startDate);
            updateDatabaseSync(serverSync, "0");
            int pageNo = 1;
            while (true) {//此代码跟导出代码类似
                serverSync.setPageNo(pageNo);
                List<PeriodicalVo> pageList = serverAppDao.queryPeriodicalData(serverSync);
                //放入缓存
                serverAppCacheService.setPeriodicalToRedis(jedis,serverSync.getOrgId(),pageList);
                if (pageList.size() < Integer.parseInt(environment.getProperty(LIB3_EXPORT_PAGESIZE))) {
                    break;
                }
                pageList = null;
                pageNo++;
            }
        } catch (Exception e) {
            logger.error("生成期刊数据出现异常", e);
            serverSync.setEndDate(null);
        } finally {
            jedis.close();
            //更改状态为已生成
            updateDatabaseSync(serverSync, "1");
            updateRedisStatus(serverSync.getOrgId(),serverSync.getType());
        }
    }
    /**
     * 修改数据同步记录（数据生成时间以及状态等）
     * @param serverSync 基本条件
     * @param status 状态
     */
    @Transactional
    private void updateDatabaseSync(ServerSync serverSync,String status){
        ServerSync updateSync = new ServerSync();
        updateSync.setOrgId(serverSync.getOrgId());
        updateSync.setType(serverSync.getType());
        updateSync.setStatus(status);
        updateSync.setCreateDate(new Date());
        updateSync.setUpdateDate(new Date());
        //修改正在生成状态为正在同步
        if("0".equals(status)){
            updateSync.setEndDate(serverSync.getEndDate());
            int updateCount = serverSyncDao.update(updateSync);
            if(updateCount<=0){
                updateSync.setId(IdGen.uuid());
                serverSyncDao.insert(updateSync);
            }
        }else{ //修改状态为生成完--记录同步时间
            updateSync.setStartDate(serverSync.getEndDate());
            serverSyncDao.update(updateSync);
        }
    }

    /**
     * 修改redis状态
     */
    public void updateRedisStatus(String orgId,String type){
        try {
            serverAppCacheService.set(orgId + "_" + type + "_status",3600, "1");//有效期一个小时
        }catch (Exception e){
            logger.error(orgId + "_" + type+"将同步生成完成状态放入缓存出现异常",e);
        }
    }
    public void deleteRedisStatus(String orgId,String type){
        try {
            serverAppCacheService.delete(orgId + "_" + type + "_status");
        }catch (Exception e){
            logger.error(orgId + "_" + type+"将同步生成完成状态放入缓存出现异常",e);
        }
    }
}
