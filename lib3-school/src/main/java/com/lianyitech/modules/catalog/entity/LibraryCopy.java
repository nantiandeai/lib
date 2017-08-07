package com.lianyitech.modules.catalog.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lianyitech.core.enmu.EnumLibStoreStatus;
import com.lianyitech.core.utils.DataEntity;
import com.lianyitech.core.utils.SystemUtils;

import java.util.Date;
import java.util.List;

/**
 * Created by chenxiaoding on 2016/9/1.
 */
public class LibraryCopy extends DataEntity<LibraryCopy>{

    private String id;
    private String bookDirectoryId;//书目 ID
    private String beginBarcode;//批添加，开始条形码
    private String endBarcode;//结束条形码
    private String librarsortCode;//分类号
    private String tanejiNo;		// 种次号
    private String bookNo;//著作号
    private String assNo;		// 辅助区分号
    private String barcode;//条形码
    private String batchNo;//批次号
    private String batchId;//批次号id
    private String siteName;//馆藏地名称
    private String siteId;//馆藏地id
    private Date inLibraryDate;//入库日期
    private String countLibrary;//馆藏册数
    private String orgId;//机构id
    private String status;//馆藏状态
    private String statusName;//馆藏状态名称
    private String collectionSiteId; //查询条件的馆藏地ID
    private String isbn;//isbn
    private String title;//题名
    private String author;//著者
    private String publishingName;//出版社
    private String publishingTime;//出版时间
    private String indexNum;//索书号
    private String collectionSiteName; //馆藏地点
    private String price;//定价
    private String oldBarcode;//老条码
    private String newBarcode;//新条码

    private List<LibraryCopy> list;

    public List<LibraryCopy> getList() {
        return list;
    }

    public void setList(List<LibraryCopy> list) {
        this.list = list;
    }

    public String getOldBarcode() {
        return oldBarcode;
    }

    public void setOldBarcode(String oldBarcode) {
        this.oldBarcode = oldBarcode;
    }

    public String getNewBarcode() {
        return newBarcode;
    }

    public void setNewBarcode(String newBarcode) {
        this.newBarcode = newBarcode;
    }

    public String getBookNo() {
        return bookNo;
    }

    public void setBookNo(String bookNo) {
        this.bookNo = bookNo;
    }

    public String getCollectionSiteId() {
        return collectionSiteId;
    }

    public void setCollectionSiteId(String collectionSiteId) {
        this.collectionSiteId = collectionSiteId;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public LibraryCopy() {
    }

    public String getBeginBarcode() {
        return beginBarcode;
    }

    public void setBeginBarcode(String beginBarcode) {
        this.beginBarcode = beginBarcode;
    }

    public String getEndBarcode() {
        return endBarcode;
    }

    public void setEndBarcode(String endBarcode) {
        this.endBarcode = endBarcode;
    }

    public String getBookDirectoryId() {
        return bookDirectoryId;
    }

    public void setBookDirectoryId(String bookDirectoryId) {
        this.bookDirectoryId = bookDirectoryId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIndexNum() {
        return SystemUtils.getIndexNum1(this.getLibrarsortCode(),this.getTanejiNo(),this.getAssNo(),this.getBookNo());
    }

    public void setIndexNum(String indexNum) {
        this.indexNum = indexNum;
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

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    public Date getInLibraryDate() {
        return inLibraryDate;
    }

    public void setInLibraryDate(Date inLibraryDate) {
        this.inLibraryDate = inLibraryDate;
    }

    public String getCountLibrary() {
        return countLibrary;
    }

    public void setCountLibrary(String countLibrary) {
        this.countLibrary = countLibrary;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusName() {
        return EnumLibStoreStatus.parse(getStatus()).getName();
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
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

    public String getCollectionSiteName() {
        return collectionSiteName;
    }

    public void setCollectionSiteName(String collectionSiteName) {
        this.collectionSiteName = collectionSiteName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "LibraryCopy{" +
                "id='" + id + '\'' +
                ", bookDirectoryId='" + bookDirectoryId + '\'' +
                ", indexNum='" + indexNum + '\'' +
                ", librarsortCode='" + librarsortCode + '\'' +
                ", tanejiNo='" + tanejiNo + '\'' +
                ", assNo='" + assNo + '\'' +
                ", barcode='" + barcode + '\'' +
                ", batchNo='" + batchNo + '\'' +
                ", batchId='" + batchId + '\'' +
                ", siteId='" + siteId + '\'' +
                ", siteName='" + siteName + '\'' +
                ", inLibraryDate=" + inLibraryDate +
                '}';
    }

    public LibraryCopy(String indexNum, String librarsortCode, String tanejiNo, String assNo, String barcode, String batchNo,String batchId,String siteId, String siteName, Date inLibraryDate, String countLibrary) {
        this.indexNum = indexNum;
        this.librarsortCode = librarsortCode;
        this.tanejiNo = tanejiNo;
        this.assNo = assNo;
        this.barcode = barcode;
        this.batchNo = batchNo;
        this.batchId = batchId;
        this.siteId = siteId;
        this.siteName = siteName;
        this.inLibraryDate = inLibraryDate;
        this.countLibrary = countLibrary;
    }

}
