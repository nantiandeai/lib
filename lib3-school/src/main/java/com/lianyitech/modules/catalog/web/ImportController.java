package com.lianyitech.modules.catalog.web;

import com.lianyitech.common.persistence.Page;
import com.lianyitech.common.web.ApiController;
import com.lianyitech.common.web.ResponseData;
import com.lianyitech.modules.catalog.entity.ImportRecord;
import com.lianyitech.modules.catalog.service.ImportService;
import com.lianyitech.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 导入Controller
 * @author zengzy
 * @version 2016-08-26
 */
@RestController
@RequestMapping(value = "/api/import/record")
public class ImportController extends ApiController {

	@Autowired
	private ImportService importService;

	@RequestMapping(value = "",method = RequestMethod.GET)
	public ResponseEntity<ResponseData> list(ImportRecord record, HttpServletRequest request, HttpServletResponse response) {
		record.setCreateBy(UserUtils.getLoginName());
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		try{
			record.setCreateDate(format.parse(format.format(new Date())));
		}catch (Exception e) {
			logger.error("操作失败",e);
		}
		Page<ImportRecord> page = importService.findPage(new Page<>(request, response), record);
		return new ResponseEntity<>(success(page), HttpStatus.OK);
	}

	@RequestMapping(value = "form",method = RequestMethod.GET)
	public ResponseEntity<ResponseData> form(ImportRecord record) {
		record = importService.get(record);
		if (null == record) {
			return new ResponseEntity<>(fail("获取失败!"), HttpStatus.OK);
		}
		return new ResponseEntity<>(success(record), HttpStatus.OK);
	}

}