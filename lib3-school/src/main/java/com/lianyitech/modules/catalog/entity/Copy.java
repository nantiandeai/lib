/**
 * 
 */
package com.lianyitech.modules.catalog.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.lianyitech.core.utils.CustomDoubleSerialize;
import com.lianyitech.core.utils.DataEntity;
import com.lianyitech.core.utils.SystemUtils;
import com.lianyitech.modules.sys.utils.UserUtils;

import java.util.List;

/**
 * 馆藏复本管理Entity
 * @author zengzy
 * @version 2016-08-26
 */
public class Copy extends DataEntity<Copy> {

	private static final long serialVersionUID = 1L;
	private String orgId;		// 机构id
	private String bookDirectoryId;		// 书目id
	private String collectionSiteId;		// 馆藏地点id
	private String siteId;      //馆藏地id  查询条件专用
	private String barcode;		// 条形码
	private String batchNo;		// 批次号
	private String status;		// 馆藏状态   引用EnumLibStoreStatus类
	private String isRenew;		// 是否续借 0否 1是
	private String isStained;		// 是否污损 0否 1是
	private String isOrder;       // 是否预约 0否 1是
	private String place;		//存放位置
	private String batchId;		// 批次号id


	//下面字段是非数据库表里面字段
	private String startBarCode;   //条形码范围查询
	private String endBarCode;
	private String createDateStart;	// 入库日期-开始   	查询专用
	private String createDateEnd;  //入库日期-结束		查询专用
	private String collectionSiteName; //馆藏地点  通过关联
	private String isbn;//isbn 关联用到
	private String title;//题名 关联书目用到
	private String author;//著者
	private String publishingName;//出版社
	private String librarsortCode;//分类号librarsortCode
	private String tanejiNo;		// 种次号
	private String assNo;		// 辅助区分号
	private String publishingTime;//出版时间
	private String indexnum;//索书号
	private String scrapDate;//报废日期
	private String scrapDateStart; //报废日期-开始   	查询专用
	private String scrapDateEnd; //报废日期-结束		查询专用
	private String loseDate;//丢失日期
	private String loseDateStart; //丢失日期-开始   	查询专用
	private String loseDateEnd; //丢失日期-结束		查询专用
	private String weedingDate;//剔旧日期
	private String weedingDateStart; //剔旧日期-开始   	查询专用
	private String weedingDateEnd; //剔旧日期-结束		查询专用
	private Double price;//定价
	private List<String> libSortCodeList;
	private String type;//这里是为了馆藏清单那特别sql用
	private String stockAttr;//是否可以外借
	private String orderBarCode; //条形码排序
	private String orderIndexNum; //索书号排序
	private String statusNum;//状态中文
	private String bookNo;//著者号
	private String libIndex;
	private String orderPublishingTime; //出版日期排序
    private String orderCreateDate;//入库日期排序
	private String orderUpdateDate;//修改日期排序
	private String seriesName;		// 丛编名
    private String isCirculation;  //是否查询流通的复本  0代表是1代表否
	private String[] idList;

	private String checkAll;
	private String orderBy;

	private String exportType = "1"; //导出方式  1：仅索书号 2：索书号+条形码

	private String stainsDate;//污损日期
	private String stainsDateStart;//污损日期-开始   	查询专用
	private String stainsDateEnd;//污损日期-结束		查询专用

    public String getIsCirculation() {
        return isCirculation;
    }

    public void setIsCirculation(String isCirculation) {
        this.isCirculation = isCirculation;
    }

    /**
	 * 读者证号
	 */
	private String card;

	public String getCard() {
		return card;
	}

	public void setCard(String card) {
		this.card = card;
	}

	public String getStainsDate() {
		return stainsDate;
	}

	public void setStainsDate(String stainsDate) {
		this.stainsDate = stainsDate;
	}

	public String getStainsDateStart() {
		return stainsDateStart;
	}

	public void setStainsDateStart(String stainsDateStart) {
		this.stainsDateStart = stainsDateStart;
	}

	public String getStainsDateEnd() {
		return stainsDateEnd;
	}

	public void setStainsDateEnd(String stainsDateEnd) {
		this.stainsDateEnd = stainsDateEnd;
	}

	public String getSeriesName() {
		return seriesName;
	}

	public void setSeriesName(String seriesName) {
		this.seriesName = seriesName;
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

	private int pageNo = 1; // 当前页码
	private int pageSize = 20; // 页面大小，设置为“-1”表示不进行分页（分页无效）

	@JsonIgnore
	public String getExportType() {
		return exportType;
	}

	public void setExportType(String exportType) {
		this.exportType = exportType;
	}

	public String getOrderUpdateDate() {
		return orderUpdateDate;
	}

	public void setOrderUpdateDate(String orderUpdateDate) {
		this.orderUpdateDate = orderUpdateDate;
	}

	public String getOrderCreateDate() {
		return orderCreateDate;
	}

	public void setOrderCreateDate(String orderCreateDate) {
		this.orderCreateDate = orderCreateDate;
	}

	public String getOrderPublishingTime() {
		return orderPublishingTime;
	}

	public void setOrderPublishingTime(String orderPublishingTime) {
		this.orderPublishingTime = orderPublishingTime;
	}

	public String getStatusNum() {
		return statusNum;
	}

	public void setStatusNum(String statusNum) {
		this.statusNum = statusNum;
	}

	public String getBatchId() {
		return batchId;
	}

	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}

	public String[] getIdList() {
		return idList;
	}

	public void setIdList(String[] idList) {
		this.idList = idList;
	}

	public List<String> getLibSortCodeList() {
		return libSortCodeList;
	}

	public void setLibSortCodeList(List<String> libSortCodeList) {
		this.libSortCodeList = libSortCodeList;
	}

	public Copy() {
		super();
	}

	public Copy(String id){
		super(id);
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	
	public String getBookDirectoryId() {
		return bookDirectoryId;
	}

	public void setBookDirectoryId(String bookDirectoryId) {
		this.bookDirectoryId = bookDirectoryId;
	}

	public String getCollectionSiteId() {
		return collectionSiteId;
	}

	public void setCollectionSiteId(String collectionSiteId) {
		this.collectionSiteId = collectionSiteId;
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
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getIsRenew() {
		return isRenew;
	}

	public void setIsRenew(String isRenew) {
		this.isRenew = isRenew;
	}
	
	public String getIsStained() {
		return isStained;
	}

	public void setIsStained(String isStained) {
		this.isStained = isStained;
	}

	public String getIsOrder() {
		return isOrder;
	}

	public void setIsOrder(String isOrder) {
		this.isOrder = isOrder;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
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

	public String getCollectionSiteName() {
		return collectionSiteName;
	}

	public void setCollectionSiteName(String collectionSiteName) {
		this.collectionSiteName = collectionSiteName;
	}

	public String getIsbn() {
		return isbn!=null?isbn.replace("-",""):isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn!=null?isbn.replace("-",""):isbn;
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

	public String getLibrarsortCode() {
		return librarsortCode;
	}

	public void setLibrarsortCode(String librarsortCode) {
		this.librarsortCode = librarsortCode;
	}

	public String getAssNo() {
		return assNo;
	}

	public void setAssNo(String assNo) {
		this.assNo = assNo;
	}

	public String getTanejiNo() {
		return tanejiNo;
	}

	public void setTanejiNo(String tanejiNo) {
		this.tanejiNo = tanejiNo;
	}

	public String getPublishingTime() {
		return publishingTime;
	}

	public void setPublishingTime(String publishingTime) {
		this.publishingTime = publishingTime;
	}

	public String getIndexnum() {
		return SystemUtils.getIndexNum1(this.getLibrarsortCode(),this.getTanejiNo(),this.getAssNo(),this.getBookNo());
	}

	public String getlibIndex() {
		return SystemUtils.getIndexNum1(this.getLibrarsortCode(),this.getTanejiNo(),this.getAssNo(),this.getBookNo());
	}

	public void setLibIndex(String libIndex){
		this.libIndex= libIndex;
	}

	public void setIndexnum(String indexnum) {
		this.indexnum = indexnum;
	}

	public String getScrapDate() {
		return scrapDate;
	}

	public void setScrapDate(String scrapDate) {
		this.scrapDate = scrapDate;
	}

	public String getLoseDate() {
		return loseDate;
	}

	public void setLoseDate(String loseDate) {
		this.loseDate = loseDate;
	}

	public String getWeedingDate() {
		return weedingDate;
	}

	public void setWeedingDate(String weedingDate) {
		this.weedingDate = weedingDate;
	}

	public String getScrapDateStart() {
		return scrapDateStart;
	}

	public void setScrapDateStart(String scrapDateStart) {
		this.scrapDateStart = scrapDateStart;
	}

	public String getScrapDateEnd() {
		return scrapDateEnd;
	}

	public void setScrapDateEnd(String scrapDateEnd) {
		this.scrapDateEnd = scrapDateEnd;
	}

	public String getLoseDateStart() {
		return loseDateStart;
	}

	public void setLoseDateStart(String loseDateStart) {
		this.loseDateStart = loseDateStart;
	}

	public String getLoseDateEnd() {
		return loseDateEnd;
	}

	public void setLoseDateEnd(String loseDateEnd) {
		this.loseDateEnd = loseDateEnd;
	}

	public String getWeedingDateStart() {
		return weedingDateStart;
	}

	public void setWeedingDateStart(String weedingDateStart) {
		this.weedingDateStart = weedingDateStart;
	}

	public String getWeedingDateEnd() {
		return weedingDateEnd;
	}

	@JsonSerialize(using = CustomDoubleSerialize.class)
	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public void setWeedingDateEnd(String weedingDateEnd) {
		this.weedingDateEnd = weedingDateEnd;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStockAttr() {
		return stockAttr;
	}

	public void setStockAttr(String stockAttr) {
		this.stockAttr = stockAttr;
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

	public String getSiteId() {
		return siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}

	public String getBookNo() {
		return bookNo;
	}

	public void setBookNo(String bookNo) {
		this.bookNo = bookNo;
	}

	public String getCheckAll() {
		return checkAll;
	}

	public void setCheckAll(String checkAll) {
		this.checkAll = checkAll;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}
}