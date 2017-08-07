package com.lianyitech.modules.report.dao;

import com.lianyitech.common.persistence.CrudDao;
import com.lianyitech.common.persistence.annotation.MyBatisDao;
import com.lianyitech.modules.report.entity.LibraryAssort;
import com.lianyitech.modules.report.entity.Quota;

import java.util.List;

/**
 * 指标DAO接口
 *
 * @author jordan jiang
 * @version 2017-3-23
 */
@MyBatisDao
public interface QuotaDao{

    Quota getBooksInfo(Quota quota);

    Integer getStudentNum(Quota quota);

    Integer getBorrowerNum(Quota quota);

    Integer getStuBorrowNum(Quota quota);
}