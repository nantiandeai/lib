/**
 * 
 */
package com.lianyitech.common.persistence.interceptor;

import com.lianyitech.common.persistence.Page;
import com.lianyitech.common.persistence.dialect.Dialect;
import com.lianyitech.common.persistence.dialect.db.*;
import com.lianyitech.common.utils.Global;
import com.lianyitech.common.utils.Reflections;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.plugin.Interceptor;

import java.io.Serializable;
import java.util.Properties;

/**
 * Mybatis分页拦截器基类
 *  / zengzy
 * @version 2016-07-28
 */
public abstract class BaseInterceptor implements Interceptor, Serializable {
	
	private static final long serialVersionUID = 1L;

    protected static final String PAGE = "page";
    
    protected static final String DELEGATE = "delegate";

    protected static final String MAPPED_STATEMENT = "mappedStatement";

    protected Log log = LogFactory.getLog(this.getClass());

    protected Dialect DIALECT;

//    /**
//     * 拦截的ID，在mapper中的id，可以匹配正则
//     */
//    protected String _SQL_PATTERN = "";

    /**
     * 对参数进行转换和检查
     * @param parameterObject 参数对象
     * @param page            分页对象
     * @return 分页对象
     * @throws NoSuchFieldException 无法找到参数
     */
    @SuppressWarnings("unchecked")
	protected static Page<Object> convertParameter(Object parameterObject, Page<Object> page) {
    	try{
            if (parameterObject instanceof Page) {
                return (Page<Object>) parameterObject;
            } else {
                return (Page<Object>) Reflections.getFieldValue(parameterObject, PAGE);
            }
    	}catch (Exception e) {
			return null;
		}
    }

    /**
     * 设置属性，支持自定义方言类和制定数据库的方式
     * <code>dialectClass</code>,自定义方言类。可以不配置这项
     * <ode>dbms</ode> 数据库类型，插件支持的数据库
     * <code>sqlPattern</code> 需要拦截的SQL ID
     * @param p 属性
     */
    protected void initProperties(Properties p) {
        DIALECT = new OracleDialect();
    }
}
