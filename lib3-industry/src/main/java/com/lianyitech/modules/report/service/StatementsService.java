package com.lianyitech.modules.report.service;

import com.lianyitech.common.service.CrudService;
import com.lianyitech.modules.report.dao.StatementsDao;
import com.lianyitech.modules.report.entity.Statements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;



@Service

public class StatementsService extends CrudService<StatementsDao,Statements>{

    @Autowired
     private   StatementsDao statementsDao;
    //根据条形码分组统计文献
    public List<Statements> findByBarCode(Statements statements){
        return statementsDao.findByBarCode(statements);
    }



    public List<Statements> literature(Statements statements){
        return statementsDao.literature(statements);
    }

    //借阅统计
    public List<Statements> borrowingStatistics(Statements statements){
        return statementsDao.borrowingStatistics(statements);
    }
}
