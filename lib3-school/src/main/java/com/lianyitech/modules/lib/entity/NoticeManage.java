/**
 * 
 */
package com.lianyitech.modules.lib.entity;

import com.lianyitech.core.utils.DataEntity;
import com.lianyitech.modules.sys.utils.UserUtils;

/**
 * 公共通知Entity
 * @author zengzy
 * @version 2016-09-09
 */
public class NoticeManage extends DataEntity<NoticeManage> {

	private static final long serialVersionUID = 1L;
	private String theme;		// 主题
	private String content;		// 通知内容
	private String orgName;		// 发布单位
	private String IsTop; 		//置顶

	public NoticeManage() {
		super();
	}

	public NoticeManage(String id){
		super(id);
	}

	public String getTheme() {
		return theme;
	}
	public String getIsTop() {
		return IsTop;
	}

	public void setIsTop(String isTop) {
		IsTop = isTop;
	}
	public void setTheme(String theme) {
		this.theme = theme;
	}
	
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

}