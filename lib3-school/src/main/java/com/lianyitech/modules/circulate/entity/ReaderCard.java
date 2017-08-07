/**
 * 
 */
package com.lianyitech.modules.circulate.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.lianyitech.core.utils.CustomDoubleSerialize;
import com.lianyitech.core.utils.DataEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 读者证管理Entity
 * @author zengzy
 * @version 2016-09-09
 */
@Getter
@Setter
public class ReaderCard extends DataEntity<ReaderCard> {

	private static final long serialVersionUID = 1L;
    /**
     * 读者id
     */
	private String readerId;
    /**
     * 状态
     */
	private String status;
    /**
     * 办证日期
     */
	private Date startDate;
    /**
     * 终止日期
     */
	private Date endDate;
	private String card;
	private String[] idList;
    /**
     * 密码
     */
	private String password;
    /**
     * 押金
     */
    @JsonSerialize(using = CustomDoubleSerialize.class)
    private Double deposit;
    @JsonIgnore
	private String appId;
    /**
     * sysDate-endDate时间差1为正，0为负
     */
    @JsonIgnore
	private String times;
    /**
     * 读者证状态可操作行为 0,1,2,3
     */
	private String statusAction;
    /**
     * 换证时的新读者证
     */
    @JsonIgnore
	private String newCard;
    /**
     * 加一个机构字段--到时候所有的查询都需要跟机构id关联
     */
	private String orgId;
    @JsonIgnore
	private String keyWords;
    /**
     * 借阅规则id
     */
    @JsonIgnore
	private String ruleId;

}