package com.lianyitech.modules.report.service;

import com.lianyitech.common.service.CrudService;
import com.lianyitech.modules.report.dao.BookBorrowRankDao;
import com.lianyitech.modules.report.entity.BookBorrowRank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 图书借阅排行榜 BookBorrowRankService
 * Created by zcx on 2016/11/3.
 */
@Service
public class BookBorrowRankService extends CrudService<BookBorrowRankDao,BookBorrowRank> {

    @Autowired
    private BookBorrowRankDao bookBorrowRankDao;

    /**
     * 查询所有的图书借阅排行榜
     * @param bookBorrowRank 图书借阅排行榜实体
     * @return list
     */
    public List<BookBorrowRank> listBookBorrowRank (BookBorrowRank bookBorrowRank){
        List<BookBorrowRank> list = bookBorrowRankDao.listBookBorrowRank(bookBorrowRank);
        //这里要对list稍微处理下超过50条限制在50条内。。如果某个书目按照不同时间统计，2个时间的的书目属性（题名、著者、出版社等）不同，则可能会出现多条
        if(list.size()>50){
            list = list.subList(0,50);
        }
        return list;
    }
}
