/**
 *
 */
package com.lianyitech.modules.catalog.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lianyitech.modules.catalog.dao.CopyDao;
import com.lianyitech.modules.catalog.entity.Copy;
import com.lianyitech.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lianyitech.common.persistence.Page;
import com.lianyitech.common.service.CrudService;
import com.lianyitech.modules.catalog.entity.Batch;
import com.lianyitech.modules.catalog.dao.BatchDao;

/**
 * 批次管理Service
 *
 * @author zengzy
 * @version 2016-08-26
 */
@Service

public class BatchService extends CrudService<BatchDao, Batch> {
    @Autowired
    private CopyDao copyDao;

    public Batch get(String id) {
        return super.get(id);
    }

    public Page<Batch> findPage(Page<Batch> page, Batch batch) {
        batch.setOrgId(UserUtils.getOrgId());
        if("1".equals(batch.getType())) {
            batch.setPage(page);
            page.setList(dao.findPeriList(batch));
            return page;
        }
        return super.findPage(page, batch);
    }

    /**
     * 判断此批次是否存在 ，ture为存在
     *
     * @param batch 批次号
     * @return true or false
     */
    public Boolean beBatchNo(Batch batch) {
        batch.setOrgId(UserUtils.getOrgId());
        return dao.countBatchNo(batch) > 0 ? true : false;
    }

    @Transactional
    public void save(Batch batch) {
        batch.setOrgId(UserUtils.getOrgId());
        if (batch.getStatus().equals("0")) {//如果点击了默认，则先把以前的默认改咯
            Map<String, Object> map = new HashMap<>();
            map.put("DEL_FLAG_DELETE", "1");
            map.put("DEL_FLAG_NORMAL", "0");
            map.put("orgId",UserUtils.getOrgId());
            map.put("type",batch.getType());
            dao.updateStatus(map);
        }
        super.save(batch);
    }


    /**
     * 批次下是否有复本信息，有则不可删除批次，没有就可以删除批次
     * @param ids 批次id
     * @param type 0书目 1期刊
     * @return 复本信息
     */
    public int checkBatchByCopy(String ids, String type, String orgId) {
        String[] idarr = ids.split(",");
        List idList = Arrays.asList(idarr);
        Map<String, Object> map = new HashMap<>();
        map.put("idList", idList);
        map.put("type", type);
        map.put("orgId", orgId);
        return dao.checkBatchByCopy(map);
    }

    /**
     * 批量删除 多个id逗号隔开
     * @param ids ids
     * @param type 0书目 1期刊
     * @return
     */
    @Transactional
    public int deleteAll(String ids, String type) {
        String[] idarr = ids.split(",");
        List idList = Arrays.asList(idarr);
        Map<String,Object> map = new HashMap<>();
        map.put("idList", idList);
        map.put("type", type);
        return dao.delete(map);
    }

    /**
     * 根据批次号（唯一）查询书目的批次信息
     * @param batch
     * @return Batch
     */
    public Batch getByBatchNo(Batch batch) {
        batch.setOrgId(UserUtils.getOrgId());
        return dao.getByBatchNo(batch);
    }
}