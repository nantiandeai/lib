package com.lianyitech.modules.peri.dao;

import com.lianyitech.common.persistence.CrudDao;
import com.lianyitech.common.persistence.annotation.MyBatisDao;
import com.lianyitech.modules.peri.entity.Binding;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by zcx on 2017/3/14.
 * BindingDao
 */
@MyBatisDao
public interface BindingDao extends CrudDao<Binding> {
    /**
     * 已合订期刊
     * @param binding 合订实体
     * @return 已合订期刊列表
     */
    List<Binding> findBinding(Binding binding);

    /**
     * 查询过刊是否存在流通记录
     * @param map map
     * @return list
     */
    List<Binding> checkBindingBarcode(Map map);

    /**
     * 查询过刊是通过什么方式来的 0登记 1合并
     * @param map map
     * @return list
     */
    List<Binding> checkStatus(Map map);

    /**
     * 根据条码查询过刊信息
     * @param binding 过刊实体
     * @return 合订实体
     */
    Binding getBinding(Binding binding);

    /**
     * 书标打印
     * @param Binding 合订实体
     * @return list
     */
    List<Binding> findAllList(Binding Binding);

    /**
     * 修改馆藏状态
     * @param param
     */
    void updateByBillType(Map<String, Object> param);

    /**
     * 根据条形码判断是否是期刊 true是 false否
     * @param orgId
     * @param barcode
     * @return
     */
    Boolean isPrei(@Param( value= "orgId") String orgId, @Param(value = "barcode") String barcode);

    /**
     * 查询期刊相关信息(流通模块查询书目信息)
     * @param orgId
     * @param barcode
     * @return
     */
    Map<String, Object> findPeriByBarCode(@Param( value= "orgId") String orgId, @Param(value = "barcode") String barcode);

    /**
     * 根据条形码获取复本状态等信息
     * @param orgId
     * @param barcode
     * @return
     */
    Map<String,String> findStatusByBarCode(@Param( value= "orgId") String orgId, @Param(value = "barcode") String barcode);

}
