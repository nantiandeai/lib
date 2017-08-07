package com.lianyitech.modules.offline.entity;

import com.lianyitech.core.utils.DataEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@ToString
public class ServerSync extends DataEntity<ServerSync> {
    private static final long serialVersionUID = 1L;
    /**
     * 机构id
     */
    private String orgId;
    /**
     * 上次同步时间
     */
    private Date startDate;
    /**
     * 最后同步时间
     */
    private Date endDate;
    /**
     * 同步状态
     */
    private String status;
    /**
     * 数据类型
     */
    private String type;

    private int pageNo = 1; // 当前页码
    private int pageSize = 20; // 页面大小，设置为“-1”表示不进行分页（分页无效）
}
