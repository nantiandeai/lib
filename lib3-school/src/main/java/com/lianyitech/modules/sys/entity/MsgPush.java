/**
 *
 */
package com.lianyitech.modules.sys.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lianyitech.core.utils.DataEntity;
import oracle.sql.CLOB;

import java.util.Date;


/**
 * 消息记录Entity
 *
 * @author chenxiaoding
 * @version 2017-01-05
 */
public class MsgPush extends DataEntity<MsgPush> {

    private static final long serialVersionUID = 1L;
    private String content;//推送内容
    private String source;//推送来源
    private String userId;//接受者用户ID
    private String unitId;// 单位ID
    private Date opTime;//操作时间

    private Boolean isHasRecord;//是否看过此消息，是：看过，否：木有

    public Boolean getHasRecord() {
        return isHasRecord;
    }

    public void setHasRecord(Boolean hasRecord) {
        isHasRecord = hasRecord;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    public Date getOpTime() {
        return opTime;
    }

    public void setOpTime(Date opTime) {
        this.opTime = opTime;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }
}