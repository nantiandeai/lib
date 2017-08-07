/**
 *
 */
package com.lianyitech.modules.circulate.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lianyitech.core.utils.DataEntity;
import com.lianyitech.modules.sys.utils.UserUtils;

/**
 * 操作单据管理Entity
 *
 * @author zengzy
 * @version 2016-09-22
 */
public class Bill extends DataEntity<Bill> {

    private static final long serialVersionUID = 1L;
    private String readerId;        // 读者ID
    private String copyId;        // 复本id
    private String orgId;        // 机构id
    private String groupId;        // 组织id
    private String billType;        // 单据类型 对应EnumBillType枚举类
    private String barcode;        // 条形码
    private String batchNo;        // 批次
    private String isbn;        // ISBN
    private String librarsortCode;        // 中图分类号
    private String tanejiNo;        // 种次号
    private String assNo;        // 辅助区分号
    private String title;        // 题名
    private String isStained;        // 是否污损 0否 1是
    private String isRenew;        // 是否续借 0否 1是
    private String isOrder;        // 是否预约 0否 1是
    private Date borrowDate;        // 借书日期
    private Date shouldReturnDate;        // 应还日期
    private Date returnDate;        // 还书日期
    private Date reserveDate;        // 预约/借日期
    private Date reserveLoseDate;        // 预约/借失效日期
    private String status;        // 状态  对应EnumBillStatus枚举类
    private Date recallDate;        // 催还日期

    private String card;//读者证

    private Integer pastDay;

    //加这属性为了续借的时候传参数用的
    private Integer renewDays;        // 续借天数

    private String readerName;

    private String groupName;

    public String getReaderName() {
        return readerName;
    }

    public void setReaderName(String readerName) {
        this.readerName = readerName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    @JsonIgnore
    public Integer getPastDay() {
        return pastDay;
    }

    public void setPastDay(Integer pastDay) {
        this.pastDay = pastDay;
    }

    public Bill() {
        super();
    }

    public Bill(String id) {
        super(id);
    }

    public String getReaderId() {
        return readerId;
    }

    public void setReaderId(String readerId) {
        this.readerId = readerId;
    }

    public String getCopyId() {
        return copyId;
    }

    public void setCopyId(String copyId) {
        this.copyId = copyId;
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

    public String getBillType() {
        return billType;
    }

    public void setBillType(String billType) {
        this.billType = billType;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getLibrarsortCode() {
        return librarsortCode;
    }

    public void setLibrarsortCode(String librarsortCode) {
        this.librarsortCode = librarsortCode;
    }

    public String getTanejiNo() {
        return tanejiNo;
    }

    public void setTanejiNo(String tanejiNo) {
        this.tanejiNo = tanejiNo;
    }

    public String getAssNo() {
        return assNo;
    }

    public void setAssNo(String assNo) {
        this.assNo = assNo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIsStained() {
        return isStained;
    }

    public void setIsStained(String isStained) {
        this.isStained = isStained;
    }

    public String getIsRenew() {
        return isRenew;
    }

    public void setIsRenew(String isRenew) {
        this.isRenew = isRenew;
    }

    public String getIsOrder() {
        return isOrder;
    }

    public void setIsOrder(String isOrder) {
        this.isOrder = isOrder;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(Date borrowDate) {
        this.borrowDate = borrowDate;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getShouldReturnDate() {
        return shouldReturnDate;
    }

    public void setShouldReturnDate(Date shouldReturnDate) {
        this.shouldReturnDate = shouldReturnDate;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getReserveDate() {
        return reserveDate;
    }

    public void setReserveDate(Date reserveDate) {
        this.reserveDate = reserveDate;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getReserveLoseDate() {
        return reserveLoseDate;
    }

    public void setReserveLoseDate(Date reserveLoseDate) {
        this.reserveLoseDate = reserveLoseDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getRecallDate() {
        return recallDate;
    }

    public void setRecallDate(Date recallDate) {
        this.recallDate = recallDate;
    }

    public Integer getRenewDays() {
        return renewDays;
    }

    public void setRenewDays(Integer renewDays) {
        this.renewDays = renewDays;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

}