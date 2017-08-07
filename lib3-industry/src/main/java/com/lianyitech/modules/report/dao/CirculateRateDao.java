package com.lianyitech.modules.report.dao;

import com.lianyitech.common.persistence.CrudDao;
import com.lianyitech.common.persistence.annotation.MyBatisDao;
import com.lianyitech.modules.report.entity.CirculateRate;

import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/*
 * Created by chenxiaoding on 2016/11/4.
 */
@MyBatisDao
public interface CirculateRateDao extends CrudDao<CirculateRate>{


    List<CirculateRate> findAllCirculateRate(@Param(value = "startDate") String startDate, @Param(value = "endDate") String endDate);

}
