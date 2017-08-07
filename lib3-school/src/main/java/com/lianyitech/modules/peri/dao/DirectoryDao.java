package com.lianyitech.modules.peri.dao;

import com.lianyitech.common.persistence.CrudDao;
import com.lianyitech.common.persistence.annotation.MyBatisDao;
import com.lianyitech.modules.peri.entity.Directory;
import com.lianyitech.modules.report.entity.CirculateStatistics;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/*
 * Created by chenxiaoding on 20170310.
 */
@MyBatisDao
public interface DirectoryDao extends CrudDao<Directory>{

    int getCountByCon(Directory directory);

    /*
    * 根据id修改马克数据
    *
    */
    void updateMarc(Directory entity);

    int findCountByOrder(@Param(value = "periId")String periId, @Param(value = "orgId")String orgId);

    int findCountByBinding(@Param(value = "periId")String periId, @Param(value = "orgId")String orgId);

    List<CirculateStatistics> fiveClassRate(@Param(value = "actionTime") String actionTime, @Param(value = "orgId")String orgId);

    List<CirculateStatistics> bookDistributeRate(@Param(value = "actionTime") String actionTime, @Param(value = "orgId")String orgId, @Param(value = "orderBy")String orderBy);
}
