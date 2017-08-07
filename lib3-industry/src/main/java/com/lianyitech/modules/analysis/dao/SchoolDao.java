package com.lianyitech.modules.analysis.dao;

import com.lianyitech.common.persistence.CrudDao;
import com.lianyitech.common.persistence.annotation.MyBatisDao;
import com.lianyitech.modules.analysis.entity.School;

import java.util.List;

/**
 * Created by jordan jiang on 2017/4/7.
 */
@MyBatisDao
public interface SchoolDao  extends CrudDao<School> {

    /**
     * 无录入信息的学校列表
     * @param school 实体类
     * @return list结果
     */
    List<School> listNoInputSchool(School school);
}
