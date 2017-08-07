package com.lianyitech.modules.report.entity;


import com.lianyitech.common.utils.StringUtils;
import com.lianyitech.core.enmu.EnumReaderType;
import com.lianyitech.core.utils.DataEntity;

public class Statements extends DataEntity<Statements> {

    private String orgId ;      //单位ID
    private String barcode;    //条形码
    private String title;      //题名
    private String  author;     //著者
    private String publishingName;//出版社
    private String publishingTime;//出版时间
    private int   borrow  ;       //借阅次数
    private int    renew    ;          //续借次数
    private int     subscribe;        //预约次数
    private  int     orderBorrow;         //预借次数
    private int    exceed;               //超期次数
    private String  certNum ;         //读者证号
    private String  name;            //读者姓名
    private String  readerType ;    //读者类型
    private String  readerTypeName;  //读者类型名称
    private String beginTime;         // 开始时间
    private String endTime;           //结束时间
    private String groupName;         //组织名称
    private String orderBy;
    private int compenCount;       //罚赔次数
    private String groupType;       //组织类型

    private String dirType; //书目类型，0图书 1期刊
    private String dirName;//书目中文

    public String getDirName() {
        return dirName;
    }

    public void setDirName(String dirName) {
        this.dirName = dirName;
    }

    public String getDirType() {
        return dirType;
    }

    public void setDirType(String dirType) {
        this.dirType = dirType;
    }

    public int getCompenCount() {
        return compenCount;
    }

    public void setCompenCount(int compenCount) {
        this.compenCount = compenCount;
    }

    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getReaderTypeName() {
        if(StringUtils.isNotEmpty(readerType)){
            readerTypeName= EnumReaderType.parse(readerType).getName();
        }
        return readerTypeName;
    }

    public void setReaderTypeName(String readerTypeName) {
        this.readerTypeName = readerTypeName;
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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublishingName() {
        return publishingName;
    }

    public void setPublishingName(String publishingName) {
        this.publishingName = publishingName;
    }

    public String getPublishingTime() {
        return publishingTime;
    }

    public void setPublishingTime(String publishingTime) {
        this.publishingTime = publishingTime;
    }

    public int getBorrow() {
        return borrow;
    }

    public void setBorrow(int borrow) {
        this.borrow = borrow;
    }

    public int getRenew() {
        return renew;
    }

    public void setRenew(int renew) {
        this.renew = renew;
    }

    public int getSubscribe() {
        return subscribe;
    }

    public void setSubscribe(int subscribe) {
        this.subscribe = subscribe;
    }

    public int getOrderBorrow() {
        return orderBorrow;
    }

    public void setOrderBorrow(int orderBorrow) {
        this.orderBorrow = orderBorrow;
    }

    public int getExceed() {
        return exceed;
    }

    public void setExceed(int exceed) {
        this.exceed = exceed;
    }

    public String getCertNum() {
        return certNum;
    }

    public void setCertNum(String certNum) {
        this.certNum = certNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReaderType() {
        return readerType;
    }

    public void setReaderType(String readerType) {
        this.readerType = readerType;
    }
}
