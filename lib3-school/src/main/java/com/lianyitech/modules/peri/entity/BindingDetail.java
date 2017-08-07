package com.lianyitech.modules.peri.entity;

import com.lianyitech.core.utils.DataEntity;

/**
 * Created by zcx on 2017/3/9.
 * 期刊合订明细表
 */
public class BindingDetail extends DataEntity<BindingDetail> {
    private static final long serialVersionUID = 1L;
    /**
     * 订购明细ID
     */
    private String orderDetailId;
    /**
     * 过刊装订ID
     */
    private String bindingId;

    public String getOrderDetailId() {
        return orderDetailId;
    }

    public void setOrderDetailId(String orderDetailId) {
        this.orderDetailId = orderDetailId;
    }

    public String getBindingId() {
        return bindingId;
    }

    public void setBindingId(String bindingId) {
        this.bindingId = bindingId;
    }
}

