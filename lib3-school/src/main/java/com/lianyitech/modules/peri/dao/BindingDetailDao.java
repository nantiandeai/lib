package com.lianyitech.modules.peri.dao;

import com.lianyitech.common.persistence.CrudDao;
import com.lianyitech.common.persistence.annotation.MyBatisDao;
import com.lianyitech.modules.peri.entity.BindingDetail;

import java.util.List;

/**
 * Created by zcx on 2017/3/21.
 * BindingDetailDao
 */
@MyBatisDao
public interface BindingDetailDao extends CrudDao<BindingDetail> {
    /**
     * 添加binding明细
     * @param map map
     */
    void addBindingDetail(List<BindingDetail> map);

}
