package com.lianyitech.modules.report.service;

import com.lianyitech.modules.common.ReportCommon;
import com.lianyitech.modules.report.dao.QuotaDao;
import com.lianyitech.modules.report.entity.Quota;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jordan jiang on 2017/3/23.
 */
@Service

public class QuotaService{

    @Autowired
    private QuotaDao quotaDao;

    public Map<String, Object> getQuota(){
        Map<String, Object> result = new HashMap<>();
        Quota quotaParm = new Quota();
        Quota quota = quotaDao.getBooksInfo(quotaParm);

        quota.setBorrowerNum(quotaDao.getBorrowerNum(quotaParm));
        quota.setStudentNum(quotaDao.getStudentNum(quotaParm));
        quota.setStuBorrowNum(quotaDao.getStuBorrowNum(quotaParm));
        quota.setStuAverNum(ReportCommon.comdivision(quota.getBookNum(),quota.getStudentNum()));
        quota.setStuBorrowAverNum(ReportCommon.comdivision(quota.getStuBorrowNum(),quota.getStudentNum()));

        result.put("total", quota);
        return result;
    }
}
