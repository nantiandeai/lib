/**
 * 
 */
package com.lianyitech.modules.catalog.entity;

import com.lianyitech.core.utils.DataEntity;
import com.lianyitech.core.utils.SystemUtils;
import com.lianyitech.modules.sys.utils.UserUtils;

/**
 * 新书通报管理Entity
 * @author zengzy
 * @version 2016-08-26
 */
public class NewbookNotifiy extends DataEntity<NewbookNotifiy> {

	private static final long serialVersionUID = 1L;
	private String orgId;		// 机构id
	private String name;		// 名称
	private String status;		//通报状态  0未通报 1已通报

	//只当作查询
	private String createDateStart;	// 建立日期-开始   	查询专用
	private String createDateEnd;  //建立日期-结束		查询专用
	private String isbn;//isbn 关联用到
	private String title;//题名 关联书目用到
	private String author;//著者
	private String publishingName;//出版社
	private String publishingTime;//出版时间
	private String price; //定价
	private String librarsortCode;//分类号
	private String tanejiNo;		// 种次号
	private String assNo;		// 辅助区分号

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

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getIndexnum() {
		return SystemUtils.getIndexNum(this.getLibrarsortCode(),this.getTanejiNo(),this.getAssNo());
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

	public NewbookNotifiy() {
		super();
		this.status=NO_REPORT;
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

	public NewbookNotifiy(String id){
		super(id);
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPublishingTime() {
		return publishingTime;
	}

	public void setPublishingTime(String publishingTime) {
		this.publishingTime = publishingTime;
	}

	/**
	 * 新书通报状态标识(0未通报 1已通报)
	 */
	public static final String NO_REPORT="0";
	public static final String GO_REPORT="1";

}