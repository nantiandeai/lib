package com.lianyitech.modules.catalog.web;

import com.lianyitech.common.persistence.Page;
import com.lianyitech.common.utils.StringUtils;
import com.lianyitech.common.web.ApiController;
import com.lianyitech.common.web.ResponseData;
import com.lianyitech.modules.catalog.dao.CopyDao;
import com.lianyitech.modules.catalog.entity.Copy;
import com.lianyitech.modules.catalog.entity.SupplyLable;
import com.lianyitech.modules.catalog.service.SupplyLableService;
import com.lianyitech.modules.peri.dao.BindingDao;
import com.lianyitech.modules.peri.entity.Binding;
import com.lianyitech.modules.sys.utils.UserUtils;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by zcx on 2017/5/22.
 * SupplyLableController
 */
@Controller
@RequestMapping(value = "/api/catalog/supplyLable")
public class SupplyLableController extends ApiController {
    @Autowired
    private SupplyLableService supplyLableService;
    @Autowired
    private CopyDao copyDao;
    @Autowired
    private BindingDao bindingDao;

    /**
     * 提取复本书标（查缺打印）
     * @param copy 复本实体
     * @param request request
     * @param response response
     * @return page
     */
    @GetMapping("/list/copy/supply/lable")
    @ApiOperation(value="提取复本书标（查缺打印）", notes = "根据书目条码查询查缺打印记录", response = ResponseEntity.class)
    public ResponseEntity<ResponseData> listCopySupplyLable(Copy copy, HttpServletRequest request, HttpServletResponse response) throws Exception {
        copy.setOrgId(UserUtils.getOrgId());
        if(null!=copy.getBarcode()&& StringUtils.isNotEmpty(copy.getBarcode())&&null==copyDao.getCopyByBarcode(copy)){
            return new ResponseEntity<>(fail("该条码不存在"), HttpStatus.OK);
        }
        Page<SupplyLable> page = supplyLableService.listCopySupplyLable(new Page<>(request, response), copy);
        return new ResponseEntity<>(success(page), HttpStatus.OK);
    }

    @GetMapping("/list/supply/lable/export/excel")
    public ResponseEntity<ResponseData> exportCopySupplyLableExcel(SupplyLable supplyLable, HttpServletRequest request, HttpServletResponse response) throws Exception {
        supplyLable.setOrgId(UserUtils.getOrgId());
        supplyLableService.exportSupplyLableExcel(supplyLable, request, response);
        return new ResponseEntity<>(success("导出成功"),HttpStatus.OK);
    }

    @GetMapping("/list/supply/lable/export/txt")
    public ResponseEntity<ResponseData> exportCopySupplyLableTxt(SupplyLable supplyLable, HttpServletRequest request, HttpServletResponse response) throws Exception {
        supplyLable.setOrgId(UserUtils.getOrgId());
        supplyLableService.exportSupplyLableTxt(supplyLable, request, response);
        return new ResponseEntity<>(success("导出成功"),HttpStatus.OK);
    }

    /**
     * 提取期刊书标（查缺打印）
     * @param binding 过刊实体
     * @param request request
     * @param response response
     * @return page
     */
    @GetMapping("/list/binding/supply/lable")
    @ApiOperation(value="提取期刊书标（查缺打印）", notes = "根据期刊条码查询查缺打印记录", response = ResponseEntity.class)
    public ResponseEntity<ResponseData> listBindingSupplyLable(Binding binding, HttpServletRequest request, HttpServletResponse response) throws Exception {
        binding.setOrgId(UserUtils.getOrgId());
        if (null!=binding.getBarcode()&& StringUtils.isNotEmpty(binding.getBarcode())&&null==bindingDao.getBinding(binding)){
            return new ResponseEntity<>(fail("该条码不存在"), HttpStatus.OK);
        }
        Page<SupplyLable> page = supplyLableService.listBindingSupplyLable(new Page<>(request, response), binding);
        return new ResponseEntity<>(success(page), HttpStatus.OK);
    }

    /**
     * （查缺打印）删除
     * @param ids 要删除的id
     * @return code
     */
    @DeleteMapping("")
    @ApiOperation(value="删除查缺打印记录", notes = "根据查缺打印记录id删除查缺打印记录", response = ResponseEntity.class)
    public ResponseEntity<ResponseData> delete( @ApiParam(required =true, name ="过刊ids", value="过刊的ids")
                                                            String ids) {
        supplyLableService.delete(ids);
        return new ResponseEntity<>(success(), HttpStatus.OK);
    }

    /**
     * （查缺打印）清空列表
     * @return code
     * @throws Exception Exception
     */
    @DeleteMapping("/delete")
    @ApiOperation(value="清空列表", notes = "根据机构id清空当天的列表", response = ResponseEntity.class)
    public ResponseEntity<ResponseData> deleteAll(SupplyLable supplyLable) throws Exception {
        supplyLable.setOrgId(UserUtils.getOrgId());
        supplyLableService.deleteAll(supplyLable);
        return new ResponseEntity<>(success(), HttpStatus.OK);
    }


}
