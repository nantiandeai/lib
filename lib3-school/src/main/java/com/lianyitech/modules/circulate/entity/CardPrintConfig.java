/**
 *
 */
package com.lianyitech.modules.circulate.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.lianyitech.core.enmu.EnumReaderType;
import com.lianyitech.core.utils.CustomDoubleSerialize;
import com.lianyitech.core.utils.DataEntity;

/**
 * 读者证打印设置Entity
 *
 * @author zengzy
 * @version 2016-09-07
 */
public class CardPrintConfig extends DataEntity<CardPrintConfig> {
    private static final long serialVersionUID = 1L;
    private String compName;//单位名称
    private String cardName;//证件名称
    private String compFont;//单位名称-字体
    private Integer compFontSize;//单位名称-字体大小
    private String cardFont;//证件名称-字体
    private Integer cardFontSize;//证件名称-字体大小
    private String schoolBadge;//校徽图片
    private String printImage;//是否打印读者证照片
    private String orgId;

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getCompName() {
        return compName;
    }

    public void setCompName(String compName) {
        this.compName = compName;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getCompFont() {
        return compFont;
    }

    public void setCompFont(String compFont) {
        this.compFont = compFont;
    }

    public Integer getCompFontSize() {
        return compFontSize;
    }

    public void setCompFontSize(Integer compFontSize) {
        this.compFontSize = compFontSize;
    }

    public String getCardFont() {
        return cardFont;
    }

    public void setCardFont(String cardFont) {
        this.cardFont = cardFont;
    }

    public Integer getCardFontSize() {
        return cardFontSize;
    }

    public void setCardFontSize(Integer cardFontSize) {
        this.cardFontSize = cardFontSize;
    }

    public String getSchoolBadge() {
        return schoolBadge;
    }

    public void setSchoolBadge(String schoolBadge) {
        this.schoolBadge = schoolBadge;
    }

    public String getPrintImage() {
        return printImage;
    }

    public void setPrintImage(String printImage) {
        this.printImage = printImage;
    }
}