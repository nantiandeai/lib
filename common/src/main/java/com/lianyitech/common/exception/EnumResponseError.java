package com.lianyitech.common.exception;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.HashMap;
import java.util.Map;

/**
 * 异常编码枚举类
 *
 * @version 2017-04-13
 */
public enum EnumResponseError {
    ILLEGAL_PARAMS_EXCEP("ILLEGAL_PARAMS_EXCEP", "参数错误"),
    SQL_EXCEP("SQL_EXCEP", "操作失败"),
    CONNECT_EXCEP("CONNECT_EXCEP", "连接异常"),
    ACCESS_DENIED_EXCEP("ACCESS_DENIED_EXCEP", "没有权限"),
    FILE_EXCEED_EXCEP("FILE_EXCEED_EXCEP", "文件大小超过限制"),
    OTHER_EXCEP("OTHER_EXCEP", "其他错误");

    EnumResponseError(String code, String message) {
        this.code = code;
        this.message = message;
    }

    private String code;

    private String message;

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
