package com.lianyitech.modules.sys.web;

import com.lianyitech.common.persistence.Page;
import com.lianyitech.common.web.ApiController;
import com.lianyitech.common.web.ResponseData;
import com.lianyitech.modules.sys.entity.Dict;
import com.lianyitech.modules.sys.service.DictService;
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
 * 字典管理Controller
 *
 * @author zengzy
 * @version 2016-09-02
 */
@RestController
@RequestMapping(value = "/api/sys/dict")
public class DictController extends ApiController {

    @Autowired
    private DictService dictService;

    /**
     * 增加/修改
     * @param dict dict
     * @return code
     */
    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<ResponseData> save(Dict dict) {
        try {
            dictService.save(dict);
            return new ResponseEntity<>(success(), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("操作失败",e);
            return new ResponseEntity<>(fail(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /**
     * 获取列表
     * @param dict dict
     * @param request request
     * @param response response
     * @return code
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<ResponseData> list(Dict dict, HttpServletRequest request, HttpServletResponse response) {
        Page<Dict> page = dictService.findPage(new Page<>(request, response), dict);
        return new ResponseEntity<>(success(page), HttpStatus.OK);
    }

    /**
     * 获取单条记录
     * @param id id
     * @return code
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<ResponseData> get(@PathVariable("id") String id) {
        Dict dict = dictService.get(id);
        if (null == dict) {
            return new ResponseEntity<>(fail("数据不存在"), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(success(dict), HttpStatus.OK);
    }


    /**
     * 删除记录
     * @param id id
     * @return code
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<ResponseData> delete(@PathVariable("id") String id) {
        try {
            dictService.delete(id);
            return new ResponseEntity<>(success(), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("操作失败",e);
            return new ResponseEntity<>(fail(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 根据父id查询下面的子类，不分页
     * @param parentId parentId
     * @return code
     */
    @Secured({"ROLE_lib3school.api.sys.dict.list.get"})
    @RequestMapping(value = "/{parentId}/list", method = RequestMethod.GET)
    public ResponseEntity<ResponseData> list(@PathVariable("parentId") String parentId) {
        return new ResponseEntity<>(success(dictService.findByParentId(parentId)), HttpStatus.OK);
    }

}