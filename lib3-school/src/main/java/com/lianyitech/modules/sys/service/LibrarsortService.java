/**
 * 
 */
package com.lianyitech.modules.sys.service;

import com.lianyitech.common.utils.StringUtils;
import com.lianyitech.core.utils.SystemUtils;
import com.lianyitech.modules.sys.dao.LibrarsortDao;
import com.lianyitech.modules.sys.entity.Librarsort;
import com.lianyitech.common.persistence.Page;
import com.lianyitech.common.service.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 中图分类管理Service
 * @author zengzy
 * @version 2016-08-31
 */
@Service
public class LibrarsortService extends CrudService<LibrarsortDao, Librarsort> {
	@Autowired
	private LibrarsortDao librarsortDao;
	public Librarsort get(String id) {
		return super.get(id);
	}
	
	public List<Librarsort> findList(Librarsort librarsort) {
		return super.findList(librarsort);
	}
	
	public Page<Librarsort> findPage(Page<Librarsort> page, Librarsort librarsort) {
		return super.findPage(page, librarsort);
	}
	
	@Transactional
	public void save(Librarsort librarsort) {
		super.save(librarsort);
	}
	
	@Transactional
	public void delete(Librarsort librarsort) {
		super.delete(librarsort);
	}

	public List<Librarsort> getSorts(Librarsort librarsort){
		return librarsortDao.findBySort(librarsort);
	}
	/**
	 功能改进 #3856中图法分类号码表优化
	 判断分类号是否在分类表中存在（并非之前分类号验证规则SystemUtils.checkSortCode(bookDirectory.getLibrarsortCode())）
	 **/
	public Map<String,Object> checkExistSortCode(String sortCode) {
		Map<String,Object> result = new HashMap<>();

		if (!SystemUtils.checkSortCode(sortCode)){
			result.put("fail","分类号不存在!!");
			return result;
		}
		result.put("success","验证成功");
//		Librarsort librarsort = new Librarsort();
//		librarsort.setCode(sortCode);
//		List<Librarsort> list =librarsortDao.findBySort(librarsort);
//		if(list!=null&&list.size()>0){
//			result.put("success","验证成功");
//		}else{
//			result.put("fail","分类号不存在");
//		}
		return result;
	}

	public List<Librarsort> findAll() {
		return dao.findAll();
	}
}