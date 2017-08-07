package com.lianyitech.modules.report.entity;

/**
 * 藏书分类统计
 * Created by luzhihuai on 2016/11/10.1
 */
public class LibraryAssort extends CommonEntity<LibraryAssort> {
    private String majorClassesCode;//五大部类编号
    private String majorClassesName;//五大部类名称
    private String smallClassesName;//22个基本类名称
    private String smallClassesCode;//22个基本类编号
    private Integer bookSpecies;//藏书种类
    private String bookSpeciesRate;//种类占比
    private Integer bookNum;//藏书册数
    private String bookNumRate;//册数占比
    private Double booksAmount;//藏书金额
    private String booksAmountRate;//金额占比
    /**
     * 年
     */
    private String year;
    /**
     * 月
     */
    private String month;
    /**
     * 季
     */
    private String quarter;

    public String getMajorClassesCode() {
        return majorClassesCode;
    }

    public void setMajorClassesCode(String majorClassesCode) {
        this.majorClassesCode = majorClassesCode;
    }

    public String getMajorClassesName() {
        return majorClassesName;
    }

    public void setMajorClassesName(String majorClassesName) {
        this.majorClassesName = majorClassesName;
    }

    public String getSmallClassesName() {
        return smallClassesName;
    }

    public void setSmallClassesName(String smallClassesName) {
        this.smallClassesName = smallClassesName;
    }

    public String getSmallClassesCode() {
        return smallClassesCode;
    }

    public void setSmallClassesCode(String smallClassesCode) {
        this.smallClassesCode = smallClassesCode;
    }

    public Integer getBookSpecies() {
        return bookSpecies;
    }

    public void setBookSpecies(Integer bookSpecies) {
        this.bookSpecies = bookSpecies;
    }

    public void Integer(Integer bookSpecies) {
        this.bookSpecies = bookSpecies;
    }

    public String getBookSpeciesRate() {

        return bookSpeciesRate+"%";
    }

    public void setBookSpeciesRate(String bookSpeciesRate) {
        this.bookSpeciesRate = bookSpeciesRate;
    }

    public Integer getBookNum() {
        return bookNum;
    }

    public void setBookNum(Integer bookNum) {
        this.bookNum = bookNum;
    }

    public String getBookNumRate() {
        return bookNumRate+"%";
    }

    public void setBookNumRate(String bookNumRate) {
        this.bookNumRate = bookNumRate;
    }

    public Double getBooksAmount() {
        return booksAmount;
    }

    public void setBooksAmount(Double booksAmount) {
        this.booksAmount = booksAmount;
    }

    public String getBooksAmountRate() {
        return booksAmountRate+"%";
    }

    public void setBooksAmountRate(String booksAmountRate) {
        this.booksAmountRate = booksAmountRate;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getQuarter() {
        return quarter;
    }

    public void setQuarter(String quarter) {
        this.quarter = quarter;
    }
}
