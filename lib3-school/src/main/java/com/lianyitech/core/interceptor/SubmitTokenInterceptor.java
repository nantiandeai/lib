package com.lianyitech.core.interceptor;

import com.lianyitech.common.utils.JsonMapper;
import com.lianyitech.common.utils.StringUtils;
import com.lianyitech.common.web.ResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.UUID;

/**
 * 防反复提交token拦截器
 */
public class SubmitTokenInterceptor extends HandlerInterceptorAdapter {
    protected static Logger logger = LoggerFactory.getLogger(SubmitTokenInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //POST|PUT|PATCH请求不拦截
        String methods = "POST|PUT|PATCH";
        if (!methods.contains(request.getMethod().toUpperCase()) || request.getContextPath().contains("/api/pub/token")) {
            return true;
        }

        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            SubmitToken annotation = method.getAnnotation(SubmitToken.class);
            if (annotation != null) {
                boolean needSaveSession = annotation.save();
                if (needSaveSession) {
                    request.getSession(true).setAttribute("token", UUID.randomUUID().toString());
                }
                boolean needRemoveSession = annotation.remove();
                if (needRemoveSession) {
                    if (isRepeatSubmit(request)) {
                        ResponseData responseData = new ResponseData();
                        responseData.setData(null);
                        responseData.setCode("300");
                        responseData.setMessage("请不要反复提交");
                        response.setStatus(HttpStatus.BAD_REQUEST.value());
                        JsonMapper.renderJson(response, responseData);
                        return false;
                    }
                    request.getSession(true).removeAttribute("token");
                }
            }
            return true;
        } else {
            return super.preHandle(request, response, handler);
        }
    }

    private boolean isRepeatSubmit(HttpServletRequest request) {
        //服务端保存的token
        String serverToken = (String) request.getSession(true).getAttribute("token");
        if (StringUtils.isEmpty(serverToken)) {
            return true;
        }

        //客户端传递的token
        String clinetToken = request.getParameter("token");
        if (StringUtils.isEmpty(clinetToken)) {
            return true;
        }

        if (!serverToken.equals(clinetToken)) {
            return true;
        }
        return false;
    }
}