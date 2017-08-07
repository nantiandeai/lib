package com.lianyitech.modules.catalog.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lianyitech.common.web.ApiController;
import com.lianyitech.common.web.ResponseData;
import com.lianyitech.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import com.lianyitech.common.persistence.Page;
import com.lianyitech.common.utils.StringUtils;
import com.lianyitech.modules.catalog.entity.Batch;
import com.lianyitech.modules.catalog.service.BatchService;

/**
 * 批次管理Controller
 *
 * @author zengzy
 * @version 2016-08-26
 */
@RestController
@RequestMapping(value = "/api/catalog/batch")
public class BatchController extends ApiController {

    @Autowired
    private BatchService batchService;

    @Secured({"ROLE_lib3school.api.catalog.batch.get"})
    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<ResponseData> list(Batch batch, HttpServletRequest request, HttpServletResponse response) {
        Page<Batch> page = batchService.findPage(new Page<Batch>(request, response), batch);
        return new ResponseEntity<>(success(page), HttpStatus.OK);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public ResponseEntity<ResponseData> form(@PathVariable String id) {
        Batch batch = null;
        if (StringUtils.isNotBlank(id)) {
            batch = batchService.get(id);
        }
        if (null == batch) {
            return new ResponseEntity<>(fail("获取失败"), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(success(batch), HttpStatus.OK);
    }

    @Secured({"ROLE_lib3school.api.catalog.batch.post"})
    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<ResponseData> save(Batch batch) {
        //修改的情况：目前期刊订单跟批次号名称直接关联的，修改批次号名的时候要如下验证
        if(batch.getId()!=null) {
            Batch b = batchService.get(batch.getId());
            int count = 0;
            if (!b.getBatchNo().equals(batch.getBatchNo())) {
                 count = batchService.checkBatchByCopy(batch.getId(), batch.getType(), UserUtils.getOrgId());
            }
            if (count > 0) {
                String msg = java.util.Objects.equals("0", batch.getType())?"复本":"期刊";
                return new ResponseEntity<>(fail("修改失败,该批次下有"+msg+"信息不能修改批次号名"), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        if (StringUtils.isEmpty(batch.getBatchNo())) {
            return new ResponseEntity<>(fail("请输入批次号"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (StringUtils.isEmpty(batch.getStatus())) {
            return new ResponseEntity<>(fail("请选择状态"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (batchService.beBatchNo(batch)) {
            return new ResponseEntity<>(fail("此批次号己存在"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        try {
            batchService.save(batch);
            return new ResponseEntity<>(success("保存成功"), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("操作失败",e);
            return new ResponseEntity<>(fail("保存失败"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /**
     * 批次删除（批量删除），批次下是否有复本信息，有则不可删除批次，没有就可以删除批次
     *
     * @param id 批次id
     * @return coed
     */
    @Secured({"ROLE_lib3school.api.catalog.batch.id.get"})
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public ResponseEntity<ResponseData> delete(@PathVariable String id, String type) {
        String orgId = UserUtils.getOrgId();
        int count = batchService.checkBatchByCopy(id, type, orgId);
        if (count > 0 && java.util.Objects.equals("0", type)) {
            return new ResponseEntity<>(fail("删除失败,该批次下有复本信息"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (count > 0 && java.util.Objects.equals("1", type)) {
            return new ResponseEntity<>(fail("删除失败,该批次下有订单信息"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        batchService.deleteAll(id, type);
        return new ResponseEntity<>(success("删除成功"), HttpStatus.OK);
    }

}