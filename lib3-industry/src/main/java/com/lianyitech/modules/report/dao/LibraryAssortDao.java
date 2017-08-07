/**
 *
 */
package com.lianyitech.modules.report.dao;

import com.lianyitech.common.persistence.CrudDao;
import com.lianyitech.common.persistence.annotation.MyBatisDao;
import com.lianyitech.modules.report.entity.LibraryAssort;

import java.util.List;

/**
 * 藏书 分类统计
 *
 * @author luzhihuai
 * @version 2016-11-10
 */
@MyBatisDao
public interface LibraryAssortDao extends CrudDao<LibraryAssort> {

    List<LibraryAssort> libraryStatis(LibraryAssort libraryAssort);

    List<LibraryAssort> fiveClassStatis(LibraryAssort libraryAssort);

}