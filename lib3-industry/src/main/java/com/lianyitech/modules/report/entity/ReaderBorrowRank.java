package com.lianyitech.modules.report.entity;

import com.lianyitech.core.enmu.EnumReaderType;

/**
 * 读者借阅排行榜实体
 * Created by zcx on 2016/11/3
 */
public class ReaderBorrowRank extends CommonEntity<ReaderBorrowRank>{
    private static final long serialVersionUID = 1L;
    /**
     * 读者姓名
     */
    private String readerName;
    /**
     * 读者类型
     */
    private String readerType;

    private  String readerTypeName;//读者类型名称
    /**
     * 学校名称
     */
    private String schoolName;
    /**
     * 读者组织
     */
    private String groupName;
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
     * 借阅排名
     */

    private String ranks;

    public String getReaderName() {
        return readerName;
    }

    public void setReaderName(String readerName) {
        this.readerName = readerName;
    }

    public String getReaderType() {
        return readerType;
    }

    public void setReaderType(String readerType) {
        this.readerType = readerType;
    }

    public String getReaderTypeName() {
        if(EnumReaderType.parse(readerType)!=null){
            return EnumReaderType.parse(readerType).getName();
        }
        return readerTypeName;
    }

    public void setReaderTypeName(String readerTypeName) {
        this.readerTypeName = readerTypeName;
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

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getRanks() {
        return ranks;
    }

    public void setRanks(String ranks) {
        this.ranks = ranks;
    }

}
