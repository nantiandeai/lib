/**
 * 
 */
package com.lianyitech.modules.sys.entity;

import com.google.common.collect.Lists;
import com.lianyitech.common.utils.Global;
import com.lianyitech.core.utils.DataEntity;

import java.util.List;

/**
 * 角色管理Entity
 * @author zengzy
 * @version 2016-08-10
 */
public class Role extends DataEntity<Role> {
	
	private static final long serialVersionUID = 1L;
	private String orgId;		// 机构编号
	private String name;		// 角色名称
	private String enname;		// 英文名称
	private String dataScope;		// 数据范围
	private String useable;		// 是否可用 1是
	private String defaultType;  //默认角色类型  1学校  2机构
	private String menuIds;   //角色权限ids
	private String menus;   //角色权限names

	public Role() {
		super();
		this.useable= Global.YES;
	}

	public Role(String id){
		super(id);
	}

	public String getMenuIds() {
		return menuIds;
	}

	public void setMenuIds(String menuIds) {
		this.menuIds = menuIds;
	}

	public String getMenus() {
		return menus;
	}

	public void setMenus(String menus) {
		this.menus = menus;
	}
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getEnname() {
		return enname;
	}

	public void setEnname(String enname) {
		this.enname = enname;
	}
	
	public String getDataScope() {
		return dataScope;
	}

	public void setDataScope(String dataScope) {
		this.dataScope = dataScope;
	}
	
	public String getUseable() {
		return useable;
	}

	public void setUseable(String useable) {
		this.useable = useable;
	}

	public String getDefaultType() {return defaultType;}

	public void setDefaultType(String defaultType) {this.defaultType = defaultType;}

}