/**
 * 
 */
package com.lianyitech.modules.report.dao;

;import com.lianyitech.common.persistence.CrudDao;
import com.lianyitech.common.persistence.annotation.MyBatisDao;
import com.lianyitech.modules.report.entity.SchoolBorrowRank;

import java.util.List;

/**
 * 学校借阅排行榜 SchoolBorrowRankDao
 * @author zcx
 * @version 2016-11-4
 */
@MyBatisDao
public interface SchoolBorrowRankDao extends CrudDao<SchoolBorrowRank> {
    /**
     * 学校借阅排行榜查询
     * @param schoolBorrowRank 学校借阅排行榜实体类
     * @return list
     */
    List<SchoolBorrowRank> listSchoolBorrowRank(SchoolBorrowRank schoolBorrowRank);

}