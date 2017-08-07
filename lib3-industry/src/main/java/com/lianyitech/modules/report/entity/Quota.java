package com.lianyitech.modules.report.entity;

import com.lianyitech.core.utils.DataEntity;

/**
 * Created by jordan jiang on 2017/3/23.
 */
public class Quota extends CommonEntity<Quota> {
    //学生人数
    private Integer studentNum = 0;
    //学生借阅册数
    private Integer stuBorrowNum = 0;
    //借阅人数
    private Integer borrowerNum = 0;
    //生均借阅册数
    private String stuBorrowAverNum;
    //生均册数
    private String stuAverNum;
    //藏数总册数
    private Integer bookNum;
    //藏数种类
    private Integer bookSpecies;
    //藏数总金额
    private Double booksAmount;
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

    public Integer getStudentNum() {
        return studentNum;
    }

    public void setStudentNum(Integer studentNum) {
        this.studentNum = studentNum;
    }

    public Integer getStuBorrowNum() {
        return stuBorrowNum;
    }

    public void setStuBorrowNum(Integer stuBorrowNum) {
        this.stuBorrowNum = stuBorrowNum;
    }

    public Integer getBorrowerNum() {
        return borrowerNum;
    }

    public void setBorrowerNum(Integer borrowerNum) {
        this.borrowerNum = borrowerNum;
    }

    public String getStuBorrowAverNum() {
        return stuBorrowAverNum;
    }

    public void setStuBorrowAverNum(String stuBorrowAverNum) {
        this.stuBorrowAverNum = stuBorrowAverNum;
    }

    public String getStuAverNum() {
        return stuAverNum;
    }

    public void setStuAverNum(String stuAverNum) {
        this.stuAverNum = stuAverNum;
    }

    public Integer getBookNum() {
        return bookNum;
    }

    public void setBookNum(Integer bookNum) {
        this.bookNum = bookNum;
    }

    public Integer getBookSpecies() {
        return bookSpecies;
    }

    public void setBookSpecies(Integer bookSpecies) {
        this.bookSpecies = bookSpecies;
    }

    public Double getBooksAmount() {
        return booksAmount;
    }

    public void setBooksAmount(Double booksAmount) {
        this.booksAmount = booksAmount;
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
