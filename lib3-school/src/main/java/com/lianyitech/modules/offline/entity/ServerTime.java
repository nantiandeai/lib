package com.lianyitech.modules.offline.entity;

import com.lianyitech.core.utils.DataEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@ToString
public class ServerTime extends DataEntity<ServerTime> {
    private static final long serialVersionUID = 1L;
    /**
     * 机构id
     */
    private String orgId;
    /**
     * 客户端时间
     */
   private Date  clientDate;
    /**
     * 认证key
     */
   private String certKey;
    /**
     * 专门针对上传传参数字符串
     */
    private String conStr;
}
