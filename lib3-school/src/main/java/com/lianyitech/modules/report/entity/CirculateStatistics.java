package com.lianyitech.modules.report.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.lianyitech.common.utils.StringUtils;
import com.lianyitech.core.utils.CustomDoubleSerialize;
import com.lianyitech.core.utils.DataEntity;
import com.lianyitech.modules.common.ReportCommon;

import java.text.DecimalFormat;

/**
 * 流通率DTO
 * Created by chenxiaoding on 2016/11/4.
 */
public class CirculateStatistics extends DataEntity<CirculateStatistics> {

    private static final long serialVersionUID = 1L;
    private String classCode;//22大类分类号
    private String className;//22大类分类名
    private Integer bookNum;//藏书册数
    private Integer bookSpecies;//藏书种数
    private Integer borrowNum;//借阅次数
    private String circulateRate;//流通率

    private Integer totalBookNum;//合计的总藏书
    private Integer totalBorrowNum;//合计的借阅次数
    private String totalCirculateRate;//合计的流通率

    //五大部类，二十二大类 分布结构所需字段
    private String fiveClass;//五大类
    private String fiveName;//五大类名称
    private Double classPrice;//二十二大类每类钱
    private String speciesRate;//种类占比
    private String bookNumRate;//册数占比
    private String priceRate;//金额占比
    private String totalSpeciesRate = "0.00%";//合计种类占比
    private String totalBookNumRate = "0.00%";//合计册数占比
    private String totalpriceRate = "0.00%";//合计金额占比
    private Integer totalSpecies = 0;//合计种数
    private Double totalPrice = 0d;//合计金额
    private String dirType;   //流通类型 0图书1期刊


    DecimalFormat formater = new DecimalFormat("###0.00");
    public String getFiveClass() {
        return fiveClass;
    }

    public void setFiveClass(String fiveClass) {
        this.fiveClass = fiveClass;
    }

    public String getFiveName() {
        return fiveName;
    }

    public void setFiveName(String fiveName) {
        this.fiveName = fiveName;
    }
    @JsonSerialize(using = CustomDoubleSerialize.class)
    public Double getClassPrice() {
        return classPrice;
    }

    public void setClassPrice(Double classPrice) {
        this.classPrice = classPrice;
    }

    public String getSpeciesRate() {
        if(totalSpecies != null &&  totalSpecies>0 &&bookSpecies!=null && bookSpecies>0){
            Double d = ReportCommon.Comdivision(bookSpecies*100,totalSpecies);
            return formater.format(d)+"%";
        }else if(StringUtils.isEmpty(speciesRate)){
            return "0.00%";
        } else {
            return speciesRate;
        }
    }

    public void setSpeciesRate(String speciesRate) {
        this.speciesRate = speciesRate;
    }

    public String getBookNumRate() {
        if(totalBookNum!=null && totalBookNum>0 &&bookNum!=null && bookNum>0){
            Double  d = ReportCommon.Comdivision(bookNum*100,totalBookNum);
            return formater.format(d)+"%";
        }else if(StringUtils.isEmpty(bookNumRate)){
            return "0.00%";
        } else {
            return bookNumRate;
        }
    }

    public void setBookNumRate(String bookNumRate) {
        this.bookNumRate = bookNumRate;
    }

    public String getPriceRate() {
        if(totalPrice!=null && totalPrice>0 &&classPrice!=null && classPrice>0){
            Double d = ReportCommon.Comdivision(classPrice*100,totalPrice);
            return formater.format(d)+"%";
        }else if(StringUtils.isEmpty(priceRate)) {
            return "0.00%";
        }else {
            return priceRate;
        }
    }

    public void setPriceRate(String priceRate) {
        this.priceRate = priceRate;
    }
    @JsonIgnore
    public String getTotalSpeciesRate() {
        return totalSpeciesRate;
    }


    public void setTotalSpeciesRate(String totalSpeciesRate) {
        this.totalSpeciesRate = totalSpeciesRate;
    }
    @JsonIgnore
    public String getTotalBookNumRate() {
        return totalBookNumRate;
    }

    public void setTotalBookNumRate(String totalBookNumRate) {
        this.totalBookNumRate = totalBookNumRate;
    }
    @JsonIgnore
    public String getTotalpriceRate() {
        return totalpriceRate;
    }

    public void setTotalpriceRate(String totalpriceRate) {
        this.totalpriceRate = totalpriceRate;
    }
    @JsonIgnore
    public Integer getTotalSpecies() {
        return totalSpecies;
    }

    public void setTotalSpecies(Integer totalSpecies) {
        this.totalSpecies = totalSpecies;
    }

    @JsonIgnore
    @JsonSerialize(using = CustomDoubleSerialize.class)
    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    @JsonIgnore
    public Integer getTotalBookNum() {
        return totalBookNum;
    }

    public void setTotalBookNum(Integer totalBookNum) {
        this.totalBookNum = totalBookNum;
    }
    @JsonIgnore
    public Integer getTotalBorrowNum() {
        return totalBorrowNum;
    }

    public void setTotalBorrowNum(Integer totalBorrowNum) {
        this.totalBorrowNum = totalBorrowNum;
    }
    @JsonIgnore
    public String getTotalCirculateRate() {
        if(totalBookNum!=null && totalBookNum>0 &&totalBorrowNum!=null && totalBorrowNum>0) {
            Double d = ReportCommon.Comdivision(totalBorrowNum*100, totalBookNum);

            return formater.format(d)+"%";
        }else if (StringUtils.isEmpty(totalCirculateRate)){
            return "0.00%";
        }else {
            return totalCirculateRate;
        }
    }

    public void setTotalCirculateRate(String totalCirculateRate) {
        this.totalCirculateRate = totalCirculateRate;
    }

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
        if(bookNum!=null && bookNum>0 &&borrowNum!=null && borrowNum>0) {
            Double d = ReportCommon.Comdivision(borrowNum*100, bookNum);
            return formater.format(d)+"%";
        }else if (StringUtils.isEmpty(circulateRate)){
            return "0.00%";
        }else {
            return circulateRate;
        }

    }

    public void setCirculateRate(String circulateRate) {
        this.circulateRate = circulateRate;
    }

    public String getDirType() {
        return dirType;
    }

    public void setDirType(String dirType) {
        this.dirType = dirType;
    }

    public CirculateStatistics() {
    }

    @Override
    public String toString() {
        return "CirculateStatistics{" +
                "totalPrice=" + totalPrice +
                ", classCode='" + classCode + '\'' +
                ", className='" + className + '\'' +
                ", bookNum=" + bookNum +
                ", bookSpecies=" + bookSpecies +
                ", borrowNum=" + borrowNum +
                ", circulateRate='" + circulateRate + '\'' +
                ", totalBookNum=" + totalBookNum +
                ", totalBorrowNum=" + totalBorrowNum +
                ", totalCirculateRate='" + totalCirculateRate + '\'' +
                ", fiveClass='" + fiveClass + '\'' +
                ", fiveName='" + fiveName + '\'' +
                ", classPrice=" + classPrice +
                ", speciesRate='" + speciesRate + '\'' +
                ", bookNumRate='" + bookNumRate + '\'' +
                ", priceRate='" + priceRate + '\'' +
                ", totalSpeciesRate='" + totalSpeciesRate + '\'' +
                ", totalBookNumRate='" + totalBookNumRate + '\'' +
                ", totalpriceRate='" + totalpriceRate + '\'' +
                ", totalSpecies=" + totalSpecies +
                '}';
    }

    public CirculateStatistics(String classCode, String className, Integer bookNum, Integer bookSpecies, Integer borrowNum, String circulateRate, Integer totalBookNum, Integer totalBorrowNum, String totalCirculateRate, String fiveClass, String fiveName, Double classPrice, String speciesRate, String bookNumRate, String priceRate, String totalSpeciesRate, String totalBookNumRate, String totalpriceRate, Integer totalSpecies, Double totalPrice) {
        this.classCode = classCode;
        this.className = className;
        this.bookNum = bookNum;
        this.bookSpecies = bookSpecies;
        this.borrowNum = borrowNum;
        this.circulateRate = circulateRate;
        this.totalBookNum = totalBookNum;
        this.totalBorrowNum = totalBorrowNum;
        this.totalCirculateRate = totalCirculateRate;
        this.fiveClass = fiveClass;
        this.fiveName = fiveName;
        this.classPrice = classPrice;
        this.speciesRate = speciesRate;
        this.bookNumRate = bookNumRate;
        this.priceRate = priceRate;
        this.totalSpeciesRate = totalSpeciesRate;
        this.totalBookNumRate = totalBookNumRate;
        this.totalpriceRate = totalpriceRate;
        this.totalSpecies = totalSpecies;
        this.totalPrice = totalPrice;
    }
}
