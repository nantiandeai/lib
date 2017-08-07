/**
 * 
 */
package com.lianyitech.modules.lib.service;

import com.lianyitech.common.persistence.Page;
import com.lianyitech.common.service.CrudService;
import com.lianyitech.modules.lib.dao.WaitingHandleDao;
import com.lianyitech.modules.lib.entity.WaitingHandle;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * 待办管理Service
 * @author zengzy
 * @version 2016-09-09
 */
@Service

public class WaitingHandleService extends CrudService<WaitingHandleDao, WaitingHandle> {

	public WaitingHandle get(String id) {
		return super.get(id);
	}
	
	public List<WaitingHandle> findList(WaitingHandle waitingHandle) {
		return super.findList(waitingHandle);
	}
	
	public Page<WaitingHandle> findPage(Page<WaitingHandle> page, WaitingHandle waitingHandle) {
		return super.findPage(page, waitingHandle);
	}
	
	@Transactional
	public void save(WaitingHandle waitingHandle) {
		super.save(waitingHandle);
	}
	
	@Transactional
	public int delete(String ids) {
		return super.delete(ids);
	}
	
}