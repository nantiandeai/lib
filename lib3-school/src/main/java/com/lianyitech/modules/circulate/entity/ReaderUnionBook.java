package com.lianyitech.modules.circulate.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lianyitech.core.enmu.EnumLibStoreStatus;
import com.lianyitech.core.utils.SystemUtils;

import java.util.Date;

/**
 * 读者所借书的集合
 * Created by chenxiaoding on 2016/9/12.
 */
public class ReaderUnionBook {

    private String billId;
    private String barcode;//条形码billDetail
    private String title;//题名
    private String subTitle;//副标题
    private String indexNum;//索书号
    private Date borrowDate;//借书日期
    private Date returnDate;//还书日期
    private Date shouldReturnDate;//应还日期
    private String isOrder;		// 是否预约 0否 1是
    private String isRenew;		// 是否续借 0否 1是
    private String librarsortCode;//分类号
    private String tanejiNo;//种次号
    private String assNo;//辅助区分号
    private Integer pastDay;//超期天数
    private String dirType="0";//借阅书目类型 0图片 1期刊

    public ReaderUnionBook() {
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

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIndexNum() {
        return SystemUtils.getIndexNum(getLibrarsortCode(),getTanejiNo(),getAssNo());
    }

    public void setIndexNum(String indexNum) {
        this.indexNum = indexNum;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    public Date getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(Date borrowDate) {
        this.borrowDate = borrowDate;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    public Date getShouldReturnDate() {
        return shouldReturnDate;
    }

    public void setShouldReturnDate(Date shouldReturnDate) {
        this.shouldReturnDate = shouldReturnDate;
    }

    public String getIsOrder() {
        return isOrder;
    }

    public void setIsOrder(String isOrder) {
        this.isOrder = isOrder;
    }

    public String getIsRenew() {
        return isRenew;
    }

    public void setIsRenew(String isRenew) {
        this.isRenew = isRenew;
    }

    public Integer getPastDay() {
        return pastDay;
    }

    public void setPastDay(Integer pastDay) {
        this.pastDay = pastDay;
    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getDirType() {
        return dirType;
    }

    public void setDirType(String dirType) {
        this.dirType = dirType;
    }
}
