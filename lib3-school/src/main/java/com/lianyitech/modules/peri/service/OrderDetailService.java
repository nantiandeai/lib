package com.lianyitech.modules.peri.service;

import com.lianyitech.common.persistence.Page;
import com.lianyitech.common.service.CrudService;
import com.lianyitech.modules.peri.dao.OrderDetailDao;
import com.lianyitech.modules.peri.entity.OrderDetail;
import com.lianyitech.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by zcx on 2017/3/15.
 * OrderDetailService
 */
@Service
public class OrderDetailService extends CrudService<OrderDetailDao,OrderDetail> {
    @Autowired
    private OrderDetailDao orderDetailDao;

    /**
     * 根据书目id查询已记到期刊
     * @param page 分页对象
     * @param orderDetail 订单明细实体
     * @return page
     */
    public Page<OrderDetail> findArriveOrderDetail(Page<OrderDetail> page,OrderDetail orderDetail){
        orderDetail.setOrgId(UserUtils.getOrgId());
        orderDetail.setPage(page);
        page.setList(orderDetailDao.findArriveOrderDetail(orderDetail));
        return page;
    }

    /**
     * 判断是否能删除订单信息
     * @param orderDetail 订单明细实体
     * @return 列表
     */
    public List<OrderDetail> checkDelete(OrderDetail orderDetail){
        return orderDetailDao.checkDelete(orderDetail);
    }

    /**
     * 订单明细删除（批量）
     * @param ids ids
     * @return code
     */
    public int delete(String ids) {
        return super.delete(ids);
    }

    /**
     * 期刊合订
     * @param ids ids
     */
    void bindingPeri(String ids){
        String[] idarr = ids.split(",");
        List idList = Arrays.asList(idarr);
        Map<String,Object> map = new HashMap<>();
        map.put("idList", idList);
        map.put("updateDate",new Date());
        orderDetailDao.bindingPeri(map);
    }

    /**
     * 取消期刊合订
     * @param ids ids
     */
    void removeBindingPeri(String ids){
        String[] idarr = ids.split(",");
        List idList = Arrays.asList(idarr);
        Map<String,Object> map = new HashMap<>();
        map.put("idList", idList);
        List<Map<String, String>> list = orderDetailDao.getBindingPeri(map);
        orderDetailDao.removeBindingPeri(list,new Date());
    }

    public int deleteDetail(String ids) {
        String[] idarr = ids.split(",");
        List idList = Arrays.asList(idarr);
        Map<String,Object> map = new HashMap<>();
        map.put("idList", idList);
        map.put("DEL_FLAG_DELETE", "1");
        map.put("updateDate",new Date());
        return dao.deleteDetail(map);
    }
}
