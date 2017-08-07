package com.lianyitech.modules.offline.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.lianyitech.core.utils.CustomDoubleSerialize;
import com.lianyitech.core.utils.DataEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@ToString
public class CopyVo extends DataEntity<CopyVo> {
    private static final long serialVersionUID = 1L;
    /**
     * 机构id
     */
    private String orgId;
    /**
     * 书目id
     */
    private String bookDirectoryId;
    /**
     * 条形码
     */
    private String barCode;
    /**
     * 批次号
     */
    private String batchNo;
    /**
     * 状态
     */
    private String status;
    /**
     * 是否续借
     */
    private String isRenew;
    /**
     * 是否污损
     */
    private String isStained;
    /**
     * isbn
     */
    private String isbn;
    /**
     * 题名
     */
    private String title;

    /**
     * 副题名
     */
    private String subTitle;
    /**
     * 并列题目
     */
    private String tiedTitle;
    /**
     * 分辑名
     */
    private String partName;
    /**
     * 分辑号
     */
    private String partNum;
    /**
     * 丛编名
     */
    private String seriesName;
    /**
     * 著者
     */
    private String author;
    /**
     * 次要责任者
     */
    private String subAuthor;
    /**
     * 丛编编者
     */
    private String seriesEditor;
    /**
     * 译者
     */
    private String translator;
    /**
     * 出版社
     */
    private String publishingName;
    /**
     * 出版地
     */
    private String publishingAddress;
    /**
     * 出版时间
     */
    private String publishingTime;
    /**
     *馆藏地
     */
    private String collectionSiteName;
    /**
     * 分类号
     */
    private String librarsortCode;
    /**
     * 定价
     */
    @JsonSerialize(using = CustomDoubleSerialize.class)
    private Double price;
    /**
     * 版次
     */
    private String edition;
    /**
     * 语种
     */
    private String language;
    /**
     * 尺寸
     */
    private String measure;
    /**
     * 页码
     */
    private String pageNo;
    /**
     *装帧形式
     */
    private String bindingForm;
    /**
     * 适读年龄
     */
    private String bestAge;
    /**
     * 附件备注
     */
    private String attachmentNote;
    /**
     * 主题词
     */
    private String subject;

    private String tanejiNo;
    private String assNo;
    private String bookNo;


}
