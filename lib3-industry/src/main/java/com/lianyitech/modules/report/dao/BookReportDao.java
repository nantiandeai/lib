/**
 * 
 */
package com.lianyitech.modules.report.dao;

import com.lianyitech.common.persistence.CrudDao;
import com.lianyitech.common.persistence.annotation.MyBatisDao;
import com.lianyitech.modules.report.entity.BookReport;

import java.util.List;

/**
 * 跟书目有关统计DAO接口
 * @author zuo
 * @version 2016-10-28
 */
@MyBatisDao
public interface BookReportDao extends CrudDao<BookReport> {
    /**
     * 藏书统计列表
     * @param bookReport 实体类
     * @return list结果
     */
    List<BookReport> listCollectionStat(BookReport bookReport);
    /**
     * 生均及增量统计查询
     * @param bookReport 实体类
     * @return list结果
     */
    List<BookReport> listStuAddBookNum(BookReport bookReport);

    /**
     *首页学校统计
     */
    int countSchool(BookReport bookReport);

    /**
     *藏书种类
     */
    int bookType(BookReport bookReport);
}