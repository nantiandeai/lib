/**
 *
 */
package com.lianyitech.modules.circulate.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.lianyitech.core.enmu.EnumLibStoreStatus;
import com.lianyitech.core.enmu.EnumReaderType;
import com.lianyitech.core.utils.CustomDoubleSerialize;
import com.lianyitech.core.utils.DataEntity;
import com.lianyitech.modules.sys.utils.UserUtils;

/**
 * 借阅规则管理Entity
 *
 * @author zengzy
 * @version 2016-09-07
 */
public class Rule extends DataEntity<Rule> {


    private static final long serialVersionUID = 1L;
    private String readerType;        // 读者类型
    private String ruleName;        // 借阅规则名称
    private Integer borrowDays;        // 借阅天数
    private Integer borrowNumber;        // 借阅册数
    private Integer renewDays;        // 续借天数
    private Integer renewNumber;        // 续借次数
    private Integer bespeakingDays;        // 预约天数
    private Integer bespeakingNumber;        // 预约册数
    private Integer bespokeDays;        // 预借天数
    private Integer bespokeNumber;        // 预借册数
    private String exceedLimit;        // 超期可借  0否 1是
    private Integer shortBorrowDays; //最短借阅天数
    private String  exceedFine; //超期是否罚款 0否 1是
    private Double  exceedFineDayAmount; //超期每天罚金
    private Double  exceedFineMaxAmount; //超期最大罚金
    private String exceedAutoStopBorrow; //超期是否自动停借 0否 1是
    private Integer exceedAutoStopDays; //超期自动停借天数
    private String lossFine; //丢书是否罚款 0否 1是
    private Double lossFineMulti; //丢书罚款倍率
    private String  stainFine; //污损是否罚款 0否 1是
    private Double stainFineMulti;//污损罚款倍率
    private String readerTypeName;        // 读者类型名称
    private String orgId;          //机构id

    public Rule() {
        super();
    }

    public Rule(String id) {
        super(id);
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public void setBorrowDays(Integer borrowDays) {
        this.borrowDays = borrowDays;
    }

    public Integer getBorrowNumber() {
        return borrowNumber;
    }

    public void setBorrowNumber(Integer borrowNumber) {
        this.borrowNumber = borrowNumber;
    }

    public Integer getRenewDays() {
        return renewDays;
    }

    public void setRenewDays(Integer renewDays) {
        this.renewDays = renewDays;
    }

    public Integer getRenewNumber() {
        return renewNumber;
    }

    public void setRenewNumber(Integer renewNumber) {
        this.renewNumber = renewNumber;
    }

    public Integer getBespeakingDays() {
        return bespeakingDays;
    }

    public void setBespeakingDays(Integer bespeakingDays) {
        this.bespeakingDays = bespeakingDays;
    }

    public Integer getBespeakingNumber() {
        return bespeakingNumber;
    }

    public void setBespeakingNumber(Integer bespeakingNumber) {
        this.bespeakingNumber = bespeakingNumber;
    }

    public Integer getBespokeDays() {
        return bespokeDays;
    }

    public void setBespokeDays(Integer bespokeDays) {
        this.bespokeDays = bespokeDays;
    }

    public Integer getBespokeNumber() {
        return bespokeNumber;
    }

    public void setBespokeNumber(Integer bespokeNumber) {
        this.bespokeNumber = bespokeNumber;
    }

    public String getExceedLimit() {
        return exceedLimit;
    }

    public void setExceedLimit(String exceedLimit) {
        this.exceedLimit = exceedLimit;
    }

    public Integer getBorrowDays() {
        return borrowDays;
    }

    public String getReaderType() {
        return readerType;
    }

    public void setReaderType(String readerType) {
        this.readerType = readerType;
    }

    public Integer getShortBorrowDays() {
        return shortBorrowDays;
    }

    public void setShortBorrowDays(Integer shortBorrowDays) {
        this.shortBorrowDays = shortBorrowDays;
    }

    public String getExceedFine() {
        return exceedFine;
    }

    public void setExceedFine(String exceedFine) {
        this.exceedFine = exceedFine;
    }

    @JsonSerialize(using = CustomDoubleSerialize.class)
    public Double getExceedFineDayAmount() {
        return exceedFineDayAmount;
    }

    public void setExceedFineDayAmount(Double exceedFineDayAmount) {
        this.exceedFineDayAmount = exceedFineDayAmount;
    }

    @JsonSerialize(using = CustomDoubleSerialize.class)
    public Double getExceedFineMaxAmount() {
        return exceedFineMaxAmount;
    }

    public void setExceedFineMaxAmount(Double exceedFineMaxAmount) {
        this.exceedFineMaxAmount = exceedFineMaxAmount;
    }

    public String getExceedAutoStopBorrow() {
        return exceedAutoStopBorrow;
    }

    public void setExceedAutoStopBorrow(String exceedAutoStopBorrow) {
        this.exceedAutoStopBorrow = exceedAutoStopBorrow;
    }

    public Integer getExceedAutoStopDays() {
        return exceedAutoStopDays;
    }

    public void setExceedAutoStopDays(Integer exceedAutoStopDays) {
        this.exceedAutoStopDays = exceedAutoStopDays;
    }

    public String getLossFine() {
        return lossFine;
    }

    public void setLossFine(String lossFine) {
        this.lossFine = lossFine;
    }

    public Double getLossFineMulti() {
        return lossFineMulti;
    }

    public void setLossFineMulti(Double lossFineMulti) {
        this.lossFineMulti = lossFineMulti;
    }

    public String getStainFine() {
        return stainFine;
    }

    public void setStainFine(String stainFine) {
        this.stainFine = stainFine;
    }


    public Double getStainFineMulti() {
        return stainFineMulti;
    }

    public void setStainFineMulti(Double stainFineMulti) {
        this.stainFineMulti = stainFineMulti;
    }

    public String getReaderTypeName() {
        return EnumReaderType.parse(this.getReaderType()).getName();
    }

    public void setReaderTypeName(String readerTypeName) {
        this.readerTypeName = readerTypeName;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }
}