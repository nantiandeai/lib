package com.lianyitech.modules.catalog.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lianyitech.common.utils.StringUtils;
import com.lianyitech.core.utils.DataEntity;
import com.lianyitech.core.utils.SystemUtils;

import java.util.Date;

/**
 * Created by zcx on 2017/5/18.
 * 书目查缺打印实体
 */
public class SupplyLable extends DataEntity<SupplyLable> {

    private static final long serialVersionUID = 1L;
    /**
     * 机构id
     */
    private String orgId;
    /**
     * 类型 0书目 1期刊
     */
    private String type;
    /**
     * 条形码
     */
    private String barcode;

    private String librarsortCode;//分类号librarsortCode
    private String tanejiNo;		// 种次号
    private String assNo;		// 辅助区分号
    private String bookNo;//著者号
    /**
     * 书次号
     */
    private String bookTimeNo;
    /**
     * 题名/刊名
     */
    private String titleName;
    /**
     * 入库日期
     */
    private Date putDate;
    /**
     * 导出方式  1：仅索书号 2：索书号+条形码
     */
    private String exportType = "1";

    private String indexnum;//索书号
    /**
     * 索刊号
     */
    private String somNo;

    public String getSomNo() {
        return SystemUtils.getSomNo(this.getLibrarsortCode(),this.getBookTimeNo(),this.getAssNo());
    }

    public void setSomNo(String somNo) {
        this.somNo = somNo;
    }

    public String getIndexnum() {
        return SystemUtils.getIndexNum1(this.getLibrarsortCode(),this.getTanejiNo(),this.getAssNo(),this.getBookNo());
    }

    public void setIndexnum(String indexnum) {
        this.indexnum = indexnum;
    }

    @JsonIgnore
    public String getExportType() {
        return exportType;
    }

    public void setExportType(String exportType) {
        this.exportType = exportType;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getTitleName() {
        return titleName;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }

    public Date getPutDate() {
        return putDate;
    }

    public void setPutDate(Date putDate) {
        this.putDate = putDate;
    }

    public String getLibrarsortCode() {
        return librarsortCode;
    }

    public void setLibrarsortCode(String librarsortCode) {
        this.librarsortCode = librarsortCode;
    }

    public String getTanejiNo() {
        return tanejiNo;
    }

    public void setTanejiNo(String tanejiNo) {
        this.tanejiNo = tanejiNo;
    }

    public String getAssNo() {
        return assNo;
    }

    public void setAssNo(String assNo) {
        this.assNo = assNo;
    }

    public String getBookNo() {
        return bookNo;
    }

    public void setBookNo(String bookNo) {
        this.bookNo = bookNo;
    }

    public String getBookTimeNo() {
        return bookTimeNo;
    }

    public void setBookTimeNo(String bookTimeNo) {
        this.bookTimeNo = bookTimeNo;
    }
}
