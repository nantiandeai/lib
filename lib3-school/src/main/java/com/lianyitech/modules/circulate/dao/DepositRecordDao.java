/**
 * 
 */
package com.lianyitech.modules.circulate.dao;

import com.lianyitech.common.persistence.CrudDao;
import com.lianyitech.common.persistence.annotation.MyBatisDao;
import com.lianyitech.modules.circulate.entity.DepositRecord;

/**
 * 押金记录管理DAO接口
 * @author zengzy
 * @version 2017-07-13
 */
@MyBatisDao
public interface DepositRecordDao extends CrudDao<DepositRecord> {
}