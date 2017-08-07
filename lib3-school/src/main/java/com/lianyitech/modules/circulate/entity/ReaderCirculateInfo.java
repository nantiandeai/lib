package com.lianyitech.modules.circulate.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.lianyitech.common.persistence.Page;
import com.lianyitech.core.enmu.EnumCertStatus;
import com.lianyitech.core.utils.CustomDoubleSerialize;
import org.apache.poi.ss.formula.functions.T;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 读者流通信息
 * Created by chenxiaoding on 2016/9/12.
 */
public class ReaderCirculateInfo {
    private String readerId;//读者ID
    private String readerType;//读者类型
    private String name;//读者姓名
    private String cardStatus;//读者证状态
    private String status ;
    private Integer borrowNumber;//借阅册数
    private Integer countBorrow;//己借数
    private Date endDate;//终止日期
    private String readerCard;//读者证号
    private String groupName;//组织名称
    private String img; //读者的头像
    private List<ReaderUnionBook> list = new ArrayList<>();//所借书的集合
    private Page<ReaderUnionBook> page ;

    private String groupId;
    private String sex;//性别   0是男 1是女
    private Double deposit;//押金
    private String phone;		// 手机号码
    private String certType;		// 证件类型    1.学生证 2.身份证  3.学籍证
    private String certNum;		// 证件号码,这NM不是读者证啊

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
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

    @JsonSerialize(using = CustomDoubleSerialize.class)
    public Double getDeposit() {
        return deposit;
    }

    public void setDeposit(Double deposit) {
        this.deposit = deposit;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getStatus() {
        return cardStatus;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Page<ReaderUnionBook> getPage() {
        return page;
    }

    public void setPage(Page<ReaderUnionBook> page) {
        this.page = page;
    }

    public String getReaderCard() {
        return readerCard;
    }

    public void setReaderCard(String readerCard) {
        this.readerCard = readerCard;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<ReaderUnionBook> getList() {
        return list;
    }

    public void setList(List<ReaderUnionBook> list) {
        this.list = list;
    }

    public ReaderCirculateInfo() {
    }

    public String getReaderId() {
        return readerId;
    }

    public void setReaderId(String readerId) {
        this.readerId = readerId;
    }

    public String getReaderType() {
        return readerType;
    }

    public void setReaderType(String readerType) {
        this.readerType = readerType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCardStatus() {
        return EnumCertStatus.parse(cardStatus).getName();
    }

    public void setCardStatus(String cardStatus) {
        this.cardStatus = cardStatus;
    }

    public Integer getBorrowNumber() {
        return borrowNumber;
    }

    public void setBorrowNumber(Integer borrowNumber) {
        this.borrowNumber = borrowNumber;
    }

    public Integer getCountBorrow() {
        return countBorrow;
    }

    public void setCountBorrow(Integer countBorrow) {
        this.countBorrow = countBorrow;
    }
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }


}
