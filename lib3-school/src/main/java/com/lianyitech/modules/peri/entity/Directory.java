package com.lianyitech.modules.peri.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lianyitech.common.utils.DoubleUtils;
import com.lianyitech.common.utils.StringUtils;
import com.lianyitech.core.enmu.EnumPeriFrequency;
import com.lianyitech.core.utils.DataEntity;
import com.lianyitech.core.utils.SystemUtils;
import jdk.nashorn.internal.ir.annotations.Ignore;

/**
 * Created by zcx on 2017/3/9.
 * 期刊目录表
 */
public class Directory extends DataEntity<Directory> {
    private static final long serialVersionUID = 1L;
    /**
     * 机构id
     */
    private String orgId;
    /**
     * issn
     */
    private String issn;
    /**
     * 统一刊号
     */
    private String periNum;
    /**
     * 邮发代号
     */
    private String emailNum;
    /**
     * 刊名
     */
    private String title;
    /**
     * 副刊名
     */
    private String subTitle;
    /**
     * 著者
     */
    private String author;
    /**
     * 出版社id
     */
    private String pressId;
    /**
     * 中图分类
     */
    private String librarsortCode;
    /**
     * 中图分类
     */
    private String librarsortId;
    /**
     * 定价
     */
    private String price;
    /**
     * 开本16开32开
     */
    private String bookSize;
    /**
     * 出版频率(枚举类型)
     */
    private String publishingFre;
    /**
     * 出版频率名称(枚举类型)
     */
    private String publishingFreName;
    /**
     * 语种(枚举类型)
     */
    private String language;
    /**
     * 级别(枚举类型)
     */
    private String lev;
    /**
     * 刊期类型(枚举类型)
     */
    private String periType;
    /**
     * 马克字段
     */
    private String marc64;
    /**
     * 出版社名
     */
    private String publishingName;
    /**
     * 内容简介
     */
    private String content;
    /**
     * 关键字
     */
    private String keyWord;
    /**
     * 期刊复本数 有为true 没有为false
     */
    private Boolean bookNum;
    /**
     * 馆藏地id
     */
    private String collectionSiteId;

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

    public Boolean getBookNum() {
        return bookNum;
    }

    public void setBookNum(Boolean bookNum) {
        this.bookNum = bookNum;
    }

    @JsonIgnore
    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
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

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getIssn() {
        return issn;
    }

    public void setIssn(String issn) {
        if(issn.length()==9){
            if(StringUtils.isNotEmpty(issn) && issn.substring(4,5).equals("—")){
                issn = issn.replaceFirst("—","-");
            }
            if(issn.substring(8,9).equals("x")){
                issn = issn.replace("x","X");
            }
        }
        this.issn = issn;
    }

    public String getPeriNum() {
        return periNum;
    }

    public void setPeriNum(String periNum) {
        //把中文－- 和／/换成英文的,让他们存在数据库是英文的，匹配的时候，也是
        if(StringUtils.isNotEmpty(periNum)){
            periNum = SystemUtils.changeChineseSymbol(periNum);
        }
        this.periNum = periNum;
    }

    public String getEmailNum() {
        return emailNum;
    }

    public void setEmailNum(String emailNum) {
        this.emailNum = emailNum;
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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPressId() {
        return pressId;
    }

    public void setPressId(String pressId) {
        this.pressId = pressId;
    }

    public String getPrice() {
        if(StringUtils.isNotBlank(price)) {
            Double p = Double.valueOf(price);
            return DoubleUtils.formatDoubleToString(p);
        }
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getBookSize() {
        return bookSize;
    }

    public void setBookSize(String bookSize) {
        this.bookSize = bookSize;
    }

    public String getPublishingFre() {
        return publishingFre;
    }

    public void setPublishingFre(String publishingFre) {
        this.publishingFre = publishingFre;
    }

    public String getPublishingFreName() {
        return EnumPeriFrequency.parse(getPublishingFre()).getName();
    }

    public void setPublishingFreName(String publishingFreName) {
        this.publishingFreName = publishingFreName;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getLev() {
        return lev;
    }

    public void setLev(String lev) {
        this.lev = lev;
    }

    public String getPeriType() {
        return periType;
    }

    public void setPeriType(String periType) {
        this.periType = periType;
    }

}
