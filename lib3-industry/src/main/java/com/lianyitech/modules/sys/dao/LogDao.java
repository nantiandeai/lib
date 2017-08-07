/**
 * 
 */
package com.lianyitech.modules.sys.dao;

import com.lianyitech.common.persistence.CrudDao;
import com.lianyitech.common.persistence.annotation.MyBatisDao;
import com.lianyitech.modules.sys.entity.Log;

/**
 * 日志DAO接口
 * 
 * @version 2016-07-16
 */
@MyBatisDao
public interface LogDao extends CrudDao<Log> {

}
