package com.lianyitech.modules.report.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.lianyitech.core.enmu.EnumLibStoreStatus;
import com.lianyitech.core.utils.CustomDoubleSerialize;
import com.lianyitech.core.utils.DataEntity;
import com.lianyitech.core.utils.SystemUtils;
import com.lianyitech.modules.common.ReportCommon;

/*
 * 馆藏书目及复本报表通用DTO
 * Created by chenxiaoding on 2016/11/4.
 */
public class BookDirectoryCopy extends DataEntity<BookDirectoryCopy> {

    private static final long serialVersionUID = 1L;
    private String bookId;//书目ID
    private String isbn;//isbn
    private String indexNum;//索书号 这在此算出
    private String tanejiNo;//种次号
    private String assNo;//辅助区分号
    private String librarsortCode;//分类号
    private String title;//题名
    private String author;//作者
    private String bookNo; //著者号
    private String publishingName;//出版社
    private String orderPublishingTime; //出版时间排序
    private String publishingTime;//出版时间
    private Double price;//价格
    private Integer bookNum;//馆藏册数
    private String bookNumSort; //排序字段
    //书目条件字段
    private String startBookNum;//开始册数
    private String endBookNum;//结束册数
    //复本明细里加的字段
    private String barcode;
    private String putDate;//入库时间
    private String collectionSiteId;//馆藏地ID
    private String collectionName;//馆藏地名
    private String status;//馆藏状态
    //复本条件
    private String startDate;//入库日期开始
    private String endDate;//入库日期结束
    private String orderBy;
    private int pageNo = 1; // 当前页码
    private int pageSize = 20; // 页面大小，设置为“-1”表示不进行分页（分页无效）
    private String orgId;//机构id

    private int   borrow  ;       //总借阅次数
    private int    renew    ;          //总续借次数
    private int     subscribe;        //总预约次数
    private  int     orderBorrow;         //总预借次数
    private String    batchNo;		// 批次号
    private String likeBoo;         //是否有流通记录 1是 0否

    public String getLikeBoo() {
        return likeBoo;
    }

    public void setLikeBoo(String likeBoo) {
        this.likeBoo = likeBoo;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public int getBorrow() {
        return borrow;
    }

    public void setBorrow(int borrow) {
        this.borrow = borrow;
    }

    public int getRenew() {
        return renew;
    }

    public void setRenew(int renew) {
        this.renew = renew;
    }

    public int getSubscribe() {
        return subscribe;
    }

    public void setSubscribe(int subscribe) {
        this.subscribe = subscribe;
    }

    public int getOrderBorrow() {
        return orderBorrow;
    }

    public void setOrderBorrow(int orderBorrow) {
        this.orderBorrow = orderBorrow;
    }

    public String getBookNo() {
        return bookNo;
    }

    public void setBookNo(String bookNo) {
        this.bookNo = bookNo;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getOrderPublishingTime() {
        return orderPublishingTime;
    }

    public void setOrderPublishingTime(String orderPublishingTime) {
        this.orderPublishingTime = orderPublishingTime;
    }

    public String getBookNumSort() {
        return bookNumSort;
    }

    public void setBookNumSort(String bookNumSort) {
        this.bookNumSort = bookNumSort;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    @JsonIgnore
    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
    @JsonIgnore
    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getPutDate() {
        return putDate;
    }

    public void setPutDate(String putDate) {
        this.putDate = putDate;
    }

    @JsonIgnore
    public String getCollectionSiteId() {
        return collectionSiteId;
    }

    public void setCollectionSiteId(String collectionSiteId) {
        this.collectionSiteId = collectionSiteId;
    }

    public String getCollectionName() {
        return collectionName;
    }

    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }

    public String getStatus() {
        if(status!=null && status!="") {
            return EnumLibStoreStatus.parse(status).getName();
        }else {
            return "";
        }
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @JsonIgnore
    public String getStartBookNum() {
        return startBookNum;
    }

    public void setStartBookNum(String startBookNum) {
        this.startBookNum = startBookNum;
    }

    @JsonIgnore
    public String getEndBookNum() {
        return endBookNum;
    }

    public void setEndBookNum(String endBookNum) {
        this.endBookNum = endBookNum;
    }

    public BookDirectoryCopy() {
    }

    public BookDirectoryCopy( String bookId, String isbn, String indexNum,
                             String tanejiNo, String assNo, String librarsortCode,
                             String title, String author, String publishingName,
                             String publishingTime, String putDate,Double price, Integer bookNum) {
        this.bookId = bookId;
        this.isbn = isbn;
        this.indexNum = indexNum;
        this.tanejiNo = tanejiNo;
        this.assNo = assNo;
        this.librarsortCode = librarsortCode;
        this.title = title;
        this.author = author;
        this.publishingName = publishingName;
        this.putDate = putDate;
        this.publishingTime = publishingTime;
        this.price = price;
        this.bookNum = bookNum;
    }

    @Override
    public String toString() {
        return "BookDirectoryCopy{" +
                "bookId='" + bookId + '\'' +
                ", isbn='" + isbn + '\'' +
                ", indexNum='" + indexNum + '\'' +
                ", tanejiNo='" + tanejiNo + '\'' +
                ", assNo='" + assNo + '\'' +
                ", librarsortCode='" + librarsortCode + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", publishingName='" + publishingName + '\'' +
                ", publishingTime='" + publishingTime + '\'' +
               ", price=" + price +
                ", bookNum=" + bookNum +
                '}';
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getIndexNum() {
        return SystemUtils.getIndexNum1(librarsortCode,tanejiNo,assNo,bookNo);
    }

    public void setIndexNum(String indexNum) {
        this.indexNum = indexNum;
    }
    @JsonIgnore
    public String getTanejiNo() {
        return tanejiNo;
    }

    public void setTanejiNo(String tanejiNo) {
        this.tanejiNo = tanejiNo;
    }
    @JsonIgnore
    public String getAssNo() {
        return assNo;
    }

    public void setAssNo(String assNo) {
        this.assNo = assNo;
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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublishingName() {
        return publishingName;
    }

    public void setPublishingName(String publishingName) {
        this.publishingName = publishingName;
    }

    public String getPublishingTime() {
        return publishingTime;
    }

    public void setPublishingTime(String publishingTime) {
        this.publishingTime = publishingTime;
    }

    @JsonSerialize(using = CustomDoubleSerialize.class)
    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getBookNum() {
        return bookNum;
    }

    public void setBookNum(Integer bookNum) {
        this.bookNum = bookNum;
    }

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

    @JsonIgnore
    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }
}
