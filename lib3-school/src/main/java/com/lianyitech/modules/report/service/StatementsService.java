package com.lianyitech.modules.report.service;


import com.lianyitech.common.persistence.Page;
import com.lianyitech.common.service.CrudService;
import com.lianyitech.modules.report.dao.StatementsDao;
import com.lianyitech.modules.report.entity.Statements;
import com.lianyitech.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service

public class StatementsService extends CrudService<StatementsDao,Statements> {

    @Autowired
    private StatementsDao statementsDao;
    //统计文献
    public Page<Statements> documentCirculate(Page<Statements> page, Statements statements){
        String orgId = UserUtils.getOrgId();
        assert orgId != null;
        statements.setOrgId(orgId);
        statements.setPage(page);
        page.setList(statementsDao.documentCirculate(statements));
        return page;
    }
    //统计文献 导出
    public List<Statements> exportDocumentCirculate(Statements statements) {
        String orgId = UserUtils.getOrgId();
        assert orgId != null;
        statements.setOrgId(orgId);
        return statementsDao.documentCirculate(statements);
    }


    //文献借阅排行
    public Page<Statements> literature(Page<Statements> page,Statements statements){
        String orgId = UserUtils.getOrgId();
        assert orgId != null;
        statements.setOrgId(orgId);
        statements.setPage(page);
        page.setList(statementsDao.literature(statements));
        return page;
    }
    //文献借阅排行导出
    public List<Statements> exportLiterature(Statements statements) {
        String orgId = UserUtils.getOrgId();
        assert orgId != null;
        statements.setOrgId(orgId);
        return statementsDao.literature(statements);
    }

    //借阅统计
    public Page<Statements> borrowingStatistics(Page<Statements> page,Statements statements){
        String orgId = UserUtils.getOrgId();
        assert orgId != null;
        statements.setOrgId(orgId);
        statements.setPage(page);
        page.setList(statementsDao.borrowingStatistics(statements));
        return page;
    }
    //借阅统计导出
    public List<Statements> exportBorrowingStatistics(Statements statements) {
        String orgId = UserUtils.getOrgId();
        assert orgId != null;
        statements.setOrgId(orgId);
        return statementsDao.borrowingStatistics(statements);
    }

    public Page<Statements> groupStatistics(Page<Statements> page, Statements statements) {
        String orgId = UserUtils.getOrgId();
        assert orgId != null;
        statements.setOrgId(orgId);
        statements.setPage(page);
        page.setList(statementsDao.groupStatistics(statements));
        return page;
    }

    public List<Statements> exportGroupStatistics(Statements statements) {
        String orgId = UserUtils.getOrgId();
        assert orgId != null;
        statements.setOrgId(orgId);
        return statementsDao.groupStatistics(statements);
    }



}
