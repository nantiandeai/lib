/**
 * 
 */
package com.lianyitech.modules.sys.dao;

import com.lianyitech.common.persistence.CrudDao;
import com.lianyitech.common.persistence.annotation.MyBatisDao;
import com.lianyitech.modules.sys.entity.Librarsort;

import java.util.List;

/**
 * 主题词管理DAO接口
 * @author zengzy
 * @version 2016-08-31
 */
@MyBatisDao
public interface SubjectDao extends CrudDao<Librarsort> {
    public List<Librarsort> getSubjects(Librarsort librarsort);
}