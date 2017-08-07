package com.lianyitech.modules.circulate.service;

import com.lianyitech.common.service.CrudService;
import com.lianyitech.modules.circulate.dao.BillDetailDao;
import com.lianyitech.modules.circulate.entity.BillDetail;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 操作单据管理Service
 *
 * @author zengzy
 * @version 2016-09-09
 */
@Service
class BillDetailService extends CrudService<BillDetailDao, BillDetail> {

    /**
     * 创建 单据明细
     * @param param 输入参数 barcode billId
     */
    public void create(Map<String, String> param) {
        dao.create(param);
    }
}