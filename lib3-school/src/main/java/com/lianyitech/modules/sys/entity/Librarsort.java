/**
 * 
 */
package com.lianyitech.modules.sys.entity;

import com.lianyitech.core.utils.DataEntity;
import com.lianyitech.modules.sys.utils.UserUtils;
import org.hibernate.validator.constraints.Length;

/**
 * 中图分类管理Entity
 * @author zengzy
 * @version 2016-08-31
 */
public class Librarsort extends DataEntity<Librarsort> {

	private static final long serialVersionUID = 1L;
	private String code;		// 分类号
	private String name;		// 分类名称
	private String parentCode;		// 父分类号
	private Integer sort;		// 排序
	private Integer levels;		// 分类级别(1:一级分类 2：二级分类 3：三级分类 4：四级分类)
	private String isleaf;		// 是否有子节点 0否 1是
	private String subjects;//主题词（目前放入一张表）
	
	public Librarsort() {
		super();
	}

	public Librarsort(String id){
		super(id);
	}

	@Length(min=0, max=150, message="分类号长度必须介于 0 和 150 之间")
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	@Length(min=0, max=150, message="分类名称长度必须介于 0 和 150 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Length(min=0, max=150, message="父分类号长度必须介于 0 和 150 之间")
	public String getParentCode() {
		return parentCode;
	}

	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}
	
	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}
	
	public Integer getLevels() {
		return levels;
	}

	public void setLevels(Integer levels) {
		this.levels = levels;
	}
	
	@Length(min=0, max=1, message="是否有子节点 0否 1是长度必须介于 0 和 1 之间")
	public String getIsleaf() {
		return isleaf;
	}

	public void setIsleaf(String isleaf) {
		this.isleaf = isleaf;
	}

	public String getSubjects() {
		return subjects;
	}

	public void setSubjects(String subjects) {
		this.subjects = subjects;
	}

}