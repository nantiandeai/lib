package com.lianyitech.modules.report.service;

import com.lianyitech.common.service.CrudService;
import com.lianyitech.core.utils.SystemUtils;
import com.lianyitech.modules.report.dao.CirculateStatisticsDao;
import com.lianyitech.modules.report.entity.CirculateStatistics;
import com.lianyitech.modules.sys.utils.UserUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 *
 * Created by chenxiaoding on 2016/11/4.
 */
@Service

public class CirculateRateService extends CrudService<CirculateStatisticsDao, CirculateStatistics> {

    public Map<String,Object> unionCirculateRate(String startDate,String endDate,String dirType){
        String orgId=UserUtils.getOrgId();
        List<CirculateStatistics> crList;
        CirculateStatistics cr;
        if ("0".equals(dirType)){
         crList = dao.findAllCirculateRate(startDate,endDate,orgId, dirType);
         cr = dao.totalCirculateRate(startDate,endDate,orgId,dirType);}
        else {
             crList = dao.findAllCirculateRatePeir(startDate,endDate,orgId, dirType);
             cr = dao.totalCirculateRatePeir(startDate,endDate,orgId,dirType);
        }
        Map<String,Object> map = new HashMap<>();
        map.put("circulateRateList",crList);
        map.put("totalBookNum",cr.getTotalBookNum());
        map.put("totalBorrowNum",cr.getTotalBorrowNum());
        map.put("totalCirculateRate",cr.getTotalCirculateRate());
        return map;
    }

    public Map<String,Object> bookDistributeRate(String actionTime){
        List<CirculateStatistics> crList = dao.bookDistributeRate(actionTime,UserUtils.getOrgId());
        return SystemUtils.resolveCirculateRate(crList);
    }

    public Map<String,Object> fiveClassRate(String actionTime){
        List<CirculateStatistics> crList = dao.fiveClassRate(actionTime,UserUtils.getOrgId());
        return SystemUtils.resolveCirculateRate(crList);
    }
}