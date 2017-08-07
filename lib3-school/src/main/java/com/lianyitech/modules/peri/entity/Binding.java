package com.lianyitech.modules.peri.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.lianyitech.common.utils.StringUtils;
import com.lianyitech.core.enmu.EnumLibStoreStatus;
import com.lianyitech.core.utils.CustomDoubleSerialize;
import com.lianyitech.core.utils.DataEntity;
import com.lianyitech.core.utils.SystemUtils;
import com.lianyitech.modules.peri.service.BindingService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;

/**
 * Created by zcx on 2017/3/9.
 * 过刊装订表
 */
public class Binding extends DataEntity<Binding> {
    private static final Log log = LogFactory.getLog(BindingService.class);
    private static final long serialVersionUID = 1L;
    /**
     * 机构id
     */
    private String orgId;
    /**
     * 期刊目录id
     */
    private String periDirectoryId;
    /**
     * 馆藏地id
     */
    private String collectionSiteId;
    /**
     * 出版年份
     */
    private String publishingYear;
    /**
     * 刊名
     */
    private String title;
    /**
     * 装订卷期
     */
    private String bindingNum;
    /**
     * issn
     */
    private String issn;
    /**
     * 装订价格
     */
    private Double price;
    /**
     * 条形码
     */
    private String barcode;
    /**
     * 分类号
     */
    private String librarsortCode;
    /**
     * 书次号
     */
    private String bookTimeNo;
    /**
     * 辅助区分号
     */
    private String assNo;
    /**
     * 索刊号
     */
    private String somNo;
    /**
     * 作者
     */
    private String author;
    /**
     * 登记状态(0用户登记和新增的合订  1通过记到步骤的合订)
     */
    private String checkStatus;
    /**
     * 出版社名称
     */
    private String publishingName;
    /**
     * 条码开始范围
     */
    private String startBarCode;
    /**
     * 条码结束范围
     */
    private String endBarCode;
    /**
     * 馆藏状态
     */
    private String status;
    /**
     * 出版时间开始范围
     */
    private String publishingYearStart;
    /**
     * 出版时间结束范围
     */
    private String publishingYearEnd;
    /**
     * 统一刊号
     */
    private String periNum;
    /**
     * 馆藏地名称
     */
    private String collectionSiteName;
    /**
     * 分类号
     */
    private List<String> libSortCodeList;
    /**
     * 是否记到 1是 0否
     */
    private String likeBoo;
    /**
     * 条形码排序
     */
    private String orderBarCode;
    /**
     * 索书号排序
     */
    private String orderIndexNum;
    /**
     * 入库日期开始范围
     */
    private String createDateStart;
    /**
     * 入库日期结束范围
     */
    private String createDateEnd;

    private String orderBy;
    /**
     * 导出方式  1：仅索书号 2：索书号+条形码
     */
    private String exportType = "1";

    /**
     * 邮发代号
     */
    private String emailNum;

    @JsonIgnore
    public String getExportType() {
        return exportType;
    }

    public void setExportType(String exportType) {
        this.exportType = exportType;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getOrderBarCode() {
        return orderBarCode;
    }

    public void setOrderBarCode(String orderBarCode) {
        this.orderBarCode = orderBarCode;
    }

    public String getOrderIndexNum() {
        return orderIndexNum;
    }

    public void setOrderIndexNum(String orderIndexNum) {
        this.orderIndexNum = orderIndexNum;
    }

    public String getLikeBoo() {
        return likeBoo;
    }

    public void setLikeBoo(String likeBoo) {
        this.likeBoo = likeBoo;
    }

    public List<String> getLibSortCodeList() {
        return libSortCodeList;
    }

    public void setLibSortCodeList(List<String> libSortCodeList) {
        this.libSortCodeList = libSortCodeList;
    }

    public String getCollectionSiteName() {
        return collectionSiteName;
    }

    public void setCollectionSiteName(String collectionSiteName) {
        this.collectionSiteName = collectionSiteName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIssn() {
        return issn;
    }

    public void setIssn(String issn) {
        if(StringUtils.isNotEmpty(issn)){
            issn = SystemUtils.changeChineseSymbol(issn);
        }
        this.issn = issn;
    }

    public String getStartBarCode() {
        return startBarCode;
    }

    public void setStartBarCode(String startBarCode) {
        this.startBarCode = startBarCode;
    }

    public String getEndBarCode() {
        return endBarCode;
    }

    public void setEndBarCode(String endBarCode) {
        this.endBarCode = endBarCode;
    }

    public String getPublishingYearStart() {
        return publishingYearStart;
    }

    public void setPublishingYearStart(String publishingYearStart) {
        this.publishingYearStart = publishingYearStart;
    }

    public String getPublishingYearEnd() {
        return publishingYearEnd;
    }

    public void setPublishingYearEnd(String publishingYearEnd) {
        this.publishingYearEnd = publishingYearEnd;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getPeriDirectoryId() {
        return periDirectoryId;
    }

    public void setPeriDirectoryId(String periDirectoryId) {
        this.periDirectoryId = periDirectoryId;
    }

    public String getCollectionSiteId() {
        return collectionSiteId;
    }

    public void setCollectionSiteId(String collectionSiteId) {
        this.collectionSiteId = collectionSiteId;
    }

    public String getPublishingYear() {
        return publishingYear;
    }

    public void setPublishingYear(String publishingYear) {
        this.publishingYear = publishingYear;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBindingNum() {
        return bindingNum;
    }

    public void setBindingNum(String bindingNum) {
        this.bindingNum = bindingNum;
    }

    @JsonSerialize(using = CustomDoubleSerialize.class)
    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getLibrarsortCode() {
        return librarsortCode;
    }

    public void setLibrarsortCode(String librarsortCode) {
        this.librarsortCode = librarsortCode;
    }

    public String getBookTimeNo() {
        return bookTimeNo;
    }

    public void setBookTimeNo(String bookTimeNo) {
        this.bookTimeNo = bookTimeNo;
    }

    public String getAssNo() {
        return assNo;
    }

    public void setAssNo(String assNo) {
        this.assNo = assNo;
    }

    public String getSomNo() {
        return SystemUtils.getSomNo(this.getLibrarsortCode(),this.getBookTimeNo(),this.getAssNo());
    }

    public void setSomNo(String somNo) {
        if(StringUtils.isNotEmpty(somNo)){
            somNo = SystemUtils.changeChineseSymbol(somNo);
        }
        this.somNo = somNo;
    }

    public String getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(String checkStatus) {
        this.checkStatus = checkStatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPublishingName() {
        return publishingName;
    }

    public void setPublishingName(String publishingName) {
        this.publishingName = publishingName;
    }

    public String getPeriNum() {
        return periNum;
    }

    public void setPeriNum(String periNum) {
        if(StringUtils.isNotEmpty(periNum)){
            periNum = SystemUtils.changeChineseSymbol(periNum);
        }
        this.periNum = periNum;
    }

    public String getCreateDateStart() {
        return createDateStart;
    }

    public void setCreateDateStart(String createDateStart) {
        this.createDateStart = createDateStart;
    }

    public String getCreateDateEnd() {
        return createDateEnd;
    }

    public void setCreateDateEnd(String createDateEnd) {
        this.createDateEnd = createDateEnd;
    }

    public String getEmailNum() {
        return emailNum;
    }

    public void setEmailNum(String emailNum) {
        this.emailNum = emailNum;
    }
}
