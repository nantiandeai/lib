/**
 * 
 */
package com.lianyitech.modules.lib.dao;

import com.lianyitech.common.persistence.CrudDao;
import com.lianyitech.common.persistence.annotation.MyBatisDao;
import com.lianyitech.modules.lib.entity.NoticeManage;

/**
 * 公共通知DAO接口
 * @author zengzy
 * @version 2016-09-09
 */
@MyBatisDao
public interface NoticeManageDao extends CrudDao<NoticeManage> {

     NoticeManage findIsTop();
	
}