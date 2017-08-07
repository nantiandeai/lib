/**
 * 
 */
package com.lianyitech.modules.circulate.dao;

import com.lianyitech.common.persistence.CrudDao;
import com.lianyitech.common.persistence.annotation.MyBatisDao;
import com.lianyitech.modules.circulate.entity.CompenRecord;

/**
 * 赔付管理DAO接口
 * @author
 * @version
 */
@MyBatisDao
public interface CompenDao extends CrudDao<CompenRecord> {

}