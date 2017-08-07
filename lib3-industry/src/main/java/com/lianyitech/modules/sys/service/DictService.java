/**
 * 
 */
package com.lianyitech.modules.sys.service;

import com.lianyitech.common.service.CrudService;
import com.lianyitech.modules.sys.dao.DictDao;
import com.lianyitech.modules.sys.entity.Dict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 字典Service
 * 
 * @version 2014-05-16
 */
@Service

public class DictService extends CrudService<DictDao, Dict> {


	/**
	 * 根据父类型查询下面子类
	 * @param parentId
	 * @return
	 */
	public List<Dict> findByParentId(String parentId){
		Dict dict=new Dict();
		dict.setParentId(parentId);
		return dao.findByParentId(dict);
	}

	@Transactional
	public void save(Dict dict) {
		super.save(dict);
//		CacheUtils.remove(DictUtils.CACHE_DICT_MAP);
	}

	@Transactional
	public void delete(Dict dict) {
		super.delete(dict);
//		CacheUtils.remove(DictUtils.CACHE_DICT_MAP);
	}

}
