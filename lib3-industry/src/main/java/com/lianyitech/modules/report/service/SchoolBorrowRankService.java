package com.lianyitech.modules.report.service;

import com.lianyitech.common.service.CrudService;
import com.lianyitech.modules.report.dao.SchoolBorrowRankDao;
import com.lianyitech.modules.report.entity.SchoolBorrowRank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 学校借阅排行榜 SchoolBorrowRankService
 * Created by zcx on 2016/11/4
 */
@Service
public class SchoolBorrowRankService extends CrudService<SchoolBorrowRankDao,SchoolBorrowRank> {
    @Autowired
    private SchoolBorrowRankDao schoolBorrowRankDao;

    /**
     * 学校借阅排行榜查询
     * @param schoolBorrowRank 学校借阅排行榜实体类
     * @return list
     */
    public List<SchoolBorrowRank> listSchoolBorrowRank(SchoolBorrowRank schoolBorrowRank){
        return schoolBorrowRankDao.listSchoolBorrowRank(schoolBorrowRank);
    }

}
