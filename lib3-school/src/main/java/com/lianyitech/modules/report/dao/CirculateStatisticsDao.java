package com.lianyitech.modules.report.dao;

import com.lianyitech.common.persistence.CrudDao;
import com.lianyitech.common.persistence.annotation.MyBatisDao;
import com.lianyitech.modules.report.entity.CirculateStatistics;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/*
 * Created by chenxiaoding on 2016/11/4.
 */
@MyBatisDao
public interface CirculateStatisticsDao extends CrudDao<CirculateStatistics>{

    List<CirculateStatistics> findAllCirculateRate(@Param(value = "startDate") String startDate, @Param(value = "endDate") String endDate, @Param(value = "orgId")String orgId,@Param(value = "dirType")String dirType);

    List<CirculateStatistics> findAllCirculateRatePeir(@Param(value = "startDate") String startDate, @Param(value = "endDate") String endDate, @Param(value = "orgId")String orgId,@Param(value = "dirType")String dirType);

    /**
     * 合计数据
     * @param startDate 开始
     * @param endDate 结束时间
     * @return
     */
    CirculateStatistics totalCirculateRate(@Param(value = "startDate") String startDate, @Param(value = "endDate") String endDate, @Param(value = "orgId")String orgId,@Param(value = "dirType")String dirType);
    CirculateStatistics totalCirculateRatePeir(@Param(value = "startDate") String startDate, @Param(value = "endDate") String endDate, @Param(value = "orgId")String orgId,@Param(value = "dirType")String dirType);


    List<CirculateStatistics> bookDistributeRate(@Param(value = "actionTime") String actionTime, @Param(value = "orgId")String orgId);

    List<CirculateStatistics> fiveClassRate(@Param(value = "actionTime") String actionTime, @Param(value = "orgId")String orgId);
}
