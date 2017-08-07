/**
 * 
 */
package com.lianyitech.modules.catalog.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lianyitech.core.utils.DataEntity;
import com.lianyitech.modules.sys.utils.UserUtils;

/**
 * 批次管理Entity
 * @author zengzy
 * @version 2016-08-26
 */
public class Batch extends DataEntity<Batch> {
	
	private static final long serialVersionUID = 1L;
	private String orgId;		// 机构id
	private String batchNo;		// 批次号
	private Integer actualSpeciesNum; //实到种数
	private Integer speciesNum;		// 种数
	private Integer actualBookNum;   //实到册数
	private Integer bookNum;		// 册数
	private String status;
    private String type;            //类型 0是书目 1是期刊
	private String beginDate;		// 开始日期
	private String endDate;        // 结束日期

	@JsonIgnore
	public String getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}
	@JsonIgnore
	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	public Batch() {
		super();
	}

	public Batch(String id){
		super(id);
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	
	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}
	
	public Integer getSpeciesNum() {
		return speciesNum;
	}

	public void setSpeciesNum(Integer speciesNum) {
		this.speciesNum = speciesNum;
	}
	
	public Integer getBookNum() {
		return bookNum;
	}

	public void setBookNum(Integer bookNum) {
		this.bookNum = bookNum;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getActualSpeciesNum() {
		return actualSpeciesNum;
	}

	public void setActualSpeciesNum(Integer actualSpeciesNum) {
		this.actualSpeciesNum = actualSpeciesNum;
	}

	public Integer getActualBookNum() {
		return actualBookNum;
	}

	public void setActualBookNum(Integer actualBookNum) {
		this.actualBookNum = actualBookNum;
	}
}