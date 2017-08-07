package com.lianyitech.modules.circulate.web;

import com.lianyitech.common.persistence.Page;
import com.lianyitech.common.web.ApiController;
import com.lianyitech.common.web.ResponseData;
import com.lianyitech.modules.circulate.entity.Rule;
import com.lianyitech.modules.circulate.service.RuleService;
import com.lianyitech.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 借阅规则管理Controller
 * @author zengzy
 * @version 2016-09-07
 */
@RestController
@RequestMapping(value = "/api/circulate/rule")
public class RuleController extends ApiController {

	@Autowired
	private RuleService ruleService;

    /**
     * 查询所有数据
     * @param rule      //借阅实体
     * @param request    // 请求结果
     * @param response     // 请求
     * @return             //ResponseEntity
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<ResponseData> list(Rule rule, HttpServletRequest request, HttpServletResponse response) {
		Page<Rule> page = ruleService.findPage(new Page<>(request, response), rule);
		return new ResponseEntity<>(success(page), HttpStatus.OK);
	}

    /**
     * 新增和修改
     * @param rule    //借阅实体
     * @return         //操作结果
     */
    @Secured({"ROLE_lib3school.api.circulate.rule.post"})
    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<ResponseData> save(Rule rule) {
		try {
		    String orgId = UserUtils.getOrgId();
            rule.setOrgId(orgId);
            ruleService.save(rule);
            return new ResponseEntity<>(success("操作成功"), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("操作失败",e);
            return new ResponseEntity<>(fail("操作失败"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
	}
	


}