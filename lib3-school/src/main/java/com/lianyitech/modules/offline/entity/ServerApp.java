package com.lianyitech.modules.offline.entity;

import com.lianyitech.core.utils.DataEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = false)
@ToString
public class ServerApp extends DataEntity<ServerApp> {
    private static final long serialVersionUID = 1L;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 机构id
     */
    private String orgId;
    /**
     * 认证key
     */
    private String certKey;
    /**
     * 状态
     */
    private String status;
    /**
     * 机构名称
     */
    private String orgName;
}
