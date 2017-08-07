package com.lianyitech.modules.report.entity;

import com.lianyitech.modules.common.ReportCommon;

/**
 * 藏书有关统计报表（藏书量统计和生均及增量统计）
 *2016/10/29.
 */
public class BookReport extends CommonEntity<BookReport>{
    private static final long serialVersionUID = 1L;
    /**
     * 藏书种类
     */
    private Integer bookSpecies=0;
    /**
     * 藏书册数
     */
    private Integer bookNum=0;
    /**
     * 藏书金额
     */
    private Double booksAmount=0.00;
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

    //生均及增量统计--------公用实体类
    /**
     * 新增册数
     */
    private Integer addBookNum=0;
    /**
     * 学生人数
     */
    private Integer studentNum=0;
    /**
     * 生均册数 计算放到实体类里面进行计算
     */
    private String stuBookNum;
    /**
     * 生均年递增长 计算在实体类里面进行计算
     */
    private String stuAddBookNum;

    public Integer getBookSpecies() {
        return bookSpecies;
    }

    public void setBookSpecies(Integer bookSpecies) {
        this.bookSpecies = bookSpecies;
    }

    public Integer getBookNum() {
        return bookNum;
    }

    public void setBookNum(Integer bookNum) {
        this.bookNum = bookNum;
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

    public Integer getAddBookNum() {
        return addBookNum;
    }

    public void setAddBookNum(Integer addBookNum) {
        this.addBookNum = addBookNum;
    }

    public Integer getStudentNum() {
        return studentNum;
    }

    public void setStudentNum(Integer studentNum) {
        this.studentNum = studentNum;
    }

    public String getStuBookNum() {
        return ReportCommon.comdivision(bookNum,studentNum);
    }

    public void setStuBookNum(String stuBookNum) {
        this.stuBookNum = stuBookNum;
    }

    public String getStuAddBookNum() {
       return ReportCommon.comdivision(addBookNum,studentNum);// b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public void setStuAddBookNum(String stuAddBookNum) {
        this.stuAddBookNum = stuAddBookNum;//stuAddBookNum/studentNum
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
