package com.lianyitech.modules.report.dao;

import com.lianyitech.common.persistence.CrudDao;
import com.lianyitech.common.persistence.annotation.MyBatisDao;
import com.lianyitech.modules.report.entity.CollectBookStatistics;

import java.util.List;

/**
 * Created by zcx on 2016/11/10.
 * 藏书量分类统计 CollectBookStatisticsDao
 */
@MyBatisDao
public interface CollectBookStatisticsDao extends CrudDao<CollectBookStatistics> {
    //藏书量分类统计
    List<CollectBookStatistics> findList(CollectBookStatistics collectBookStatistics);

    //藏书量分类统计专用导出
    List<CollectBookStatistics> findListByPage(CollectBookStatistics collectBookStatistics);
}
