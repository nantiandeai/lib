/**
 *
 */
package com.lianyitech.modules.report.dao;

import com.lianyitech.common.persistence.CrudDao;
import com.lianyitech.common.persistence.annotation.MyBatisDao;

import com.lianyitech.modules.report.entity.Area;
import org.apache.ibatis.annotations.Param;


import java.util.List;

/**
 * 区域DAO接口
 *
 * @author zengzy
 * @version 2016-10-10
 */
@MyBatisDao
public interface AreaDao extends CrudDao<Area> {


    /**
     * 区域列表-根据类型，父级编号查询子区域
     * @return
     */
    public List<Area> findByParentCode(Area area);

    /**
     * 根据编号查询区域类型
     *
     * @param code
     * @return
     */
    public String findTypeByCode(String code);

    /**
     * 根据编号查询对应的信息
     */
    public Area findByCode(String code);
}