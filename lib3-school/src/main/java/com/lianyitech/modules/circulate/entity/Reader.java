/**
 * 
 */
package com.lianyitech.modules.circulate.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.lianyitech.core.utils.CustomDoubleSerialize;
import com.lianyitech.core.utils.DataEntity;
import com.lianyitech.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.Date;

/**
 * 读者管理Entity
 * @author zengzy
 * @version 2016-09-09
 */
public class Reader extends DataEntity<Reader> {

	private static final long serialVersionUID = 1L;
	private String orgId;		// 机构id
	private String groupId;		// 组织id
	private String name;		// 姓名
	private String email;		// 邮箱
	private String phone;		// 手机号码
	private String certType;		// 证件类型    1.学生证 2.身份证  3.学籍证
	private String certNum;		// 证件号码,这NM不是读者证啊
	private String readerType;		// 读者类型
	private String readerPassword;   //读者密码
	private String img;   //头像
	private String sex;   //性别   0是男 1是女


	//下面字段是非数据库表里面字段，用于读者数据导入
	private  String certName;   //证件名称
	private  String readerTypeName;  //读者类型名称
	private  String oldReaderId;    //旧读者ID
	private  String terminationDate; //终止日期
	private  String card;          //读者证号
	private  String groupName;          //读者组织
	private  String errorinfo;    //导入错误信息
	private  String status;			//读者证状态
	private Date endDate; //终止日期
	private Date startDate;//办证日期

	private String password;
	private String newCard;

	private String keyWords;

	private String circulateNum;//流通记录条数

	private String orderBy ;

	private Double deposit;//押金

	private String billId;

	private String startCard;

	private String endCard;

	private String cardImg;//条码二维码图片

	public String getCardImg() {
		return cardImg;
	}

	public void setCardImg(String cardImg) {
		this.cardImg = cardImg;
	}

	public String getStartCard() {
		return startCard;
	}

	public void setStartCard(String startCard) {
		this.startCard = startCard;
	}

	public String getEndCard() {
		return endCard;
	}

	public void setEndCard(String endCard) {
		this.endCard = endCard;
	}

	@JsonIgnore
	public String getBillId() {
		return billId;
	}

	public void setBillId(String billId) {
		this.billId = billId;
	}

	@JsonIgnore
	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public String getCirculateNum() {
		return circulateNum;
	}

	public void setCirculateNum(String circulateNum) {
		this.circulateNum = circulateNum;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	@JsonIgnore
	public String getKeyWords() {
		return keyWords;
	}

	public void setKeyWords(String keyWords) {
		this.keyWords = keyWords;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNewCard() {
		return newCard;
	}

	public void setNewCard(String newCard) {
		this.newCard = newCard;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getReaderPassword() {
		return readerPassword;
	}

	public void setReaderPassword(String readerPassword) {
		this.readerPassword = readerPassword;
	}

	private String[] idList;

	public String[] getIdList() {
		return idList;
	}

	public void setIdList(String[] idList) {
		this.idList = idList;
	}

	public Reader() {
		super();
	}



	public Reader(String id){
		super(id);
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	
	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public String getCertType() {
		return certType;
	}

	public void setCertType(String certType) {
		this.certType = certType;
	}
	
	public String getCertNum() {
		return certNum;
	}

	public void setCertNum(String certNum) {
		this.certNum = certNum;
	}
	
	public String getReaderType() {
		return readerType;
	}

	public void setReaderType(String readerType) {
		this.readerType = readerType;
	}
	@JsonIgnore
	public String getCertName() {
		return certName;
	}

	public void setCertName(String certName) {
		this.certName = certName;
	}
	@JsonIgnore
	public String getReaderTypeName() {
		return readerTypeName;
	}

	public void setReaderTypeName(String readerTypeName) {
		this.readerTypeName = readerTypeName;
	}
	@JsonIgnore
	public String getOldReaderId() {
		return oldReaderId;
	}

	public void setOldReaderId(String oldReaderId) {
		this.oldReaderId = oldReaderId;
	}
	@JsonIgnore
	public String getTerminationDate() {
		return terminationDate;
	}

	public void setTerminationDate(String terminationDate) {
		this.terminationDate = terminationDate;
	}
//	@JsonIgnore
	public String getCard() {
		return card;
	}

	public void setCard(String card) {
		this.card = card;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	@JsonIgnore
	public String getErrorinfo() {
		return errorinfo;
	}

	public void setErrorinfo(String errorinfo) {
		this.errorinfo = errorinfo;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	@JsonSerialize(using = CustomDoubleSerialize.class)
	public Double getDeposit() {
		return deposit;
	}

	public void setDeposit(Double deposit) {
		this.deposit = deposit;
	}
}