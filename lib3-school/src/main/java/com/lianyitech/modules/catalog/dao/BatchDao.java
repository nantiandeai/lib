/**
 * 
 */
package com.lianyitech.modules.catalog.dao;

import com.lianyitech.common.persistence.CrudDao;
import com.lianyitech.common.persistence.annotation.MyBatisDao;
import com.lianyitech.modules.catalog.entity.Batch;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 批次管理DAO接口
 * @author zengzy
 * @version 2016-08-26
 */
@MyBatisDao
public interface BatchDao extends CrudDao<Batch> {



     int updateStatus(Map map);

    /**
     * 查批次号存在不存在
     * @param batch batch
     * @return code
     */
     int countBatchNo(Batch batch);

    /**
     * 判断批次下是否有复本信息
     * @param map 批次id集合
     * @return 复本信息
     */
     int checkBatchByCopy(Map map);

    /**
     * 查询期刊批次的列表
     * @param batch batch
     * @return list
     */
     List<Batch>  findPeriList(Batch batch);

    /**
     * 根据批次号（唯一）查询书目的批次信息
     * @param batch
     * @return Batch
     */
    Batch getByBatchNo(Batch batch);

    int insertBatch(@Param("orgId")String orgId, @Param("tableName")String tableName);
}