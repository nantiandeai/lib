package com.lianyitech.modules.report.service;

import com.lianyitech.common.service.CrudService;
import com.lianyitech.modules.report.dao.ReaderBorrowRankDao;
import com.lianyitech.modules.report.entity.ReaderBorrowRank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 读者借阅排行榜 ReaderBorrowRankService
 * Created by zcx on 2016/11/2.
 */
@Service
public class ReaderBorrowRankService extends CrudService<ReaderBorrowRankDao,ReaderBorrowRank> {

    @Autowired
    private ReaderBorrowRankDao readerBorrowRankDao;

    /**
     * 查询所有的读者借阅排行榜
     * @param readerBorrowRank 读者借阅排行榜实体
     * @return list
     */
    public List<ReaderBorrowRank> listReaderBorrowRank(ReaderBorrowRank readerBorrowRank){
        List list = readerBorrowRankDao.listReaderBorrowRank(readerBorrowRank);
        //这里要对list稍微处理下超过50条限制在50条内。。如果某个读者按照不同时间统计，2个时间的的读者属性（名称、组织等）不同，则可能会出现多条
        if(list.size()>50){
            list = list.subList(0,50);
        }
        return list;
    }

}
