package com.lianyitech.modules.peri.service;

import com.lianyitech.common.persistence.Page;
import com.lianyitech.common.service.CrudService;
import com.lianyitech.modules.peri.dao.OrderDao;
import com.lianyitech.modules.peri.dao.OrderDetailDao;
import com.lianyitech.modules.peri.entity.Order;
import com.lianyitech.modules.peri.entity.OrderDetail;
import com.lianyitech.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zcx on 2017/3/10.
 * OrderService
 */
@Service

public class OrderService extends CrudService<OrderDao,Order> {
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private OrderDetailDao orderDetailDao;
    @Autowired
    private OrderDetailService orderDetailService;

    private Map<String,String> PeriFrequency(){
        Map<String,String> map = new HashMap<>();
        map.put("0","365");//日刊
        map.put("1","48");//周刊
        map.put("2","36");//旬刊
        map.put("3","24");//半月刊
        map.put("4","12");//月刊
        map.put("5","6");//双月刊
        map.put("6","4");//季刊
        map.put("7","2");//半年刊
        map.put("8","1");//年刊
        return map;
    }

    /**
     * 订购期数
     * @param order 订单实体
     * @throws Exception Exception
     */
    public int orderPeri(Order order) throws Exception {
        int orderPeri = 0;
        switch (order.getPublishingFre()) {
            case "0":
                order.setPublishingFre(this.PeriFrequency().get("0"));
                orderPeri = orderDao.orderPeri(order);
                break;
            case "1":
                order.setPublishingFre(this.PeriFrequency().get("1"));
                orderPeri = orderDao.orderPeri(order);
                break;
            case "2":
                order.setPublishingFre(this.PeriFrequency().get("2"));
                orderPeri = orderDao.orderPeri(order);
                break;
            case "3":
                order.setPublishingFre(this.PeriFrequency().get("3"));
                orderPeri = orderDao.orderPeri(order);
                break;
            case "4":
                order.setPublishingFre(this.PeriFrequency().get("4"));
                orderPeri = orderDao.orderPeri(order);
                break;
            case "5":
                order.setPublishingFre(this.PeriFrequency().get("5"));
                orderPeri = orderDao.orderPeri(order);
                break;
            case "6":
                order.setPublishingFre(this.PeriFrequency().get("6"));
                orderPeri = orderDao.orderPeri(order);
                break;
            case "7":
                order.setPublishingFre(this.PeriFrequency().get("7"));
                orderPeri = orderDao.orderPeri(order);
                break;
            case "8":
                order.setPublishingFre(this.PeriFrequency().get("8"));
                orderPeri = orderDao.orderPeri(order);
                break;
        }
        return orderPeri;
    }

    public Order orderPeriNum(Order order) throws Exception {
        order.getSqlMap().put("dsf", UserUtils.dataScopeFilter("a"));
        order = orderDao.orderPeriNum(order);
        return order;
    }

    /**
     * 添加
     * @param order 订单实体
     * @throws Exception Exception
     */
    @Transactional
    public void saveOrder(Order order) throws Exception {
        order.preInsert();
        orderDao.insert(order);
        int size = order.getOrderPeri();
        OrderDetail orderDetail = getOrderDetail(order);
        for (int i=1;i<=size;i++){
            orderDetail.preInsert();
            orderDetail.setSeq(i);
            orderDetailDao.insert(orderDetail);
        }
    }

    private OrderDetail getOrderDetail(Order order){
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setOrderId(order.getId());
        orderDetail.setOrderAmount(order.getOrderAmount());
        orderDetail.setArriveAmount(0);
        orderDetail.setAmount(orderDetail.getArriveAmount());
        orderDetail.setIsBound("0");
        orderDetail.setDelFlag("0");
        return orderDetail;
    }

    /**
     * 增刊记到
     * @param orderDetail orderDetail
     */
    public void addRemember(OrderDetail orderDetail) throws Exception {
        orderDetail.preInsert();
        orderDetail.preUpdate();
        orderDetail.setIsBound("0");
        orderDetail.setDelFlag("0");
        orderDetail.setAmount(orderDetail.getArriveAmount());
        orderDetailDao.addRemember(orderDetail);
    }

    /**
     * 修改
     * @param order 订单实体
     * @throws Exception Exception
     */
    @Transactional
    public void updateOrder(Order order) throws Exception {
        order.preUpdate();
        order.setOrgId(UserUtils.getOrgId());
        orderDao.update(order);
        //根据orderId删除
        orderDetailService.delete(order.getId());
        OrderDetail orderDetail = getOrderDetail(order);
        int size = order.getOrderPeri();
        for (int i=0;i<size;i++){
            orderDetail.preInsert();
            orderDetailDao.insert(orderDetail);
        }
    }

    /**
     * 查询
     * @param page   分页对象
     * @param order 订单实体
     * @return page
     */
    public Page<Order> findPage(Page<Order> page,Order order) {
        order.setOrgId(UserUtils.getOrgId());
        return super.findPage(page, order);
    }

    /**
     * 删除
     * @param ids ids
     * @throws Exception Exception
     */
    @Transactional
    public void deleteOrder(String ids) throws Exception {
        //根据orderId删除
        orderDetailService.delete(ids);
        super.delete(ids);
    }

    /**
     * 查询机构下所有的订单（点击期刊记到）
     * @param page   分页对象
     * @param order 订单实体
     * @return page
     */
    public Page<Order> findOrder(Page<Order> page ,Order order){
        order.setOrgId(UserUtils.getOrgId());
        order.setPage(page);
        page.setList(orderDao.findOrder(order));
        return page;
    }


    /**
     * 查询已记到期刊书目
     * @param page  分页对象
     * @param order 订单实体
     * @return page
     */
    public Page<Order> findArriveDirectory(Page<Order> page, Order order){
        order.setOrgId(UserUtils.getOrgId());
        order.setPage(page);
        page.setList(orderDao.findArriveDirectory(order));
        return page;
    }
}
