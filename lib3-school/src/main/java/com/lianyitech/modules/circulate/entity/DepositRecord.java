/**
 * 
 */
package com.lianyitech.modules.circulate.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.lianyitech.core.utils.CustomDoubleSerialize;
import com.lianyitech.core.utils.DataEntity;

/**
 * 押金记录管理Entity
 * @author zengzy
 * @version 2017-07-13
 */
public class DepositRecord extends DataEntity<DepositRecord> {

	private static final long serialVersionUID = 1L;
	private String readerId;		// 读者id
	private String readerName;		// 读者姓名
	private String readerCard;		// 读者证号

	private Double amount;		// 金额
	private String opType;		// 操作类型 0交押金 1退押金 2旧证转出 3新证转入
	private String orgId;		// 机构id

	public String getReaderId() {
		return readerId;
	}

	public void setReaderId(String readerId) {
		this.readerId = readerId;
	}

	public String getReaderName() {
		return readerName;
	}

	public void setReaderName(String readerName) {
		this.readerName = readerName;
	}

	public String getReaderCard() {
		return readerCard;
	}

	public void setReaderCard(String readerCard) {
		this.readerCard = readerCard;
	}

	@JsonSerialize(using = CustomDoubleSerialize.class)
	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getOpType() {
		return opType;
	}

	public void setOpType(String opType) {
		this.opType = opType;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
}