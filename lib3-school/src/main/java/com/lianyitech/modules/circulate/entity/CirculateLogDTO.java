package com.lianyitech.modules.circulate.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.lianyitech.core.enmu.EnumCirculateLogType;
import com.lianyitech.core.utils.CustomDoubleSerialize;
import com.lianyitech.core.utils.DataEntity;
import com.lianyitech.core.utils.SystemUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 所有流通记录模型
 * Created by chenxiaoding on 2016/9/13.
 */
public class CirculateLogDTO extends DataEntity<CirculateLogDTO> {

    private String readerCardId;//读者证号
    private String name;//读者姓名
    private String barcode;//条形码
    private String title;//题名
    private Double price;// 定价
    private String author;//作者
    private String collectName;//地点所在地
    private Date borrowDate;//借书日期
    private Date shouldReturnDate;//应还日期
    private Date returnDate;//还书日期
    private String status;//操作行为
    private String statusName;//操作对应名
    private Date actionDate;//操作日期
    private String groupId;//读者组织ID
    private String groupName;//读者组织名称
    private String billId;//加了一个billid
    private String logType;//操作行为--操作日志表的操作行为

    private String librarsortCode;
    private String tanejiNo;
    private String assNo;
    private String indexNum;
    private Date reserveDate;
    private Date reserveLoseDate;

    private String hasUse;//有效，还是无效条件
    private String nameId;//证号/姓名
    private String barcodeTitle;//条形码/题名
    private Date beginDate;//开始操作间
    private Date endDate;//结束时间
    private Integer pastDay;//超期天数
    private String orgId;//机构id（所有查询都跟机构id关联）
    private String card;//读者证号
    private String keyWord;//关键字混合查

    private String keyWords;
    /**
     * 读者类型
     */
    private String readerType;
    /**
     * 开始借书日期范围
     */
    private Date beginBorrowDate;
    /**
     * 结束借书日期范围
     */
    private Date endBorrowDate;
    /**
     * 开始应还日期范围
     */
    private Date beginShouldReturnDate;
    /**
     * 结束应还日期范围
     */
    private Date endShouldReturnDate;
    /**
     * 是否超期
     */
    private String pastStatus;

    /**
     * 流通类型 0图片 1期刊
     */
    private String dirType;


    /**
     * 来源 1:离线客户端
     */
    private String source;

    public String getPastStatus() {
        return pastStatus;
    }

    public void setPastStatus(String pastStatus) {
        this.pastStatus = pastStatus;
    }

    public String getReaderType() {
        return readerType;
    }

    public void setReaderType(String readerType) {
        this.readerType = readerType;
    }

    public Date getBeginShouldReturnDate() {
        return beginShouldReturnDate;
    }

    public void setBeginShouldReturnDate(Date beginShouldReturnDate) {
        this.beginShouldReturnDate = beginShouldReturnDate;
    }

    public Date getEndShouldReturnDate() {
        return endShouldReturnDate;
    }

    public void setEndShouldReturnDate(Date endShouldReturnDate) {
        this.endShouldReturnDate = endShouldReturnDate;
    }

    public Date getBeginBorrowDate() {
        return beginBorrowDate;
    }

    public void setBeginBorrowDate(Date beginBorrowDate) {
        this.beginBorrowDate = beginBorrowDate;
    }

    public Date getEndBorrowDate() {
        return endBorrowDate;
    }

    public void setEndBorrowDate(Date endBorrowDate) {
        this.endBorrowDate = endBorrowDate;
    }

    public String getKeyWords() {
        return keyWords;
    }

    public void setKeyWords(String keyWords) {
        this.keyWords = keyWords;
    }
    public String getCollectName() {
        return collectName;
    }

    public void setCollectName(String collectName) {
        this.collectName = collectName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public String getHasUse() {
        return hasUse;
    }

    public void setHasUse(String hasUse) {
        this.hasUse = hasUse;
    }

    public String getIndexNum() {
        return SystemUtils.getIndexNum(librarsortCode,tanejiNo,assNo);
    }

    public void setIndexNum(String indexNum) {
        this.indexNum = indexNum;
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

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    public Date getReserveDate() {
        return reserveDate;
    }

    public void setReserveDate(Date reserveDate) {
        this.reserveDate = reserveDate;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    public Date getReserveLoseDate() {
        return reserveLoseDate;
    }

    public void setReserveLoseDate(Date reserveLoseDate) {
        this.reserveLoseDate = reserveLoseDate;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
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

    public String getStatusName() {
        if(getStatus()!=null) {
            return EnumCirculateLogType.parse(getStatus().trim()).getName();
        }else {
            return null;
        }
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getNameId() {
        return nameId;
    }

    public void setNameId(String nameId) {
        this.nameId = nameId;
    }

    public String getBarcodeTitle() {
        return barcodeTitle;
    }

    public void setBarcodeTitle(String barcodeTitle) {
        this.barcodeTitle = barcodeTitle;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        try {
            if(endDate!=null){//查询结束日期当天的数据所以进行下面转换
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String endDateStr = sdf.format(endDate);
                sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                this.endDate = sdf.parse(endDateStr+" 23:59:59");
            }
        } catch (ParseException e) {
            e.printStackTrace();
            this.endDate = endDate;
        }
    }

    public CirculateLogDTO() {
    }

    @Override
    public String toString() {
        return "CirculateLogDTO{" +
                "readerCardId='" + readerCardId + '\'' +
                ", name='" + name + '\'' +
                ", barcode='" + barcode + '\'' +
                ", title='" + title + '\'' +
                ", borrowDate=" + borrowDate +
                ", shouldReturnDate=" + shouldReturnDate +
                ", returnDate=" + returnDate +
                ", status='" + status + '\'' +
                ", statusName='" + statusName + '\'' +
                ", actionDate=" + actionDate +
                ", groupId='" + groupId + '\'' +
                ", billId='" + billId + '\'' +
                ", nameId='" + nameId + '\'' +
                ", barcodeTitle='" + barcodeTitle + '\'' +
                ", beginDate=" + beginDate +
                ", endDate=" + endDate +
                ", pastDay=" + pastDay +
                '}';
    }

    public String getReaderCardId() {
        return readerCardId;
    }

    public void setReaderCardId(String readerCardId) {
        this.readerCardId = readerCardId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    public Date getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(Date borrowDate) {
        this.borrowDate = borrowDate;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    public Date getShouldReturnDate() {
        return shouldReturnDate;
    }

    public void setShouldReturnDate(Date shouldReturnDate) {
        this.shouldReturnDate = shouldReturnDate;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    public Date getActionDate() {
        return getUpdateDate();
    }

    public void setActionDate(Date actionDate) {
        this.actionDate = actionDate;
    }
    public String getLogType() {
        return logType;
    }

    public void setLogType(String logType) {
        this.logType = logType;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }
    @JsonSerialize(using = CustomDoubleSerialize.class)
    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @JsonIgnore
    public String getDirType() {
        return dirType;
    }

    public void setDirType(String dirType) {
        this.dirType = dirType;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
