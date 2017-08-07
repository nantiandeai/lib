package com.lianyitech.modules.peri.dao;

import com.lianyitech.common.persistence.CrudDao;
import com.lianyitech.common.persistence.annotation.MyBatisDao;
import com.lianyitech.modules.peri.entity.Order;

import java.util.List;

/**
 * Created by zcx on 2017/3/10.
 * OrderDao
 */
@MyBatisDao
public interface OrderDao extends CrudDao<Order> {
    /**
     * 订购期数
     * @param order 订单实体
     * @return 订购期数
     */
    int orderPeri(Order order);

    Order orderPeriNum(Order order);

    /**
     * 查询机构下所有的订单（期刊记到列表）
     * @param order 订单实体
     * @return 期刊记到列表
     */
    List<Order> findOrder (Order order);

    /**
     * 查询已记到期刊书目
     * @param order 订单实体
     * @return 已记到期刊书目列表
     */
    List<Order> findArriveDirectory(Order order);
}
