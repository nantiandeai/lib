/**
 * 
 */
package com.lianyitech.modules.catalog.dao;

import com.lianyitech.common.persistence.CrudDao;
import com.lianyitech.common.persistence.annotation.MyBatisDao;
import com.lianyitech.modules.catalog.entity.BookDirectory;
/**
 * 书目管理DAO接口
 * @author zengym
 * @version 2016-09-23
 */
@MyBatisDao
public interface BookDirectoryDao extends CrudDao<BookDirectory> {

}