/**
 * 
 */
package com.lianyitech.modules.catalog.web;

import com.lianyitech.common.web.ApiController;
import com.lianyitech.common.web.ResponseData;
import com.lianyitech.modules.catalog.entity.BookDirectory;
import com.lianyitech.modules.catalog.service.BookDirectoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

/**
 * 书目管理Controller
 * @author zym
 * @version 2016-09-21
 */
@RestController
@RequestMapping(value = "/api/catalog/bookdirectory")
public class BookDirectoryController extends ApiController {
	@Autowired
	BookDirectoryService bookDirectoryService;

	//@RequiresPermissions("catalog:bookDirectory:view")
	@RequestMapping(value = {"/copy"},method = RequestMethod.POST)
	public ResponseEntity<ResponseData> copy(BookDirectory bookDirectory) {
		BookDirectory book = bookDirectoryService.get(bookDirectory.getId());
		if (null == book) {
			return new ResponseEntity<>(fail("书库不存在"), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(success(book), HttpStatus.OK);
	}
}