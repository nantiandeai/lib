package com.lianyitech.modules.circulate.entity;

import com.lianyitech.core.utils.DataEntity;

/**
 * 元素坐标DTO
 */
public class PrintDTO extends DataEntity<CirculateLogDTO> {
    private static final long serialVersionUID = 1L;
    private Integer schoolLogoX;//学校校徽-X坐标
    private Integer schoolLogoY;//学校校徽-Y坐标
    private Integer schoolNameX;//学校名称-X坐标
    private Integer schoolNameY;//学校名称-Y坐标
    private Integer titleBorrowCardX;//标题-读者证-X坐标
    private Integer titleBorrowCardY;//标题-读者证-Y坐标
    private Integer cardPhotoX;//读者头像-X坐标
    private Integer cardPhotoY;//读者头像-Y坐标
    private Integer titleNameX;//标题-姓名-X坐标
    private Integer titleNameY;//标题-姓名-Y坐标
    private Integer nameX;//姓名-X坐标
    private Integer nameY;//姓名-Y坐标
    private Integer nameUnderlineX1;//姓名下划线-X1坐标
    private Integer nameUnderlineY1;//姓名下划线Y1坐标
    private Integer nameUnderlineX2;//姓名下划线-X1坐标
    private Integer nameUnderlineY2;//姓名下划线Y1坐标
    private Integer barCodeX;//条码-X坐标
    private Integer barCodeY;//条码-Y坐标
    private Integer barCodeWidth;//条码-宽度
    private Integer outBorderX;//外边框-X坐标
    private Integer outBorderY;//外边框-Y坐标


    CardPrintConfig cardPrintConfig;

    Reader reader ;

    public CardPrintConfig getCardPrintConfig() {
        return cardPrintConfig;
    }

    public void setCardPrintConfig(CardPrintConfig cardPrintConfig) {
        this.cardPrintConfig = cardPrintConfig;
    }

    public Reader getReader() {
        return reader;
    }

    public void setReader(Reader reader) {
        this.reader = reader;
    }

    public Integer getSchoolLogoX() {
        return schoolLogoX;
    }

    public void setSchoolLogoX(Integer schoolLogoX) {
        this.schoolLogoX = schoolLogoX;
    }

    public Integer getSchoolLogoY() {
        return schoolLogoY;
    }

    public void setSchoolLogoY(Integer schoolLogoY) {
        this.schoolLogoY = schoolLogoY;
    }

    public Integer getSchoolNameX() {
        return schoolNameX;
    }

    public void setSchoolNameX(Integer schoolNameX) {
        this.schoolNameX = schoolNameX;
    }

    public Integer getSchoolNameY() {
        return schoolNameY;
    }

    public void setSchoolNameY(Integer schoolNameY) {
        this.schoolNameY = schoolNameY;
    }

    public Integer getTitleBorrowCardX() {
        return titleBorrowCardX;
    }

    public void setTitleBorrowCardX(Integer titleBorrowCardX) {
        this.titleBorrowCardX = titleBorrowCardX;
    }

    public Integer getTitleBorrowCardY() {
        return titleBorrowCardY;
    }

    public void setTitleBorrowCardY(Integer titleBorrowCardY) {
        this.titleBorrowCardY = titleBorrowCardY;
    }

    public Integer getCardPhotoX() {
        return cardPhotoX;
    }

    public void setCardPhotoX(Integer cardPhotoX) {
        this.cardPhotoX = cardPhotoX;
    }

    public Integer getCardPhotoY() {
        return cardPhotoY;
    }

    public void setCardPhotoY(Integer cardPhotoY) {
        this.cardPhotoY = cardPhotoY;
    }

    public Integer getTitleNameX() {
        return titleNameX;
    }

    public void setTitleNameX(Integer titleNameX) {
        this.titleNameX = titleNameX;
    }

    public Integer getTitleNameY() {
        return titleNameY;
    }

    public void setTitleNameY(Integer titleNameY) {
        this.titleNameY = titleNameY;
    }

    public Integer getNameX() {
        return nameX;
    }

    public void setNameX(Integer nameX) {
        this.nameX = nameX;
    }

    public Integer getNameY() {
        return nameY;
    }

    public void setNameY(Integer nameY) {
        this.nameY = nameY;
    }

    public Integer getNameUnderlineX1() {
        return nameUnderlineX1;
    }

    public void setNameUnderlineX1(Integer nameUnderlineX1) {
        this.nameUnderlineX1 = nameUnderlineX1;
    }

    public Integer getNameUnderlineY1() {
        return nameUnderlineY1;
    }

    public void setNameUnderlineY1(Integer nameUnderlineY1) {
        this.nameUnderlineY1 = nameUnderlineY1;
    }

    public Integer getNameUnderlineX2() {
        return nameUnderlineX2;
    }

    public void setNameUnderlineX2(Integer nameUnderlineX2) {
        this.nameUnderlineX2 = nameUnderlineX2;
    }

    public Integer getNameUnderlineY2() {
        return nameUnderlineY2;
    }

    public void setNameUnderlineY2(Integer nameUnderlineY2) {
        this.nameUnderlineY2 = nameUnderlineY2;
    }

    public Integer getBarCodeX() {
        return barCodeX;
    }

    public void setBarCodeX(Integer barCodeX) {
        this.barCodeX = barCodeX;
    }

    public Integer getBarCodeY() {
        return barCodeY;
    }

    public void setBarCodeY(Integer barCodeY) {
        this.barCodeY = barCodeY;
    }

    public Integer getBarCodeWidth() {
        return barCodeWidth;
    }

    public void setBarCodeWidth(Integer barCodeWidth) {
        this.barCodeWidth = barCodeWidth;
    }

    public Integer getOutBorderX() {
        return outBorderX;
    }

    public void setOutBorderX(Integer outBorderX) {
        this.outBorderX = outBorderX;
    }

    public Integer getOutBorderY() {
        return outBorderY;
    }

    public void setOutBorderY(Integer outBorderY) {
        this.outBorderY = outBorderY;
    }
}
