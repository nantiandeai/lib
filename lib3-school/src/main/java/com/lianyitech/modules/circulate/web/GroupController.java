package com.lianyitech.modules.circulate.web;

import com.lianyitech.common.persistence.Page;
import com.lianyitech.common.utils.StringUtils;
import com.lianyitech.common.web.ApiController;
import com.lianyitech.common.web.ResponseData;
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

import com.lianyitech.modules.circulate.entity.Group;
import com.lianyitech.modules.circulate.service.GroupService;


/**
 * 读者组织管理Controller
 *
 * @author zengzy
 * @version 2016-09-19
 */
@RestController
@RequestMapping(value = "/api/circulate/group")
public class GroupController extends ApiController {

    @Autowired
    private GroupService groupService;

    @Secured({"ROLE_lib3school.api.circulate.group.get"})
    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<ResponseData> list(Group group, HttpServletRequest request, HttpServletResponse response) {
        Page<Group> page = groupService.findPage(new Page<>(request, response), group);
        return new ResponseEntity<>(success(page), HttpStatus.OK);
    }

    @Secured({"ROLE_lib3school.api.circulate.group.post"})
    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<ResponseData> save(Group group) {
        try {
            String id = group.getId();
            if (StringUtils.isEmpty(group.getName())) {
                return new ResponseEntity<>(fail("组织名不能为空"), HttpStatus.INTERNAL_SERVER_ERROR);
            }
            if (StringUtils.isNotEmpty(id) && group.getName().contains(",")) {
                return new ResponseEntity<>(fail("不能包含逗号"), HttpStatus.INTERNAL_SERVER_ERROR);
            }
            String[] names = group.getName().split(",");
            StringBuilder sb = new StringBuilder("");
            for (String name : names) {
                Group g = new Group();
                g.setId(id);
                g.setName(name);
                if (groupService.isSameGroup(g)) {
                    sb.append(g.getName()).append("|");
                }
            }
            if (sb.toString().length() > 0) {
                return new ResponseEntity<>(fail(sb.toString().substring(0,sb.length()-1) + "已存在"), HttpStatus.INTERNAL_SERVER_ERROR);
            }
            groupService.save(group);
            return new ResponseEntity<>(success(), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("操作失败",e);
            return new ResponseEntity<>(fail("保存失败"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @Secured({"ROLE_lib3school.api.circulate.group.delete"})
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<ResponseData> delete(@PathVariable("id") String id) {
        try {
            int i = groupService.delete(id);
            if (i == -1) {
                return new ResponseEntity<>(fail("组织内读者有流通记录，删除失败"), HttpStatus.INTERNAL_SERVER_ERROR);
            }
            if (i == -2){
                return new ResponseEntity<>(fail("该组织中有读者存在押金不可删除"),HttpStatus.INTERNAL_SERVER_ERROR);
            }

            return new ResponseEntity<>(success(), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("操作失败",e);
            return new ResponseEntity<>(fail("删除失败"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Secured({"ROLE_lib3school.api.circulate.group.logOutGroup.delete"})
    @RequestMapping(value = "/logOutGroup/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<ResponseData> logOutGroup(@PathVariable("id") String groups) {
        String msg;
        try {
            msg = groupService.logOutGroup(groups);
            if (!msg.equals("")) {
                return new ResponseEntity<>(fail(msg), HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return new ResponseEntity<>(success("注销成功！"), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("操作失败",e);
            return new ResponseEntity<>(fail(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}