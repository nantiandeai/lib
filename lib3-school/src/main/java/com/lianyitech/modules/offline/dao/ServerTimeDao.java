package com.lianyitech.modules.offline.dao;


import com.lianyitech.common.persistence.CrudDao;
import com.lianyitech.common.persistence.annotation.MyBatisDao;
import com.lianyitech.modules.offline.entity.ServerTime;

@MyBatisDao
public interface ServerTimeDao extends CrudDao<ServerTime> {
}
