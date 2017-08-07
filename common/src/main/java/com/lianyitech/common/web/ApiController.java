/**
 *
 */
package com.lianyitech.common.web;

import com.lianyitech.common.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.util.HtmlUtils;

import java.beans.PropertyEditorSupport;
import java.util.Date;

/**
 * 控制器支持类
 *
 * @version 2016-7-20
 */
public abstract class ApiController{

    /**
     * 日志对象
     */
    protected Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 构造操作成功数据包   *
     *
     * @param data    业务数据
     * @param message 提示消息
     * @return
     */
    public ResponseData success(Object data, String message) {
        ResponseData responseData = new ResponseData();
        responseData.setData(data);
        responseData.setCode("200");
        responseData.setMessage(message);
        return responseData;
    }

    /**
     * 构造操作成功数据包
     *
     * @param data
     * @return
     */
    public ResponseData success(Object data) {
        ResponseData responseData = new ResponseData();
        responseData.setData(data);
        responseData.setCode("200");
        responseData.setMessage("操作成功");
        return responseData;
    }

    /**
     * 构造操作成功数据包
     *
     * @param messsage
     * @return
     */
    public ResponseData success(String messsage) {
        ResponseData responseData = new ResponseData();
        responseData.setData(null);
        responseData.setCode("200");
        responseData.setMessage(messsage);
        return responseData;
    }

    /**
     * 构造操作成功数据包
     *
     * @return
     */
    public ResponseData success() {
        ResponseData responseData = new ResponseData();
        responseData.setData(null);
        responseData.setCode("200");
        responseData.setMessage("操作成功");
        return responseData;
    }

    /**
     * 构造操作失败数据包
     *
     * @return
     */
    public ResponseData fail(String message) {
        ResponseData responseData = new ResponseData();
        responseData.setData(null);
        responseData.setCode("300");
        responseData.setMessage(message);
        return responseData;
    }

    /**
     * 构造操作失败数据包
     *
     * @return
     */
    public ResponseData fail() {
        ResponseData responseData = new ResponseData();
        responseData.setData(null);
        responseData.setCode("300");
        responseData.setMessage("操作失败");
        return responseData;
    }



    /**
     * 构造失败数据包
     *
     * @param data
     * @return
     */
    public ResponseData fail(Object data) {
        ResponseData responseData = new ResponseData();
        responseData.setData(data);
        responseData.setCode("300");
        responseData.setMessage("操作失败");
        return responseData;
    }


    public ResponseData fail(String code,String messsage) {
        ResponseData responseData = new ResponseData();
        responseData.setData(null);
        responseData.setCode(code);
        responseData.setMessage(messsage);
        return responseData;
    }
    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        // String类型HTML编码转义
        binder.registerCustomEditor(String.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                setValue(text == null ? null : HtmlUtils.htmlUnescape(text.trim()));
            }

            @Override
            public String getAsText() {
                Object value = getValue();
                return value != null ? value.toString() : "";
            }
        });
        // Date 类型转换
        binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                setValue(DateUtils.parseDate(text));
            }
        });
    }
}
