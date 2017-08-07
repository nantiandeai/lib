package com.lianyitech.modules.report.service;

import com.lianyitech.common.persistence.Page;
import com.lianyitech.common.service.CrudService;
import com.lianyitech.modules.report.dao.GroupRankDao;
import com.lianyitech.modules.report.entity.GroupRank;
import com.lianyitech.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by zcx on 2016/11/8.
 * 组织借阅排行service
 */
@Service

public class GroupRankService extends CrudService<GroupRankDao,GroupRank> {

    @Autowired
    private GroupRankDao groupRankDao;

    public Page<GroupRank> findPage(Page<GroupRank> page, GroupRank groupRank) {
        String orgId = UserUtils.getOrgId();
        assert orgId != null;
        groupRank.setOrgId(orgId);
        return super.findPage(page, groupRank);
    }

    //组织借阅排行榜
    public List<GroupRank> groupRanking(GroupRank groupRank){
        String orgId = UserUtils.getOrgId();
        assert orgId != null;
        groupRank.setOrgId(orgId);
        return groupRankDao.findList(groupRank);
    }
}
