/**
 * 
 */
package com.lianyitech.modules.sys.service;

import com.lianyitech.common.service.CrudService;
import com.lianyitech.modules.sys.dao.DictDao;
import com.lianyitech.modules.sys.entity.Dict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	@Cacheable(value = "dictList", key = "'dictList'+#parentId")
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

	public List<Dict> findByCon(Dict dict){
		return dao.findByCon(dict);
	}

	/**
	 * 根据条件查询字典表数据--针对导入
	 * @param dict
	 * @return list集合
	 */
	@Cacheable(value = "dictMap", key = "'dictMap'+#dict.parentId")
	public Map<String,Dict> findListByCon(Dict dict){
		Map<String,Dict> map = new HashMap<>();
		List<Dict> list = dao.findByCon(dict);
		for(Dict d : list){
			map.put(d.getLabel(),d);
		}
		return map;
	}

	public Map<String,Dict> getDictMap(Dict dict){
		Map<String,Dict> returnMap = new HashMap<>();
		Map<String,Dict> map = this.findListByCon(dict);
		for (String key : map.keySet()) {
			returnMap.put(map.get(key).getValue(),map.get(key));
		}
		return returnMap;
	}
}
