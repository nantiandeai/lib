/**
 * 
 */
package com.lianyitech.modules.sys.dao;

import com.lianyitech.modules.sys.entity.Publishing;
import com.lianyitech.common.persistence.CrudDao;
import com.lianyitech.common.persistence.annotation.MyBatisDao;

/**
 * 出版社管理DAO接口
 * @author zengzy
 * @version 2016-08-31
 */
@MyBatisDao
public interface PublishingDao extends CrudDao<Publishing> {
	
}