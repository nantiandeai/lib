package com.lianyitech.modules.sys.service;

import com.lianyitech.common.persistence.Page;
import com.lianyitech.common.service.CrudService;
import com.lianyitech.common.utils.DateUtils;
import com.lianyitech.modules.sys.dao.LogDao;
import com.lianyitech.modules.sys.entity.Log;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 日志Service
 * 
 * @version 2014-05-16
 */
@Service

public class LogService extends CrudService<LogDao, Log> {

	public Page<Log> findPage(Page<Log> page, Log log) {
		
		// 设置默认时间范围，默认当前月
		if (log.getBeginDate() == null){
			log.setBeginDate(DateUtils.setDays(DateUtils.parseDate(DateUtils.getDate()), 1));
		}
		if (log.getEndDate() == null){
			log.setEndDate(DateUtils.addMonths(log.getBeginDate(), 1));
		}
		
		return super.findPage(page, log);
		
	}
	
}
