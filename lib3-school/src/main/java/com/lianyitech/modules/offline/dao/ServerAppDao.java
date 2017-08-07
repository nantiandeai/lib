package com.lianyitech.modules.offline.dao;

import com.lianyitech.common.persistence.CrudDao;
import com.lianyitech.common.persistence.annotation.MyBatisDao;
import com.lianyitech.modules.offline.entity.*;

import java.util.List;
@MyBatisDao
public interface ServerAppDao extends CrudDao<ServerApp> {
    int insert(ServerApp serverApp);
    int update(ServerApp serverApp);
    List<ServerApp> getList(ServerApp serverApp);
    List<ReaderVo> queryReaderData(ServerSync serverSync);
    List<CopyVo> queryCopyData(ServerSync serverSync);
    List<PeriodicalVo> queryPeriodicalData(ServerSync serverSync);
    int updateDate(ServerApp serverApp);
    Integer readerCount(ServerSync serverSync);
    Integer copyCount(ServerSync serverSync);
    Integer periodicalCount(ServerSync serverSync);
}
