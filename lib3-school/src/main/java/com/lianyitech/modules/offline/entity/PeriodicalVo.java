package com.lianyitech.modules.offline.entity;

import com.lianyitech.core.utils.DataEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = false)
@ToString
public class PeriodicalVo extends DataEntity<PeriodicalVo> {
    private static final long serialVersionUID = 1L;
    /**
     * 机构id
     */
    private String orgId;
    /**
     * 期刊目录id
     */
    private String periDirectoryId;
    /**
     * 条形码
     */
    private String barCode;
    /**
     * 是否续借
     */
    private String isRenew;
    /**
     * 是否预约
     */
    private String isOrder;
    /**
     * issn
     */
    private String  issn;
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
     * 出版社名
     */
    private String publishingName;
    /**
     * 出版年
     */
    private String publishingYear;
    /**
     * 馆藏地名称
     */
    private String collectionSiteName;
    /**
     * 中图分类
     */
    private String librarsortCode;
    /**
     * 定价
     */
    private Double price;
    /**
     * 统一刊号
     */
    private String periNum;
    /**
     * 邮发代号
     */
    private String emailNum;
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
     * 装订卷期
     */
    private String bindingNum;
    /**
     * 书次号
     */
    private String bookTimeNo;
    /**
     * 辅助区分号
     */
    private String assNo;
    /**
     * 索刊号
     */
    private String somNo;
    /**
     * 过刊状态
     */
    private String status;
    /**
     * 出版频率(枚举类型)
     */
    private String publishingFre;
    /**
     * 开本16开32开
     */
    private String bookSize;

}
