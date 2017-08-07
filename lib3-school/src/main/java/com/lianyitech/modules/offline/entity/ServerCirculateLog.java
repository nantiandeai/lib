package com.lianyitech.modules.offline.entity;

import com.lianyitech.core.utils.DataEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@ToString
public class ServerCirculateLog extends DataEntity<ServerCirculateLog> {
    private static final long serialVersionUID = 1L;

    /**
     * 读者证号
     */
    private String card;
    /**
     * 条形码
     */
    private String barCode;
    /**
     * 机构id
     */
    private String orgId;
    /**
     * 操作类型
     */
    private String opType;
    /**
     * 借书日期
     */
    private Date borrowDate;
    /**
     * 还书日期
     */
    private Date returnDate;
    /**
     * 应还日期
     */
    private Date shouldReturnDate;
    /**
     * 同步时间
     */
    private Date syncDate;
    /**
     * 状态
     */
   private String status;
    /**
     * 上传时间id
     */
    private String serverTimeId;
    /**
     * 给离线客户端同步结果
     */
    private String type;

    /**
     * 同步失败原因
     */
    private String errorInfo;

    /**
     * 上次同步时间
     */
    private Date startDate;
    /**
     * 最后同步时间
     */
    private Date endDate;
    /**
     * 流通类型
     */
    private String dirType;
    private int pageNo = 1; // 当前页码
    private int pageSize = 20; // 页面大小，设置为“-1”表示不进行分页（分页无效）
}
