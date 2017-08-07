package com.lianyitech.modules.circulate.entity;

import java.util.Date;

/**
 * Created by jordan jiang on 2016/9/18.
 */
public class BillOverdueRecall {
    private String billId;       //单据ID
    private String billType;     //操作类型
    private String readerId;     //读者ID
    private String barcode;      //条形码
    private String status;       //图书状态
    private Date sysNowDate;    //系统当前时间
    private Date shouldReturnDate;    //应还日期
    private Date recallDate;    //催还日期
    private String title;         //题名
    private String readerName;   // 读者姓名
    private String orgId;        //机构ID

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public String getBillType() {
        return billType;
    }

    public void setBillType(String billType) {
        this.billType = billType;
    }

    public Date getSysNowDate() {
        return sysNowDate;
    }

    public void setSysNowDate(Date sysNowDate) {
        this.sysNowDate = sysNowDate;
    }

    public Date getShouldReturnDate() {
        return shouldReturnDate;
    }

    public void setShouldReturnDate(Date shouldReturnDate) {
        this.shouldReturnDate = shouldReturnDate;
    }

    public Date getRecallDate() {
        return recallDate;
    }

    public void setRecallDate(Date recallDate) {
        this.recallDate = recallDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReaderName() {
        return readerName;
    }

    public void setReaderName(String readerName) {
        this.readerName = readerName;
    }

    public String getReaderId() {
        return readerId;
    }

    public void setReaderId(String readerId) {
        this.readerId = readerId;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }
}
