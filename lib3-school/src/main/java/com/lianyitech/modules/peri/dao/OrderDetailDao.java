package com.lianyitech.modules.peri.dao;

import com.lianyitech.common.persistence.CrudDao;
import com.lianyitech.common.persistence.annotation.MyBatisDao;
import com.lianyitech.modules.peri.entity.OrderDetail;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by zcx on 2017/3/13.
 * OrderDetailDao
 */
@MyBatisDao
public interface OrderDetailDao extends CrudDao<OrderDetail> {
    /**
     * 根据书目id查询已记到期刊
     * @param orderDetail 订单明细实体
     * @return 已记到期刊列表
     */
    List<OrderDetail> findArriveOrderDetail(OrderDetail orderDetail);

    /**
     * 期刊合订
     * @param map map
     */
    void bindingPeri(Map map);

    /**
     * 获得需要取消合订的信息
     * @param map 取消的id和数量
     * @return list
     */
    List<Map<String, String>> getBindingPeri(Map map);

    /**
     * 取消期刊合订
     * @param map 取消合订的相关参数
     */
    void removeBindingPeri(@Param("list")List<Map<String, String>> map,@Param("updateDate") Date updateDate);

    /**
     * 判断是否能删除订单信息
     * @param orderDetail 订单明细实体
     * @return 列表
     */
    List<OrderDetail> checkDelete(OrderDetail orderDetail);

    /**
     * 增刊记到
     * @param orderDetail 订单明细实体
     * @return 新增数量
     */
    int addRemember(OrderDetail orderDetail);

    /**
     * 批量删除
     * @param map map
     * @return count
     */
    int deleteDetail(Map map);

}
