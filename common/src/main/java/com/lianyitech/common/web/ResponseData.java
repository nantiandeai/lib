package com.lianyitech.common.web;

import com.lianyitech.common.utils.DateUtils;

/**
 * 前后通讯数据格式
 *
 * @version 2016-07-26
 */
public class ResponseData {
    /**
     * 状态码
     * 200：操作成 300：操作失败 400：未登录
     */
    private String code;
    /**
     * 消息提示
     * 默认操作成功
     */
    private String message;
    /**
     * 当前服务器时间
     * 格式yyyy-MM-dd HH:mm:ss
     */
    private String time;
    /**
     * 业务数据
     */
    private Object data;

    public void setData(Object data) {
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public ResponseData() {
        setTime(DateUtils.getDateTime());
    }

    public Object getData() {
        return data;
    }
}
