package com.lianyitech.common.web;

import com.lianyitech.common.exception.BusinessException;
import com.lianyitech.common.exception.EnumResponseError;
import com.lianyitech.common.utils.StringUtils;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import javax.servlet.http.HttpServletRequest;
import java.net.ConnectException;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by cjw on 2017/4/12.
 */
@RestControllerAdvice
public class GlobalExceptionController {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 若下面没有特别声明，都会走该异常处理方法
     */
    @ExceptionHandler({Exception.class})
    public ResponseEntity<ResponseData> handleException(HttpServletRequest request, Exception e) {
        handlerLog("exception", request, e);
        return errorResponse(EnumResponseError.OTHER_EXCEP, e.getMessage());
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<ResponseData> handleSQLException(HttpServletRequest request, SQLException e) {
        handlerLog("sqlException", request, e);
        return errorResponse(EnumResponseError.SQL_EXCEP, e.getMessage());
    }

    @ExceptionHandler({ConnectException.class})
    public ResponseEntity<ResponseData> handleConnectException(HttpServletRequest request, ConnectException e) {
        handlerLog("connectException", request, e);
        return errorResponse(EnumResponseError.CONNECT_EXCEP, e.getMessage());
    }

    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<ResponseData> handleAccessDeniedException(HttpServletRequest request, AccessDeniedException e) {
        handlerLog("accessDeniedException", request, e);
        if(StringUtils.isNotBlank(request.getRequestURI()) && request.getRequestURI().equals(request.getContextPath()+"/api/web/server/unbind")) {
            return errorResponse("400", "您没有操作该功能的权限");
        }
        return errorResponse(EnumResponseError.ACCESS_DENIED_EXCEP, e.getMessage());
    }

    @ExceptionHandler({BindException.class})
    public ResponseEntity<ResponseData> handleBindException(HttpServletRequest request, BindException e) {
        List<FieldError> fieldErrors=e.getFieldErrors();
        //取第一条验证消息
        String msg="";
        if(CollectionUtils.isNotEmpty(fieldErrors)){
            FieldError fieldError= fieldErrors.get(0);
            msg=fieldError.getField()+fieldError.getDefaultMessage();
        }
        return errorResponse(EnumResponseError.ILLEGAL_PARAMS_EXCEP, msg);
    }

    @ExceptionHandler({MaxUploadSizeExceededException.class})
    public ResponseEntity<ResponseData> handleMaxUploadSizeExceededException(HttpServletRequest request, MaxUploadSizeExceededException e) {
        return errorResponse(EnumResponseError.FILE_EXCEED_EXCEP, "最大"+e.getMaxUploadSize()/(1024 * 1024)+"M");
    }

    @ExceptionHandler({BusinessException.class})
    public ResponseEntity<ResponseData> handleBusinessException(HttpServletRequest request, BusinessException e) {
        return errorResponse(e.getCode(), e.getMessage());
    }

    /**
     * @param enumResponseError 异常枚举类
     * @param extMsg            扩展的错误消息(由应用系统传递)
     * @return
     */
    private ResponseEntity<ResponseData> errorResponse(EnumResponseError enumResponseError, String extMsg) {
        StringBuilder msgBuf = new StringBuilder(enumResponseError.getCode() + ":" + enumResponseError.getMessage());
        if (StringUtils.isNotEmpty(extMsg)) {
            msgBuf.append(",").append(extMsg);
        }
        ResponseData responseData = new ResponseData();
        responseData.setData(null);
        responseData.setCode("300");
        responseData.setMessage(msgBuf.toString());
        ResponseEntity<ResponseData> res = new ResponseEntity<>(responseData, HttpStatus.BAD_REQUEST);
        return res;
    }

    private ResponseEntity<ResponseData> errorResponse(String code, String msg) {
        ResponseData responseData = new ResponseData();
        responseData.setData(null);
        responseData.setCode("300");
        responseData.setMessage(msg);
        ResponseEntity<ResponseData> res = new ResponseEntity<>(responseData, HttpStatus.BAD_REQUEST);
        return res;
    }

    private void handlerLog(String exceptionName, HttpServletRequest request, Exception e) {
        StringBuilder str = new StringBuilder();
        str.append("ly-").append(exceptionName).append(",").append("params:{").append(getParams(request)).append("}");
        logger.error(str.toString(), e);
    }

    private String getParams(HttpServletRequest request) {
        Enumeration enu = request.getParameterNames();
        StringBuilder str = new StringBuilder();
        while (enu.hasMoreElements()) {
            String paraName = (String) enu.nextElement();
            str.append(paraName).append("=").append(request.getParameter(paraName)).append(";");
        }
        return str.toString();
    }

}
