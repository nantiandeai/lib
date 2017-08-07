/**
 * 
 */
package com.lianyitech.modules.lib.entity;

import com.lianyitech.core.utils.DataEntity;
import com.lianyitech.modules.sys.utils.UserUtils;

/**
 * 待办管理Entity
 * @author zengzy
 * @version 2016-09-09
 */
public class WaitingHandle extends DataEntity<WaitingHandle> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// 待办名称
	private String themeDescribe;		// 主题描述
	private String otherThemeDescribe;		// 主题副描述
	private String waitingType;		// 类型  0公告通知   1待办事项
	private String userType;		// 适用应用对象   0单管   1读者
	private String dealData;		// 待办数据
	
	public WaitingHandle() {
		super();
	}

	public WaitingHandle(String id){
		super(id);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getThemeDescribe() {
		return themeDescribe;
	}

	public void setThemeDescribe(String themeDescribe) {
		this.themeDescribe = themeDescribe;
	}
	
	public String getOtherThemeDescribe() {
		return otherThemeDescribe;
	}

	public void setOtherThemeDescribe(String otherThemeDescribe) {
		this.otherThemeDescribe = otherThemeDescribe;
	}
	
	public String getWaitingType() {
		return waitingType;
	}

	public void setWaitingType(String waitingType) {
		this.waitingType = waitingType;
	}
	
	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}
	
	public String getDealData() {
		return dealData;
	}

	public void setDealData(String dealData) {
		this.dealData = dealData;
	}
}