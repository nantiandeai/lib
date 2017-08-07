package com.lianyitech.modules.catalog.dao;

import com.lianyitech.common.persistence.CrudDao;
import com.lianyitech.common.persistence.annotation.MyBatisDao;
import com.lianyitech.modules.catalog.entity.BarcodeRecord;

/**
 * Created by zcx on 2017/2/27.
 * BarcodeRecordDao
 */
@MyBatisDao
public interface BarcodeRecordDao extends CrudDao<BarcodeRecord> {

}
