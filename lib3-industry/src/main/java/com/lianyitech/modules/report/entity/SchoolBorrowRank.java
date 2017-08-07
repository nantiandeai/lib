package com.lianyitech.modules.report.entity;

/**
 * 学校借阅排行榜实体
 * Created by zcx on 2016/11/4
 */
public class SchoolBorrowRank extends CommonEntity<SchoolBorrowRank>{
    private static final long serialVersionUID = 1L;
    /**
     * 借阅排名
     */
    private String ranks;
    /**
     * 学校名称
     */
    private String schoolName;
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
    /**
     * 学校类别
     */
   private String schoolType;
    public String getRanks() {
        return ranks;
    }

    public void setRanks(String ranks) {
        this.ranks = ranks;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
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


    public String getSchoolType() {
        return schoolType;
    }

    public void setSchoolType(String schoolType) {
        this.schoolType = schoolType;
    }

}
