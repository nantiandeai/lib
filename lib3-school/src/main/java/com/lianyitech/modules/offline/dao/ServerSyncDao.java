package com.lianyitech.modules.offline.dao;

import com.lianyitech.common.persistence.CrudDao;
import com.lianyitech.common.persistence.annotation.MyBatisDao;
import com.lianyitech.modules.offline.entity.ServerSync;

import java.util.List;

@MyBatisDao
public interface ServerSyncDao extends CrudDao<ServerSync> {
    List<ServerSync> getList(ServerSync serverSync);
    int insert(ServerSync serverSync);
    int update(ServerSync serverSync);
    int updateDate(ServerSync serverSync);
    int delete(ServerSync serverSync);
    int updateStartDate(ServerSync serverSync);
}
