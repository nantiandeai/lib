/**
 * 
 */
package com.lianyitech.modules.catalog.service;

import com.lianyitech.common.service.CrudService;
import com.lianyitech.modules.catalog.dao.BookDirectoryDao;
import com.lianyitech.modules.catalog.entity.BookDirectory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 书目管理Service
 * @author zym
 * @version 2016-09-21
 */
@Service

public class BookDirectoryService extends CrudService<BookDirectoryDao, BookDirectory> {
	public BookDirectory get(String id) {
		return super.get(id);
	}

}