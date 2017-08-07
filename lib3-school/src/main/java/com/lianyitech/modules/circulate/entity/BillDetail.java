/**
 * 
 */
package com.lianyitech.modules.circulate.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.lianyitech.core.utils.CustomDoubleSerialize;
import com.lianyitech.core.utils.DataEntity;
import com.lianyitech.modules.sys.utils.UserUtils;
import org.hibernate.validator.constraints.Length;

/**
 * 单据明细管理Entity
 * @author zengzy
 * @version 2016-09-09
 */
public class BillDetail extends DataEntity<BillDetail> {
	
	private static final long serialVersionUID = 1L;
	private String orgId;		// 机构id
	private String billId;		// 操作单据ID
	private String copyId;		// 复本id
	private String isbn;		// ISBN
	private String title;		// 题名
	private String subTitle;		// 副题名
	private String tiedTitle;		// 并列题目
	private String partName;		// 分辑名
	private String partNum;		// 分辑号
	private String seriesName;		// 丛编名
	private String author;		// 著者
	private String subAuthor;		// 次要责任者
	private String seriesEditor;		// 丛编编者
	private String translator;		// 译者
	private String pressId;		// 出版社id
	private String publishingAddress;		// 出版地
	private String publishingTime;		// 出版时间
	private String librarsortId;		// 中图分类id
	private String librarsortCode;		// 中图分类号
	private String tanejiNo;		// 种次号
	private String assNo;		// 辅助区分号
	private Double price;		// 定价
	private String edition;		// 版次
	private String language;		// 语种
	private String measure;		// 尺寸
	private String pageNo;		// 页码
	private String bindingForm;		// 装帧形式
	private String bestAge;		// 适读年龄
	private String attachmentNote;		// 附件备注
	private String subject;		// 主题词
	private String content;		// 内容简介
	private String collectionSiteId;		// 馆藏地点id
	private String barcode;		// 条形码
	private String batchNo;		// 批次号
	private String status;		// 状态  引用EnumLibStoreStatus类
	private String isRenew;		// 是否续借 0否 1是
	private String isStained;		// 是否污损 0否 1是
	private String isOrder;		// 是否预约 0否 1是
	
	public BillDetail() {
		super();
	}

	public BillDetail(String id){
		super(id);
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	
	public String getBillId() {
		return billId;
	}

	public void setBillId(String billId) {
		this.billId = billId;
	}
	
	public String getCopyId() {
		return copyId;
	}

	public void setCopyId(String copyId) {
		this.copyId = copyId;
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
	
	public String getSubTitle() {
		return subTitle;
	}

	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}
	
	public String getTiedTitle() {
		return tiedTitle;
	}

	public void setTiedTitle(String tiedTitle) {
		this.tiedTitle = tiedTitle;
	}
	
	public String getPartName() {
		return partName;
	}

	public void setPartName(String partName) {
		this.partName = partName;
	}
	
	public String getPartNum() {
		return partNum;
	}

	public void setPartNum(String partNum) {
		this.partNum = partNum;
	}
	
	public String getSeriesName() {
		return seriesName;
	}

	public void setSeriesName(String seriesName) {
		this.seriesName = seriesName;
	}
	
	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}
	
	public String getSubAuthor() {
		return subAuthor;
	}

	public void setSubAuthor(String subAuthor) {
		this.subAuthor = subAuthor;
	}
	
	public String getSeriesEditor() {
		return seriesEditor;
	}

	public void setSeriesEditor(String seriesEditor) {
		this.seriesEditor = seriesEditor;
	}
	
	public String getTranslator() {
		return translator;
	}

	public void setTranslator(String translator) {
		this.translator = translator;
	}
	
	public String getPressId() {
		return pressId;
	}

	public void setPressId(String pressId) {
		this.pressId = pressId;
	}
	
	public String getPublishingAddress() {
		return publishingAddress;
	}

	public void setPublishingAddress(String publishingAddress) {
		this.publishingAddress = publishingAddress;
	}
	
	public String getPublishingTime() {
		return publishingTime;
	}

	public void setPublishingTime(String publishingTime) {
		this.publishingTime = publishingTime;
	}
	
	public String getLibrarsortId() {
		return librarsortId;
	}

	public void setLibrarsortId(String librarsortId) {
		this.librarsortId = librarsortId;
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

	@JsonSerialize(using = CustomDoubleSerialize.class)
	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}
	
	public String getEdition() {
		return edition;
	}

	public void setEdition(String edition) {
		this.edition = edition;
	}
	
	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}
	
	public String getMeasure() {
		return measure;
	}

	public void setMeasure(String measure) {
		this.measure = measure;
	}
	
	public String getPageNo() {
		return pageNo;
	}

	public void setPageNo(String pageNo) {
		this.pageNo = pageNo;
	}
	
	public String getBindingForm() {
		return bindingForm;
	}

	public void setBindingForm(String bindingForm) {
		this.bindingForm = bindingForm;
	}
	
	public String getBestAge() {
		return bestAge;
	}

	public void setBestAge(String bestAge) {
		this.bestAge = bestAge;
	}
	
	public String getAttachmentNote() {
		return attachmentNote;
	}

	public void setAttachmentNote(String attachmentNote) {
		this.attachmentNote = attachmentNote;
	}
	
	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
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
}