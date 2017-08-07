/**
 * 
 */
package com.lianyitech.modules.circulate.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.lianyitech.common.utils.StringUtils;
import com.lianyitech.core.utils.CustomDoubleSerialize;
import com.lianyitech.core.utils.DataEntity;

import java.text.DecimalFormat;

/**
 * 赔付管理Entity
 * @author zengzy
 * @version 2017-07-13
 */
public class CompenRecord extends DataEntity<CompenRecord> {

	private static final long serialVersionUID = 1L;
	private String readerId;		// 读者id
	private String billId;//单据ID
	private String readerName;		// 读者姓名
	private String readerCard;		// 读者证号
	private String readerGroup ; //读者组织
	private String barcode;//条形码
	private String title ;//题名
	private String opType;//操作类型  0丢失 1污损 2超期
	private String compenType;// 赔偿类型 0赔书 1 罚款
	private Double amount; //罚款金额
	private String newBarcode;//新条形码
	private String orgId;

	private String keywords1;
	private String keywords2;
	private String startDate;
	private String endDate;

	@JsonIgnore
	public String getBillId() {
		return billId;
	}

	public void setBillId(String billId) {
		this.billId = billId;
	}

	@JsonIgnore
	public String getKeywords1() {
		return keywords1;
	}

	public void setKeywords1(String keywords1) {
		this.keywords1 = keywords1;
	}
	@JsonIgnore
	public String getKeywords2() {
		return keywords2;
	}

	public void setKeywords2(String keywords2) {
		this.keywords2 = keywords2;
	}
	@JsonIgnore
	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	@JsonIgnore
	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getOpTypeName(){
		String typeName = "";
		if(StringUtils.isNotBlank(opType)) {
			if(opType.equals("0")) {
				typeName = "丢失";
			} else if (opType.equals("1")) {
				typeName = "污损";
			} else if (opType.equals("2")) {
				typeName = "超期";
			} else if (opType.equals("3")) {
				typeName = "丢失返还";
			}
		}
		return typeName;
	}

	public String getCompenTypeName(){
		String compenTypeName = "";
		if(StringUtils.isNotBlank(compenType)) {
			if(compenType.equals("0")) {
				compenTypeName = "赔书";
			} else if (compenType.equals("1")) {
				compenTypeName = "罚款";
			} else if (compenType.equals("2")) {
				compenTypeName = "退款";
			}
		}
		return compenTypeName;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

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

	public String getReaderGroup() {
		return readerGroup;
	}

	public void setReaderGroup(String readerGroup) {
		this.readerGroup = readerGroup;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getNewBarcode() {
		return newBarcode;
	}

	public void setNewBarcode(String newBarcode) {
		this.newBarcode = newBarcode;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getOpType() {
		return opType;
	}

	public void setOpType(String opType) {
		this.opType = opType;
	}

	public String getCompenType() {
		return compenType;
	}

	public void setCompenType(String compenType) {
		this.compenType = compenType;
	}

	@JsonSerialize(using = CustomDoubleSerialize.class)
	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
}