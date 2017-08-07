package com.lianyitech.modules.report.entity;

import com.lianyitech.core.utils.DataEntity;
import com.lianyitech.core.utils.SystemUtils;

/**
 * Created by zcx on 2016/11/10.
 * 藏书量统计实体
 */
public class CollectBookStatistics extends DataEntity<CollectBookStatistics> {

    private static final long serialVersionUID = 1L;
    public String siteName;//馆藏地名称
    public String siteId;//馆藏地id
    public String orgId;//机构id
    public String barcode;//条形码
    private String indexNum;//索书号
    private String orderIndexNum;//索书号排序
    public String librarsortCode;//中图分类号
    public String librarsortId;//中图分类号id
    public String tanejiNo;//种次号
    public String assNo;//辅助分区号
    public String isbn;//ISBN
    public String title;//题名
    public String author;//著者
    private String orderBy;

    private int pageNo = 1;    // 当前页码
    private int pageSize = 20; // 页面大小，设置为“-1”表示不进行分页（分页无效）

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getOrderIndexNum() {
        return orderIndexNum;
    }

    public void setOrderIndexNum(String orderIndexNum) {
        this.orderIndexNum = orderIndexNum;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
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

    public String getIndexNum() {
        return SystemUtils.getIndexNum(getLibrarsortCode(),getTanejiNo(),getAssNo());
    }

    public void setIndexNum(String indexNum) {
        this.indexNum = indexNum;
    }

    public String getLibrarsortId() {
        return librarsortId;
    }

    public void setLibrarsortId(String librarsortId) {
        this.librarsortId = librarsortId;
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

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
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
}
