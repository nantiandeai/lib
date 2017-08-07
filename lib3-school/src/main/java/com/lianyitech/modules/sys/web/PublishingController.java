package com.lianyitech.modules.sys.web;

import com.lianyitech.common.persistence.Page;
import com.lianyitech.common.utils.StringUtils;
import com.lianyitech.common.web.ApiController;
import com.lianyitech.common.web.ResponseData;
import com.lianyitech.modules.sys.entity.Publishing;
import com.lianyitech.modules.sys.service.PublishingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 出版社管理Controller
 *
 * @author zengzy
 * @version 2016-08-31
 */
@RestController
@RequestMapping(value = "/api/sys/publishing")
public class PublishingController extends ApiController {

    @Autowired
    private PublishingService publishingService;

    @Secured({"ROLE_lib3school.api.sys.publishing.get"})
    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<ResponseData> list(Publishing publishing, HttpServletRequest request, HttpServletResponse response) {
        Page<Publishing> page = publishingService.findPage(new Page<>(request, response), publishing);
        return new ResponseEntity<>(success(page), HttpStatus.OK);
    }


    @RequestMapping(value = "save")
    public ResponseEntity<ResponseData> save(Publishing publishing) {
        try {
            publishingService.save(publishing);
            return new ResponseEntity<>(success("操作成功"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(fail("操作失败"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public ResponseEntity<ResponseData> delete(@PathVariable String id) {
        try {
            publishingService.delete(id);
            return new ResponseEntity<>(success("删除成功"), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("操作失败",e);
            return new ResponseEntity<>(fail("删除失败"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 根据条件查询出版社结果集不带分页
     ***/
    @Secured({"ROLE_lib3school.api.sys.publishing.listData.get"})
    @RequestMapping(value = {"listData"})
    public ResponseEntity<ResponseData> listData(Publishing publishing) {
        if(StringUtils.isEmpty(publishing.getIsbn())){
            return new ResponseEntity<>(fail("isbn不能为空"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(success(publishingService.findList(publishing)), HttpStatus.OK);
    }


}