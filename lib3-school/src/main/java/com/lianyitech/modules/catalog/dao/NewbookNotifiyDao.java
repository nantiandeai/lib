/**
 * 
 */
package com.lianyitech.modules.catalog.dao;

import com.lianyitech.common.persistence.CrudDao;
import com.lianyitech.common.persistence.annotation.MyBatisDao;
import com.lianyitech.modules.catalog.entity.Copy;
import com.lianyitech.modules.catalog.entity.NewbookNotifiy;
import com.lianyitech.modules.catalog.entity.NewbookNotifiyDetail;

import java.util.List;

/**
 * 新书通报管理DAO接口
 * @author zengzy
 * @version 2016-08-26
 */
@MyBatisDao
public interface NewbookNotifiyDao extends CrudDao<NewbookNotifiy> {

    List<NewbookNotifiy> findNewbookList(NewbookNotifiy newbookNotifiy);

    String getNewBookId(Copy copy);

    int updateStatus(NewbookNotifiy nn);

    void insertNewbook(NewbookNotifiyDetail newbookNotifiyDetail);

    int countByName (NewbookNotifiy newbookNotifiy);
	
}