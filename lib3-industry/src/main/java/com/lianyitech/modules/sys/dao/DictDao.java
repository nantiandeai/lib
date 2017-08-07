/**
 * 
 */
package com.lianyitech.modules.sys.dao;

import com.lianyitech.common.persistence.CrudDao;
import com.lianyitech.common.persistence.annotation.MyBatisDao;
import com.lianyitech.modules.sys.entity.Dict;

import java.util.List;

/**
 * 字典DAO接口
 * 
 * @version 2016-07-16
 */
@MyBatisDao
public interface DictDao extends CrudDao<Dict> {

	public List<Dict> findByParentId(Dict dict);

}
