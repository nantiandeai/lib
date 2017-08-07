package com.lianyitech.core.interceptor;

import com.lianyitech.common.service.CrudService;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * url去空格拦截器
 * 
 * @version 2016-11-17
 */
@Intercepts({@Signature(type = Executor.class, method = "",
		args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})})
public class ParamsInterceptor extends CrudService implements HandlerInterceptor {


	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, 
			Object handler) throws Exception {
		Map<String, String[]> ajaxMap = request.getParameterMap();
//		for(Map.Entry<String, String[]> entry : ajaxMap.entrySet()){
//			String[] value = entry.getValue();
//			if(value!=null){
//				for(int i = 0 ; i < value.length ; i ++) {
//					value[i] = StringUtils.trim(value[i]);
//				}
//			}
//		}
		return true;
	}



	@Override
	public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

	}

	@Override
	public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

	}


}
