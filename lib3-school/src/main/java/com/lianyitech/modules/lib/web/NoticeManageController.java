package com.lianyitech.modules.lib.web;

import com.lianyitech.common.persistence.Page;
import com.lianyitech.common.utils.StringUtils;
import com.lianyitech.common.web.ApiController;
import com.lianyitech.common.web.ResponseData;
import com.lianyitech.modules.lib.entity.NoticeManage;
import com.lianyitech.modules.lib.service.NoticeManageService;
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
 * 公共通知Controller
 * @author zengzy
 * @version 2016-09-09
 */
@RestController
@RequestMapping(value = "/api/lib/noticeManage")
public class NoticeManageController extends ApiController {

    @Autowired
    private NoticeManageService noticeManageService;

    @Secured({"ROLE_lib3school.api.lib.noticeManage.get"})
    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<ResponseData> list(NoticeManage noticeManage, HttpServletRequest request, HttpServletResponse response) {
        Page<NoticeManage> page = noticeManageService.findPage(new Page<>(request, response), noticeManage);
        return new ResponseEntity<>(success(page), HttpStatus.OK);
    }

    @Secured({"ROLE_lib3school.api.lib.noticeManage.post"})
    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<ResponseData> save(NoticeManage noticeManage) {
        try {
            noticeManageService.save(noticeManage);
            return new ResponseEntity<>(success("操作成功"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(fail("操作失败"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Secured({"ROLE_lib3school.api.lib.noticeManage.delete"})
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<ResponseData> delete(@PathVariable("id") String id) {
        try {
            noticeManageService.delete(id);
        } catch (Exception e) {
            logger.error("操作失败",e);
            return new ResponseEntity<>(fail("删除失败"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(success("删除成功"), HttpStatus.OK);
    }


    /**
     * 查询公告详情
     * @param id id
     * @return noticeManage
     */
    @Secured({"ROLE_lib3school.api.lib.noticeManage.form.get"})
    @RequestMapping(value = "/form/{id}", method = RequestMethod.GET)
    public ResponseEntity<ResponseData> form(@PathVariable String id) {
        NoticeManage noticeManage = null;
        if (StringUtils.isNotBlank(id)) {
            noticeManage = noticeManageService.get(id);
        }
        if (null == noticeManage) {
            return new ResponseEntity<>(fail("操作失败"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(success(noticeManage), HttpStatus.OK);
    }

    /**
     * 置顶
     * @param noticeManage noticeManage
     * @return ok
     */
    @Secured({"ROLE_lib3school.api.lib.noticeManage.UpSite.post"})
    @RequestMapping(value = "UpSite",method = RequestMethod.POST)
    public ResponseEntity<ResponseData> UpSite(NoticeManage noticeManage){
        try {
            noticeManageService.UpSite(noticeManage);
        } catch (Exception e) {
            logger.error("操作失败",e);
        }
        return new ResponseEntity<>(success("操作成功"), HttpStatus.OK);
    }

}