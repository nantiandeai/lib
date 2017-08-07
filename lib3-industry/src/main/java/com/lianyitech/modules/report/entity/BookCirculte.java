package com.lianyitech.modules.report.entity;

import com.lianyitech.modules.common.ReportCommon;

import java.util.Comparator;

/**
 * 藏书流通有关统计报表（生均借阅统计和读者借阅率统计、图书流通率统计）
 *2016/11/01.
 */
public class BookCirculte extends CommonEntity<BookCirculte>{
    private static final long serialVersionUID = 1L;
    //生均借阅统计
    /**
     * 学生人数--读者借阅率统计里面的读者人数公用这个属性（为了实体类属性尽量少点）
     */
    private Integer studentNum = 0;
    /**
     * 学生借阅册数 读者借阅率统计里面的借阅人数公用这个属性 借阅册数也公用这个属性（为了实体类属性尽量少点）
     */
    private Integer stuBorrowNum = 0;


    //图书流通率统计
    /**
     * 藏书册数
     */
    private Integer bookNum = 0;
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
     * 生均借阅册数
     */
    private String stuAverRate;
    /**
     * 借阅率
     */
    private String averRate;//这里需要带百分号
    /**
     * 流通率
     */
    private String circulationRate;

    /**
     * 首页流通率,因为不能公用
     */
    private String circulateRate;

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

    public Integer getBookNum() {
        return bookNum;
    }

    public void setBookNum(Integer bookNum) {
        this.bookNum = bookNum;
    }

    public String getStuAverRate() {
        return ReportCommon.comdivision(stuBorrowNum,studentNum);
    }

    public void setStuAverRate(String stuAverRate) {
        this.stuAverRate = stuAverRate;
    }

    public String getAverRate() {
        return ReportCommon.comdivision(stuBorrowNum!=null?stuBorrowNum*100:0,studentNum)+"%";
    }

    public void setAverRate(String averRate) {
        this.averRate = averRate;
    }

    public String getCirculationRate() {
        return ReportCommon.comdivision(stuBorrowNum!=null?stuBorrowNum*100:0,bookNum)+"%";
    }

    public void setCirculationRate(String circulationRate) {
        this.circulationRate = circulationRate;
    }

    public String getCirculateRate() {
        return ReportCommon.comdivision(stuBorrowNum!=null?stuBorrowNum*100:0,bookNum);
    }

    public void setCirculateRate(String circulateRate) {
        this.circulateRate = circulateRate;
    }

//    @Override
//    public int compareTo(BookCirculte o) {
//        int c= Integer.parseInt(o2.getMonth());
//        int c1= Integer.parseInt(o2.getMonth());
//        return 0;
//    }
}
