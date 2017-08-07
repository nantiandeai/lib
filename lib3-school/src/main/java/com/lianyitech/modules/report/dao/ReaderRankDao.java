package com.lianyitech.modules.report.dao;

import com.lianyitech.common.persistence.CrudDao;
import com.lianyitech.common.persistence.annotation.MyBatisDao;
import com.lianyitech.modules.report.entity.ReaderRank;

import java.util.List;

/**
 * Created by zcx on 2016/11/8.
 * 读者组织借阅排行 ReaderRankDao
 */
@MyBatisDao
public interface ReaderRankDao extends CrudDao<ReaderRank> {

    List<ReaderRank> findList (ReaderRank readerRank);//读者借阅排行
}
