package com.lianyitech.modules.report.dao;

import com.lianyitech.common.persistence.CrudDao;
import com.lianyitech.common.persistence.annotation.MyBatisDao;
import com.lianyitech.modules.report.entity.GroupRank;

import java.util.List;

/**
 * Created by zcx on 2016/11/9.
 * 读者组织借阅排行 GroupRankDao
 */
@MyBatisDao
public interface GroupRankDao extends CrudDao<GroupRank> {

    List<GroupRank> findList (GroupRank groupRank);//组织借阅排行榜
}
