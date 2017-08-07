/**
 * 
 */
package com.lianyitech.modules.catalog.entity;
import com.lianyitech.core.utils.DataEntity;
import com.lianyitech.modules.sys.utils.UserUtils;

/**
 * 书目管理Entity
 * @author zengym
 * @version 2016-09-23
 */
public class BookDirectory extends DataEntity<BookDirectory> {
	
	private static final long serialVersionUID = 1L;
	private String orgId;		// 机构id
	private String isbn;		// ISBN
	private String title;		// 题名
	private String subTitle;		// 副题目
	private String tiedTitle;		// 并列题目
	private String partName;		// 分辑名
	private String partNum;		// 分辑号
	private String seriesName;		// 丛编名
	private String author;		// 著者
	private String subAuthor;		// 次要责任者
	private String seriesEditor;		// 丛编编者
	private String translator;		// 译者
	private String publishingName;//出版社名称
	private String publishingAddress;		// 出版地
	private String publishingTime;		// 出版时间
	private String librarsortId;		// 中图分类id
	private String librarsortCode;		// 中途分类号
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
	private String tanejiNo;		// 种次号
	private String assNo;		// 辅助区分号
	private String purpose;  //图书用途
	private String marc64;//马克数据

	public BookDirectory() {
		super();
	}

	public BookDirectory(String id){
		super(id);
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	//isbn系统目前都自动过滤-
	public String getIsbn() {
		return isbn!=null?isbn.replace("-",""):isbn;
	}

	//isbn 系统目前自动过滤-
	public void setIsbn(String isbn) {
		this.isbn = isbn!=null?isbn.replace("-",""):isbn;
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

	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	public String getPublishingName() {
		return publishingName;
	}

	public void setPublishingName(String publishingName) {
		this.publishingName = publishingName;
	}

	public String getMarc64() {
		return marc64;
	}

	public void setMarc64(String marc64) {
		this.marc64 = marc64;
	}

}