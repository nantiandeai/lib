package com.lianyitech.modules.circulate.entity;

import com.lianyitech.core.utils.DataEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 读者证补缺实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString
public class SupplyCard extends DataEntity<SupplyCard> {
    private static final long serialVersionUID = 1L;
    /**
     * 机构id
     */
    private String orgId;
    /**
     * 读者证
     */
    private String card;
    /**
     * 读者名称
     */
    private String name;
    /**
     * 组织id
     */
    private String groupId;
    /**
     * 组织名称
     */
    private String groupName;
    /**
     *性别
     */
    private String sex;
    /**
     * 读者证状态
     */
    private String status;
    /**
     * 读者证状态名称
     */
    private String statusName;

}
