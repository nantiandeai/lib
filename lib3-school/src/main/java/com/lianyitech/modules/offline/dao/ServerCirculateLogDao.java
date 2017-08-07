package com.lianyitech.modules.offline.dao;

import com.lianyitech.common.persistence.CrudDao;
import com.lianyitech.common.persistence.annotation.MyBatisDao;
import com.lianyitech.modules.offline.entity.ServerCirculateLog;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;


@MyBatisDao
public interface ServerCirculateLogDao extends CrudDao<ServerCirculateLog> {
    List<ServerCirculateLog> getList(ServerCirculateLog serverCirculateLog);
    int updateStatusByBarCode(ServerCirculateLog log);
    int updateStatusByCard(ServerCirculateLog log);
    int updateStatusByBorrow(ServerCirculateLog log);
    int insertBill(ServerCirculateLog log);
    int insertPeriBill(ServerCirculateLog log);
    int updateSyncData(ServerCirculateLog log);
    int checkOfflineNotBorrow(ServerCirculateLog log);
    int checkHistoryReturn(ServerCirculateLog log);
    int updateBillOfflineNotBorrow(ServerCirculateLog log);
    int checkReturnCopyStatus(ServerCirculateLog log);
    int checkReturnPeriStatus(ServerCirculateLog log);
    int checkBorrowCopyStatus(ServerCirculateLog log);
    int checkBorrowPeriStatus(ServerCirculateLog log);
    int insertBorrowLog(@Param("orgId") String orgId);
    int insertReturnLog(@Param("orgId") String orgId);
    int updateBorrowCopy(ServerCirculateLog log);
    int updateBorrowPeri(ServerCirculateLog log);
    int updateReturnCopy(ServerCirculateLog log);
    int updateReturnPeri(ServerCirculateLog log);
    List<ServerCirculateLog> getCirculateLog(ServerCirculateLog serverCirculateLog);
    int updateRepBorrow(ServerCirculateLog log);
    int updateRepReturn(ServerCirculateLog log);
    int updateReturnHasBorrow(ServerCirculateLog log);
    Integer circulateLogCount(ServerCirculateLog serverCirculateLog);
}
