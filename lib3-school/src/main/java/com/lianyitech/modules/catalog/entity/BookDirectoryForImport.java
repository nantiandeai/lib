/**
 *
 */
package com.lianyitech.modules.catalog.entity;

import com.lianyitech.core.utils.DataEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.validation.ValidationUtils;

@ToString
@EqualsAndHashCode(of = {"isbn", "title", "author", "unitprice", "barcode"}, callSuper = false)
@Data
public class BookDirectoryForImport extends DataEntity<BookDirectoryForImport> {

	private static final long serialVersionUID = 1L;
	private String orgId;		// 机构id
	private String isbn;		// ISBN   ---目前自动过滤-
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
	private String librarsortCode;		// 中途分类号---目前自动转成大写
	private Double price;		// 定价
	private String edition;		// 版次
	private String language;		// 语种
	private String languageName;//语种
	private String measure;		// 尺寸
	private String pageNo;		// 页码
	private String bindingForm;		// 装帧形式
	private String bindingFormName;//装帧形式中文（避免导出的时候通过函数转换影响速度）
	private String bestAge;		// 适读年龄
	private String bestAgeName;//适度年龄中文 （避免导出的时候通过函数转换影响速度）
	private String attachmentNote;		// 附件备注
	private String subject;		// 主题词
	private String content;		// 内容简介
	private String tanejiNo;		// 种次号
	private String bookNo;// 著者号
	private String assNo;		// 辅助区分号
	private String purpose;  //图书用途
	private String purposeName;//图书用途中文（避免导出的时候通过函数转换影响速度）
	private String marc64;//马克数据

	//下面字段是非数据库表里面字段
	private String errorinfo;     //导入数据的时候，错误信息字段。
	private String unitprice;     //导入excel数据的时候用到。
	private String barcode;		  // 导入馆藏数据的时候，用来保存条形码。
	private String indexnum;      //导入馆藏数据的时候，用来保存索书号
	private String collectionSiteId;		//导入馆藏数据的时候，用来保存馆藏地点id
	private String collectionSiteName;   //导入馆藏数据的时候，用来保存馆藏地点name
	private String batchNo;		  // 导入馆藏数据的时候，用来保存批次号
	private int booknumber;//复本数---加了这个参数是区分此书目是否有复本
	private String userLoginName;   //导入馆藏数据的时候用到
}