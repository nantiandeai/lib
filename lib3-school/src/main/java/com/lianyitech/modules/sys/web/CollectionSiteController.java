package com.lianyitech.modules.sys.web;

import com.lianyitech.common.persistence.Page;
import com.lianyitech.common.utils.StringUtils;
import com.lianyitech.common.web.ApiController;
import com.lianyitech.common.web.ResponseData;
import com.lianyitech.core.enmu.EnumLibStoreStatus;
import com.lianyitech.modules.catalog.entity.Copy;
import com.lianyitech.modules.catalog.service.CopyService;
import com.lianyitech.modules.sys.entity.CollectionSite;
import com.lianyitech.modules.sys.service.CollectionSiteService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 馆藏地管理Controller
 *
 * @author zengzy
 * @version 2016-09-02
 */
@RestController
@RequestMapping(value = "/api/sys/collectionsite")
public class CollectionSiteController extends ApiController {

    @Autowired
    private CollectionSiteService collectionSiteService;

    @Autowired
    private CopyService copyService;

    /**
     * @param collectionSite //馆藏地的实体
     * @param request        //请求结果
     * @param response       //请求
     * @return //ResponseEntity
     */

    @Secured({"ROLE_lib3school.api.sys.collectionsite.get"})
    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<ResponseData> list(CollectionSite collectionSite, HttpServletRequest request, HttpServletResponse response) {
        Page<CollectionSite> page = collectionSiteService.findPage(new Page<>(request, response), collectionSite);
        if (page != null) {
            return new ResponseEntity<>(success(page), HttpStatus.OK);
        }
        return new ResponseEntity<>(success("获取列表失败"), HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @Secured({"ROLE_lib3school.api.sys.collectionsite.post"})
    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<ResponseData> save(CollectionSite collectionSite) {
        if (null == collectionSite.getId() || collectionSite.getId().equals("") || !collectionSite.getName().equals(collectionSiteService.get(collectionSite.getId()).getName())) {
            int count = collectionSiteService.getName(collectionSite);
            if (count > 0) {
                return new ResponseEntity<>(fail("馆藏地已存在"), HttpStatus.BAD_REQUEST);
            }
        }
        try {
            //判断复本是否被借出
            Copy copy = new Copy();
            copy.setCollectionSiteId(collectionSite.getId());
            copy.setStatus(EnumLibStoreStatus.OUT_LIB.getValue());//借出
            if (collectionSite.getId()!=null && !"".equals(collectionSite.getId()) && collectionSite.getStockAttr().equals("0") && copyService.getCount(copy)>0){
                return new ResponseEntity<>(fail("操作失败,馆藏地中有图书未还，暂不能修改库存属性！"), HttpStatus.INTERNAL_SERVER_ERROR);
            }else{
                collectionSiteService.save(collectionSite);
                return new ResponseEntity<>(success("操作成功"), HttpStatus.OK);
            }
        } catch (Exception e) {
            logger.error("操作失败",e);
            return new ResponseEntity<>(fail("操作失败"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Secured({"ROLE_lib3school.api.sys.collectionsite.delete"})
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<ResponseData> delete(@PathVariable String id) {
        Map result = collectionSiteService.deleteCopy(id);
        return new ResponseEntity<>(success(result), HttpStatus.OK);
    }

    /**
     * 批量修改
     *
     * @param collectionSiteList //馆藏地的集合
     * @return // 操作结果
     */
    @Secured({"ROLE_lib3school.api.sys.collectionsite.updatecollectionsite.post"})
    @RequestMapping(value = "/updatecollectionsite", method = RequestMethod.POST)
    public ResponseEntity<ResponseData> updatecollectionsite(CollectionSite collectionSite) {
        try {
            String msg = collectionSiteService.updatecollectionsite(collectionSite);
            if (StringUtils.isNotBlank(msg)) {
                return new ResponseEntity<>(fail(msg), HttpStatus.INTERNAL_SERVER_ERROR);
            } else {
                return new ResponseEntity<>(success(), HttpStatus.OK);
            }
        } catch (Exception e) {
            logger.error("操作失败",e);
            return new ResponseEntity<>(fail("操作失败"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}