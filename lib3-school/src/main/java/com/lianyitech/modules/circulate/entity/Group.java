/**
 * 
 */
package com.lianyitech.modules.circulate.entity;

import com.lianyitech.modules.sys.utils.UserUtils;
import org.hibernate.validator.constraints.Length;

import com.lianyitech.core.utils.DataEntity;

/**
 * 读者组织管理Entity
 * @author zengzy
 * @version 2016-09-19
 */
public class Group extends DataEntity<Group> {

	private static final long serialVersionUID = 1L;
	private String orgId;// 机构id
	private String name;		// 名称
	private String groupType;		// 组织类型 0班级 1部门
	private String status;		// 状态  0有效 1无效

	private Integer groupNumber;//组织人数

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public Integer getGroupNumber() {
		return groupNumber;
	}

	public void setGroupNumber(Integer groupNumber) {
		this.groupNumber = groupNumber;
	}

	public Group() {
		super();
	}

	public Group(String id){
		super(id);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getGroupType() {
		return groupType;
	}

	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}