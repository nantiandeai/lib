package com.lianyitech.modules.report.service;

import com.lianyitech.common.persistence.Page;
import com.lianyitech.common.service.CrudService;
import com.lianyitech.modules.report.dao.CollectBookStatisticsDao;
import com.lianyitech.modules.report.entity.CollectBookStatistics;
import com.lianyitech.modules.sys.utils.UserUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by zcx on 2016/11/10.
 * 藏书量分类统计 CollectBookStatisticsService
 */
@Service
@Transactional(readOnly = true)
public class CollectBookStatisticsService extends CrudService<CollectBookStatisticsDao,CollectBookStatistics> {


    public Page<CollectBookStatistics> findPage(Page<CollectBookStatistics> page, CollectBookStatistics collectBookStatistics) {
        collectBookStatistics.setOrgId(UserUtils.getOrgId());
        return super.findPage(page, collectBookStatistics);
    }

    //藏书量分类统计导出专用
    public List<CollectBookStatistics> exportCollectBookByPage(CollectBookStatistics collectBookStatistics){
        collectBookStatistics.setOrgId(UserUtils.getOrgId());
        return dao.findListByPage(collectBookStatistics);
    }
}
