package com.lianyitech.modules.catalog.dao;

import com.lianyitech.common.persistence.CrudDao;
import com.lianyitech.common.persistence.annotation.MyBatisDao;
import com.lianyitech.modules.catalog.entity.SupplyLable;

import java.util.List;

/**
 * Created by zcx on 2017/5/18.
 * 书目查缺打印
 */
@MyBatisDao
public interface SupplyLableDao extends CrudDao<SupplyLable> {
    void deleteAll(SupplyLable supplyLable);

}
