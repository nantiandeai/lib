/**
 * 
 */
package com.lianyitech.modules.sys.dao;

import com.lianyitech.modules.sys.entity.Librarsort;
import com.lianyitech.common.persistence.CrudDao;
import com.lianyitech.common.persistence.annotation.MyBatisDao;

import java.util.List;

/**
 * 中图分类管理DAO接口
 * @author zengzy
 * @version 2016-08-31
 */
@MyBatisDao
public interface LibrarsortDao extends CrudDao<Librarsort> {
    public List<Librarsort> findBySort(Librarsort librarsort);

    public List<Librarsort> findAll();

}