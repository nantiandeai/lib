/**
 * 
 */
package com.lianyitech.modules.catalog.utils;

import com.lianyitech.common.utils.Global;
import com.lianyitech.common.utils.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * 书目管理Entity
 * @author zengzy
 * @version 2016-08-26
 */
public class SolrPage {
	private int pageNo = 1; // 当前页码
	private int pageSize = Integer.valueOf(Global.getConfig("lib3.school.page.pageSize")); // 页面大小，设置为“-1”表示不进行分页（分页无效）
	private int pageCount=0;//总页数
	private long count;// 总记录数，设置为“-1”表示不查询总数
	private List<?> list = new ArrayList<>();
	//private List<?> list1 = new ArrayList<>();
	public int getPageNo() {
		return pageNo;
	}

	private void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getPageSize() {
		return pageSize;
	}

	private void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}



	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public List<?> getList() {
		return list;
	}

	public void setList(List<?> list) {
		this.list = list;
	}

	public SolrPage(HttpServletRequest request){
		// 设置页码参数（传递repage参数，来记住页码）
		String no = request.getParameter("pageNo");
		if (StringUtils.isNumeric(no)){
			this.setPageNo(Integer.parseInt(no));
		}
		if(this.getPageNo()<1){
			this.setPageNo(1);
		}
		//this.getPageCount();
		// 设置页面大小参数（传递repage参数，来记住页码大小）
		String size = request.getParameter("pageSize");
		if (StringUtils.isNumeric(size)){
			this.setPageSize(Integer.parseInt(size));
		}
	}
	public int getPageCount() {
		return pageCount;
	}

	/**
	 * 初始化参数
	 */
	public void initialize(){
		//计算总页数
		this.pageCount = (int) this.count / this.pageSize;
		int mod = (int) this.count % this.pageSize;
		if (mod > 0) {
			this.pageCount++;
		}
	}

}