package com.lianyitech.modules.peri.entity;

import com.lianyitech.core.utils.DataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by zcx on 2017/3/9.
 * 期刊订购明细表
 */
@ApiModel(value="订单明细表", description = "订单明细表实体")
public class OrderDetail extends DataEntity<OrderDetail> {
    private static final long serialVersionUID = 1L;
    /**
     * 期刊订购ID
     */
    private String orderId;
    /**
     * 实到刊期
     */
    private String periNum;
    /**
     * 订购册数
     */
    private Integer orderAmount;
    /**
     * 实到册数
     */
    private Integer arriveAmount;
    /**
     * 库存册数(默认=实到册数)
     */
    private Integer amount;
    /**
     * 馆藏地id
     */
    private String collectionSiteId;
    /**
     * 是否合订过 0否 1是
     */
    private String isBound;
    /**
     * 传0和1   1代表已记到的
     */
    private String flag ;
    /**
     * 出版年份
     */
    private String publishingYear;
    /**
     * 期刊书目id
     */
    private String periDirectoryId;
    /**
     * 馆藏地名称
     */
    private String collectionSiteName;
    /**
     * 实到刊期开始范围
     */
    private Integer startPeriNum;
    /**
     * 实到刊期结束范围
     */
    private Integer endPeriNum;
    /**
     * 出版年份开始范围
     */
    private String publishingYearStart;
    /**
     * 出版年份结束范围
     */
    private String publishingYearEnd;
    /**
     * 备注
     */
    private String remarks;
    /**
     * 刊名
     */
    private String title;
    /**
     * 分类号
     */
    private String ids;

    private String librarsortCode;
    /**
     * 机构id
     */
    private String orgId;
    /**
     * 序号
     */
    private Integer seq;

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getLibrarsortCode() {
        return librarsortCode;
    }

    public void setLibrarsortCode(String librarsortCode) {
        this.librarsortCode = librarsortCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getRemarks() {
        return remarks;
    }

    @Override
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getCollectionSiteName() {
        return collectionSiteName;
    }

    public void setCollectionSiteName(String collectionSiteName) {
        this.collectionSiteName = collectionSiteName;
    }

    public String getIsBound() {
        return isBound;
    }

    public void setIsBound(String isBound) {
        this.isBound = isBound;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public Integer getStartPeriNum() {
        return startPeriNum;
    }

    public void setStartPeriNum(Integer startPeriNum) {
        this.startPeriNum = startPeriNum;
    }

    public Integer getEndPeriNum() {
        return endPeriNum;
    }

    public void setEndPeriNum(Integer endPeriNum) {
        this.endPeriNum = endPeriNum;
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

    public String getPublishingYear() {
        return publishingYear;
    }

    public void setPublishingYear(String publishingYear) {
        this.publishingYear = publishingYear;
    }

    public String getPeriDirectoryId() {
        return periDirectoryId;
    }

    public void setPeriDirectoryId(String periDirectoryId) {
        this.periDirectoryId = periDirectoryId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPeriNum() {
        return periNum;
    }

    public void setPeriNum(String periNum) {
        this.periNum = periNum;
    }

    public Integer getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(Integer orderAmount) {
        this.orderAmount = orderAmount;
    }

    public Integer getArriveAmount() {
        return arriveAmount;
    }

    public void setArriveAmount(Integer arriveAmount) {
        this.arriveAmount = arriveAmount;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getCollectionSiteId() {
        return collectionSiteId;
    }

    public void setCollectionSiteId(String collectionSiteId) {
        this.collectionSiteId = collectionSiteId;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }
}
