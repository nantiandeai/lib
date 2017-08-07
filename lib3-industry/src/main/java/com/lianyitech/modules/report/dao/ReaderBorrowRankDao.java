/**
 * 
 */
package com.lianyitech.modules.report.dao;


import com.lianyitech.common.persistence.CrudDao;
import com.lianyitech.common.persistence.annotation.MyBatisDao;
import com.lianyitech.modules.report.entity.ReaderBorrowRank;

import java.util.List;

/**
 * 读者借阅排行榜 ReaderBorrowRankDao
 * @author zcx
 * @version 2016-11-2
 */
@MyBatisDao
public interface ReaderBorrowRankDao extends CrudDao<ReaderBorrowRank> {

    /**
     * 查询所有的读者借阅排行榜
     * @param readerBorrowRank 读者借阅排行榜实体
     * @return list
     */
    public List<ReaderBorrowRank> listReaderBorrowRank(ReaderBorrowRank readerBorrowRank);

}