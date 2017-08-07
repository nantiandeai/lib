package com.lianyitech.modules.report.entity;


public class Statements extends CommonEntity<Statements> {

    private String barCode;    //条形码
    private String title;      //题名
    private String  author;     //著者
    private String publishingName;//出版社
    private String publishingTime;//出版时间
    private int   Borrowing  ;       //借阅次数
    private int    renew    ;          //续借次数
    private int     subscribe;        //预约次数
    private  int     orderBorrow;         //预借次数
    private int    exceed;               //超期次数
    private String  certNum ;         //读者证号
    private String  name;            //读者姓名
    private String  readerType ;    //读者类型
    private String schoolCode ;   //学校编码
    private String dateId;         //时间ID

    public String getDateId() {
        return dateId;
    }

    public void setDateId(String dateId) {
        this.dateId = dateId;
    }

    public String getSchoolCode() {
        return schoolCode;
    }

    public void setSchoolCode(String schoolCode) {
        this.schoolCode = schoolCode;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarcode(String barCode) {
        this.barCode = barCode;
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

    public int getBorrowing() {
        return Borrowing;
    }

    public void setBorrowing(int borrowing) {
        Borrowing = borrowing;
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
