package com.lianyitech.modules.peri.service;

import com.lianyitech.common.service.CrudService;
import com.lianyitech.modules.peri.dao.BindingDetailDao;
import com.lianyitech.modules.peri.entity.Binding;
import com.lianyitech.modules.peri.entity.BindingDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by zcx on 2017/3/21.
 * BindingDetailService
 */
@Service
public class BindingDetailService extends CrudService<BindingDetailDao,BindingDetail> {

    @Autowired
    private BindingDetailDao bindingDetailDao;

    /**
     * 添加binding明细
     * @param ids ids
     * @param binding binding
     */
    void addBindingDetail(String ids, Binding binding) {
        List<BindingDetail> details = new ArrayList<>();
        for (String id : ids.split(",")) {
            BindingDetail detail = new BindingDetail();
            detail.setBindingId(binding.getId());
            detail.setOrderDetailId(id);
            details.add(detail);
        }
        bindingDetailDao.addBindingDetail(details);
    }

    /**
     * 根据bindingId批量删除binding明细
     * @param ids ids
     * @return int
     */
    public int delete(String ids){
        return super.delete(ids);
    }
}
