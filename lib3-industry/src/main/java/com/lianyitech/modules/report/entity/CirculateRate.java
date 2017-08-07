package com.lianyitech.modules.report.entity;

import com.lianyitech.core.utils.DataEntity;
import com.lianyitech.modules.common.ReportCommon;

import java.util.DoubleSummaryStatistics;

/**
 * 流通率DTO
 * Created by chenxiaoding on 2016/11/4.
 */
public class CirculateRate extends DataEntity<CirculateRate> {


    private String classCode;//22大类分类号
    private String className;//22大类分类名
    private Integer bookNum;//藏书册数
    private Integer bookSpecies;//藏书种数
    private Integer borrowNum;//借阅次数
    private String circulateRate;//流通率

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
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

    public Integer getBorrowNum() {
        return borrowNum;
    }

    public void setBorrowNum(Integer borrowNum) {
        this.borrowNum = borrowNum;
    }

    public String getCirculateRate() {
        return ReportCommon.comdivision(borrowNum, bookNum);
    }

    public void setCirculateRate(String circulateRate) {
        this.circulateRate = circulateRate;
    }

    public CirculateRate(String classCode, String className, Integer bookNum, Integer bookSpecies, Integer borrowNum, String circulateRate) {
        this.classCode = classCode;
        this.className = className;
        this.bookNum = bookNum;
        this.bookSpecies = bookSpecies;
        this.borrowNum = borrowNum;
        this.circulateRate = circulateRate;
    }

    public CirculateRate() {
    }

    @Override
    public String toString() {
        return "CirculateRate{" +
                "classCode='" + classCode + '\'' +
                ", className='" + className + '\'' +
                ", bookNum=" + bookNum +
                ", bookSpecies=" + bookSpecies +
                ", borrowNum=" + borrowNum +
                ", circulateRate=" + circulateRate +
                '}';
    }
}
