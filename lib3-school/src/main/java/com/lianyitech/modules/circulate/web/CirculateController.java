package com.lianyitech.modules.circulate.web;

import com.lianyitech.common.web.ApiController;
import com.lianyitech.common.web.ResponseData;
import com.lianyitech.modules.circulate.entity.CirculateDTO;
import com.lianyitech.modules.circulate.service.CirculateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.InvocationTargetException;

/**
 * 操作单据管理Controller
 * @author zengzy
 * @version 2016-09-09
 */
@RestController
@RequestMapping(value = "/api/circulate")
public class CirculateController extends ApiController {

	@Autowired
	private CirculateService service;

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<ResponseData> create(CirculateDTO dto) {
		try {
			return new ResponseEntity<>(success(service.createSingle(dto)), HttpStatus.OK);
		} catch (Exception e) {
			logger.error("操作失败",e);
			return new ResponseEntity<>(fail("参数解析错误"), HttpStatus.BAD_REQUEST);
		}
	}

	@Secured({"ROLE_lib3school.api.circulate.multi.post"})
	@RequestMapping(value = "/multi", method = RequestMethod.POST)
	public ResponseEntity<ResponseData> createMulti(CirculateDTO dto) {
		try {
			return new ResponseEntity<>(success(service.createMulti(dto)), HttpStatus.OK);
		} catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
			return new ResponseEntity<>(fail("参数解析错误"), HttpStatus.BAD_REQUEST);
		}
	}

}