package com.lianyitech.modules.offline.entity;

import com.lianyitech.core.utils.DataEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@ToString
public class ReaderVo extends DataEntity<ReaderVo> {
    private static final long serialVersionUID = 1L;
    /**
     * 机构id
     */
    private String orgId;
    private String name;
    /**
     * 组织机构名称
     */
    private String groupName;
    /**
     * 读者证
     */
    private String card;
    /**
     * 邮箱
     */
    private String email;
    private String phone;		// 手机号码
    /**
     * 证件类型
     */
    private String certType;
    /**
     * 证件号码
     */
    private String certNum;
    /**
     * 读者类型
     */
    private String readerType;
    /**
     * 可借阅册数
     */
    private Integer borrowNumber;
    /**
     * 已借阅册数
     */
    private Integer countBorrow;
    /**
     * 借阅天数
     */
    private Integer borrowDays;
    /**
     * 结束日期
     */
    private Date endDate;
    /**
     * 开始日期
     */
    private Date startDate;
    /**
     *状态
     */
    private String status;

}
