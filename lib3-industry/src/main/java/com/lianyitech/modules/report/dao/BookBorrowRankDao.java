package com.lianyitech.modules.report.dao;


import com.lianyitech.common.persistence.CrudDao;
import com.lianyitech.common.persistence.annotation.MyBatisDao;
import com.lianyitech.modules.report.entity.BookBorrowRank;

import java.util.List;
/**
 * 图书借阅排行榜 BookBorrowRankDao
 * Created by zcx on 2016/11/3.
 */
@MyBatisDao
public interface BookBorrowRankDao extends CrudDao<BookBorrowRank> {

    /**
     * 查询所有的图书借阅排行榜
     * @param bookBorrowRank 图书借阅排行榜实体
     * @return list
     */
    List<BookBorrowRank> listBookBorrowRank(BookBorrowRank bookBorrowRank);

}
