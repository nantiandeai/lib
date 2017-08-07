package com.lianyitech.modules.sys.web;

import com.lianyitech.common.persistence.Page;
import com.lianyitech.common.utils.StringUtils;
import com.lianyitech.common.web.ApiController;
import com.lianyitech.common.web.ResponseData;
import com.lianyitech.modules.sys.entity.Librarsort;
import com.lianyitech.modules.sys.service.LibrarsortService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 中图分类管理Controller
 * @author zengzy
 * @version 2016-08-31
 */
@RestController
@RequestMapping(value = "/api/sys/librarsort")
public class LibrarsortController extends ApiController {

	@Autowired
	private LibrarsortService librarsortService;

	@Secured({"ROLE_lib3school.api.sys.librarsort.get"})
	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResponseEntity<ResponseData> list(Librarsort librarsort, HttpServletRequest request, HttpServletResponse response) {
		Page<Librarsort> page = librarsortService.findPage(new Page<>(request, response), librarsort);
		return new ResponseEntity<>(success(page), HttpStatus.OK);
	}

	//写个联动主题词根据分类号去得到对应的主题词内容
	@Secured({"ROLE_lib3school.api.sys.librarsort.getSubjects.get"})
	@RequestMapping(value = {"/getSubjects"}, method = RequestMethod.GET)
	public ResponseEntity<ResponseData> getSubjects(Librarsort librarsort) {
		if (StringUtils.isBlank(librarsort.getCode())) {
			return new ResponseEntity<>(success("分类号不能为空"), HttpStatus.OK);
		}
		List<Librarsort> subjects = librarsortService.getSorts(librarsort);
		return new ResponseEntity<>(success(subjects), HttpStatus.OK);
	}

	/**
	 * 根据条件查询出版社结果集不带分页
	 ***/
	@Deprecated
	@RequestMapping(value = {"listData"})
	public ResponseEntity<ResponseData> listData(Librarsort librarsort) {
		return new ResponseEntity<>(success(librarsortService.findList(librarsort)), HttpStatus.OK);
	}
	/**
	 * 分类号验证
	 */
	@Secured({"ROLE_lib3school.api.catalog.bookdirectory.checkSortCode.post"})
	@RequestMapping(value = {"/checkSortCode"},method = RequestMethod.POST)
	public ResponseEntity<ResponseData> checkSortCode(Librarsort librarsort) {
		if (StringUtils.isBlank(librarsort.getCode())) {
			Map<String,Object> result = new HashMap<>();
			result.put("fail","分类号不能为空");
			return new ResponseEntity<>(success(result), HttpStatus.OK);
		}
		return new ResponseEntity<>(success(librarsortService.checkExistSortCode(librarsort.getCode())), HttpStatus.OK);
	}
}