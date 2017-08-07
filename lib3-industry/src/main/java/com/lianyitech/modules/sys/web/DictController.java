/**
 *
 */
package com.lianyitech.modules.sys.web;

import com.lianyitech.common.persistence.Page;
import com.lianyitech.common.web.ApiController;
import com.lianyitech.modules.sys.entity.Dict;
import com.lianyitech.modules.sys.service.DictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

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
     *
     * @param dict
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<?> save(@RequestBody Dict dict, HttpServletRequest request, HttpServletResponse response) {
        try {
            dictService.save(dict);
            return new ResponseEntity<String>("操作成功", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("操作失败", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /**
     * 获取列表
     *
     * @param dict
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<?> list(Dict dict, HttpServletRequest request, HttpServletResponse response) {
        Page<Dict> page = dictService.findPage(new Page<Dict>(request, response), dict);
        return new ResponseEntity<Page>(page, HttpStatus.OK);
    }

    /**
     * 获取单条记录
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> get(@PathVariable("id") String id) {
        Dict dict = dictService.get(id);
        if (null == dict) {
            return new ResponseEntity<String>("数据不存在", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Dict>(dict, HttpStatus.OK);
    }


    /**
     * 删除记录
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable("id") String id) {
        try {
            dictService.delete(id);
            return new ResponseEntity<String>("删除成功", HttpStatus.OK);
        } catch (Exception e) {
            logger.error("操作失败",e);
            return new ResponseEntity<String>("删除失败", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 根据父id查询下面的子类，不分页
     *
     * @param parentId
     * @return
     */
    @RequestMapping(value = "/{parentId}/list", method = RequestMethod.GET)
    public ResponseEntity<?> list(@PathVariable("parentId") String parentId) {
        return new ResponseEntity<List<Dict>>(dictService.findByParentId(parentId), HttpStatus.OK);
    }

}