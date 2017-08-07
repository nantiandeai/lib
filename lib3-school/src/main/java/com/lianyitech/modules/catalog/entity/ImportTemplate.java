/**
 * 
 */
package com.lianyitech.modules.catalog.entity;

import com.lianyitech.core.utils.DataEntity;
import com.lianyitech.modules.sys.utils.UserUtils;

/**
 * 导入模板Entity
 * @author zengzy
 * @version 2016-09-12
 */
public class ImportTemplate extends DataEntity<ImportTemplate> {

	private static final long serialVersionUID = 1L;
	private String orgId;		// 机构id
	private String fileName;		// 文件名
	private String filePath;		// 文件相对路径
	private String status;		// 状态 0默认 1非默认
	private String tmpType;		// 模板类型类型 0书标
	
	public ImportTemplate() {
		super();
	}

	public ImportTemplate(String id){
		super(id);
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getTmpType() {
		return tmpType;
	}

	public void setTmpType(String tmpType) {
		this.tmpType = tmpType;
	}

}