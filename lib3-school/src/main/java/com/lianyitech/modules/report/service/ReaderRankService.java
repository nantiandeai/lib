package com.lianyitech.modules.report.service;

import com.lianyitech.common.persistence.Page;
import com.lianyitech.common.service.CrudService;
import com.lianyitech.modules.report.dao.ReaderRankDao;
import com.lianyitech.modules.report.entity.ReaderRank;
import com.lianyitech.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by zcx on 2016/11/9.
 * 读者借阅排行service
 */
@Service

public class ReaderRankService extends CrudService<ReaderRankDao,ReaderRank> {

    @Autowired
    private ReaderRankDao readerRankDao;

    public Page<ReaderRank> findPage(Page<ReaderRank> page, ReaderRank readerRank) {
        String orgId = UserUtils.getOrgId();
        assert orgId != null;
        readerRank.setOrgId(orgId);
        return super.findPage(page, readerRank);
    }

    //读者借阅排行榜
    public List<ReaderRank> readerRanking (ReaderRank readerRank){
        String orgId = UserUtils.getOrgId();
        assert orgId != null;
        readerRank.setOrgId(orgId);
        return readerRankDao.findList(readerRank);
    }
}
