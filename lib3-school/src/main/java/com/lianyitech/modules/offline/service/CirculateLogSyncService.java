package com.lianyitech.modules.offline.service;

import com.lianyitech.common.service.CrudService;
import com.lianyitech.modules.offline.dao.ServerCirculateLogDao;
import com.lianyitech.modules.offline.dao.ServerSyncDao;
import com.lianyitech.modules.offline.entity.ServerCirculateLog;
import com.lianyitech.modules.offline.entity.ServerSync;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional(readOnly = false)
public class CirculateLogSyncService extends CrudService<ServerCirculateLogDao,ServerCirculateLog> {
    @Autowired
    private ServerCirculateLogDao serverCirculateLogDao;
    @Autowired
    private ServerSyncDao serverSyncDao;
    /**
     * 验证：1条形码是否存在、2读者证是否存在、
     * 3同条复本重复在借或者只还未借
     * （有借有还的不管，作为历史数据存入到流通）
     */
    public void validateSyncData(ServerCirculateLog log){
        //1：检查条形码是否存在-不存在则同步状态修改同步失败
        serverCirculateLogDao.updateStatusByBarCode(log);
        serverCirculateLogDao.updateStatusByCard(log);
        //3：在借流通数据是否条形码重复
        serverCirculateLogDao.updateRepBorrow(log);
        //4：只还未借流通数据条形码是否重复
        serverCirculateLogDao.updateRepReturn(log);
        //5:有借有还当中“还记录”能对应上“借记录”标示验证通过的 否则需要后续单独做归还操作
        serverCirculateLogDao.updateReturnHasBorrow(log);
    }
    /**
     * 处理还未借记录（在web端借阅，离线端归还数据）---改成只要是没处理的还记录（不管是否有没有借书日期读者证）
     * 如果web端不存在借阅同步不通过，则直接修改web
     * 端借阅的归还状态，以及对应馆藏复本在馆状态
     * @param  log 流通记录实体类
     */
    public void handleReturn(ServerCirculateLog log){
        //5:检查离线客户端（只还未借）和（没有借匹配存在读者卡的还记录） 的数据是否在web端存在借阅，不存在则同步不通过
        serverCirculateLogDao.checkOfflineNotBorrow(log);
        //检查离线客户端中没有匹配借记录的归还记录 是否在web端存在借记录
        serverCirculateLogDao.checkHistoryReturn(log);
        //检查离线客户端只还未借记录的馆藏在web端的状态是借出的情况下才验证通过否则验证不通过
        serverCirculateLogDao.checkReturnCopyStatus(log);
        //检查离线客户端只还未借记录的期刊在web端的状态是借出的情况下才验证通过否则验证不通过
        serverCirculateLogDao.checkReturnPeriStatus(log);
        //6:将web端借阅的离线客户端归还的数据将web单据归还时间进行修改
        serverCirculateLogDao.updateBillOfflineNotBorrow(log);
        //7:将web端借阅的离线客户端归还的数据的复本修改状态为归还
        serverCirculateLogDao.updateReturnCopy(log);
        //将web端借阅的离线客户端归还的数据期刊修改状态为归还
        serverCirculateLogDao.updateReturnPeri(log);
    }
    /**
     * 处理借阅历史（有借有还）数据以及借出未还记录
     * 检查离线借阅流通中有借无还的数据是否在web
     * 端也存在借出没有归还的数据，如果存在则修改离线
     * 借阅流通记录为同步失败
     * @param log 流通记录实体类
     */
    public void handleHistoryOrBorrow(ServerCirculateLog log){
        //8:离线客户端借出未归还的数据如果存在web端借出无归还的复本同步不通过
        serverCirculateLogDao.updateStatusByBorrow(log);
        //检查离线客户端只借未还记录的馆藏在web端的状态是在馆或者预借的情况下才验证通过否则验证不通过
        serverCirculateLogDao.checkBorrowCopyStatus(log);
        //检查离线客户端只借未还记录的期刊在web端的状态是在馆或者预借的情况才验证通过否则验证不通过
        serverCirculateLogDao.checkBorrowPeriStatus(log);
        //9:插入新单据（历史借阅（有借有还）以及借出未还的记录）
        serverCirculateLogDao.insertBill(log);
        serverCirculateLogDao.insertPeriBill(log);

        //10:将第9步中插入的有借无还的单据修改其馆藏复本为借出状态
        serverCirculateLogDao.updateBorrowCopy(log);
        //10:将第9步中插入的有借无还的单据修改其期刊为借出状态
        serverCirculateLogDao.updateBorrowPeri(log);
    }
    /**
     * 处理流通借阅归还日志
     * 补全对应的操作日志
     * @param orgId 机构id
     */
    public void handleCirculateLog(String orgId){
        //10：插入离线客户端同步通过的数据存在单据 不存在 "借"日志
        serverCirculateLogDao.insertBorrowLog(orgId);
        serverCirculateLogDao.insertReturnLog(orgId);
    }
    public void updateSyncStartDate(String orgId,Date date,String  type){
        //看同步时间是否大于当前修改时间
        ServerSync serverSync = new ServerSync();
        serverSync.setOrgId(orgId);
        serverSync.setStartDate(date);
        serverSync.setType(type);
        serverSyncDao.updateStartDate(serverSync);
    }

}
