/**
 * 
 */
package com.lianyitech.modules.report.dao;

import com.lianyitech.common.persistence.CrudDao;
import com.lianyitech.common.persistence.annotation.MyBatisDao;
import com.lianyitech.modules.report.entity.Statements;

import java.util.List;

/**
 * 区域DAO接口
 *
 * @author zengzy
 * @version 2016-10-10
 */
@MyBatisDao
public interface StatementsDao extends CrudDao<Statements> {


    /**
     * 区域列表-根据类型，父级编号查询子区域
     *
     */
     List<Statements> findByBarCode(Statements statements);

    List<Statements> literature(Statements statements);

    List<Statements> borrowingStatistics(Statements statements);
}