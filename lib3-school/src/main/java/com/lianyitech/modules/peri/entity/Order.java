package com.lianyitech.modules.peri.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.lianyitech.common.utils.StringUtils;
import com.lianyitech.core.enmu.EnumPeriFrequency;
import com.lianyitech.core.utils.CustomDoubleSerialize;
import com.lianyitech.core.utils.DataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * Created by zcx on 2017/3/9.
 * 期刊订购表
 */
public class Order extends DataEntity<Order> {
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
     * 出版年份
     */
    private String publishingYear;
    /**
     * 批次号
     */
    private String batchNo;
    /**
     * 起订日期
     */
    private String startTime;
    /**
     * 止订日期
     */
    private String endTime;
    /**
     * 订购期数
     */
    private int orderPeri;
    /**
     * 订购数量
     */
    private int orderAmount;
    /**
     * 订购总价
     */
    private double totalPrice;
    /**
     * 出版频率(枚举类型)
     */
    private String publishingFre;
    /**
     * 出版频率名称(枚举类型)
     */
    private String publishingFreName;
    /**
     * issn
     */
    private String issn;
    /**
     * 统一刊号
     */
    private String periNum;
    /**
     * 刊名
     */
    private String title;
    /**
     * 著者
     */
    private String author;
    /**
     * 出版社名
     */
    private String publishingName;
    /**
     * 邮发代号
     */
    private String emailNum;
    /**
     * 馆藏地id
     */
    private String collectionSiteId;
    /**
     * 是否记到 1是 0否
     */
    private String likeBoo;
    /**
     * 批量删除期刊订单的ids
     */
    private String ids;

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public String getLikeBoo() {
        return likeBoo;
    }

    public void setLikeBoo(String likeBoo) {
        this.likeBoo = likeBoo;
    }

    public String getCollectionSiteId() {
        return collectionSiteId;
    }

    public void setCollectionSiteId(String collectionSiteId) {
        this.collectionSiteId = collectionSiteId;
    }

    public String getPublishingName() {
        return publishingName;
    }

    public void setPublishingName(String publishingName) {
        this.publishingName = publishingName;
    }

    public String getEmailNum() {
        return emailNum;
    }

    public void setEmailNum(String emailNum) {
        this.emailNum = emailNum;
    }

    public String getIssn() {
        return issn;
    }

    public void setIssn(String issn) {
        this.issn = issn;
    }

    public String getPeriNum() {
        return periNum;
    }

    public void setPeriNum(String periNum) {
        this.periNum = periNum;
    }

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

    public String getPublishingYear() {
        return publishingYear;
    }

    public void setPublishingYear(String publishingYear) {
        this.publishingYear = publishingYear;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getOrderPeri() {
        return orderPeri;
    }

    public void setOrderPeri(int orderPeri) {
        this.orderPeri = orderPeri;
    }

    public int getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(int orderAmount) {
        this.orderAmount = orderAmount;
    }

    @JsonSerialize(using = CustomDoubleSerialize.class)
    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getPublishingFre() {
        return publishingFre;
    }

    public void setPublishingFre(String publishingFre) {
        this.publishingFre = publishingFre;
    }

    public String getPublishingFreName() {
        if(StringUtils.isNotEmpty(getPublishingFre())){
            return EnumPeriFrequency.parse(getPublishingFre()).getName();
        }else {
            return "";
        }
    }

    public void setPublishingFreName(String publishingFreName) {
        this.publishingFreName = publishingFreName;
    }
}
