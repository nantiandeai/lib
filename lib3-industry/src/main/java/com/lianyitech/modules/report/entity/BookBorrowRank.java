package com.lianyitech.modules.report.entity;

/**
 * 图书借阅排行
 */
public class BookBorrowRank extends CommonEntity<BookBorrowRank>{
    private static final long serialVersionUID = 1L;
    /**
     * 题名
     */
    private String title;
    /**
     * 著者
     */
    private String author;
    /**
     * 出版社
     */
    private String publishingName;
    /**
     * 出版时间
     */
    private String publishingTime;
    /**
     * 借阅册次
     */
    private Integer borrowNum;

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

    public Integer getBorrowNum() {
        return borrowNum;
    }

    public void setBorrowNum(Integer borrowNum) {
        this.borrowNum = borrowNum;
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
