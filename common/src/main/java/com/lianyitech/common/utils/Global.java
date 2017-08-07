/**
 *
 */
package com.lianyitech.common.utils;

import com.google.common.collect.Maps;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * 全局配置类
 *
 * @version 2016-07-15
 */
@Service
@Lazy(false)
public class Global implements ApplicationContextAware {

    /**
     * 当前对象实例
     */
    private static Global global = new Global();

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Global.applicationContext = applicationContext;
    }





    /**
     * 保存全局属性值
     */
    private static Map<String, String> map = Maps.newHashMap();

    /**
     * 属性文件加载对象
     */
    private static PropertiesLoader loader = new PropertiesLoader("project.properties");

    /**
     * 显示/隐藏
     */
    public static final String SHOW = "1";
    public static final String HIDE = "0";

    /**
     * 是/否
     */
    public static final String YES = "1";
    public static final String NO = "0";

    /**
     * 对/错
     */
    public static final String TRUE = "true";
    public static final String FALSE = "false";

    /**
     *  上传文件基础路径
     */
    public static final String UPLOADFILES_BASE_URL = "/uploadfiles";

    /**
     * 获取当前对象实例
     */
    public static Global getInstance() {
        return global;
    }

    /**
     * 获取配置
     *
     * @see 2
     */
    public static String getConfig(String key) {
        Environment environment = (Environment) applicationContext.getBean("environment");
        return environment.getProperty(key);
    }


    public static <T> T getBin(String binName){
        return (T)applicationContext.getBean(binName);
    }


    /**
     * 是否是演示模式，演示模式下不能修改用户、角色、密码、菜单、授权
     */
    public static Boolean isDemoMode() {
        String dm = getConfig("demoMode");
        return "true".equals(dm) || "1".equals(dm);
    }


    /**
     * 页面获取常量
     *
     * @see ${fns:getConst('YES')}
     */
    public static Object getConst(String field) {
        try {
            return Global.class.getField(field).get(null);
        } catch (Exception e) {
            // 异常代表无配置，这里什么也不做
        }
        return null;
    }


    /**
     * 获取工程路径
     *
     * @return
     */
    public static String getProjectPath() {
        // 如果配置了工程路径，则直接返回，否则自动获取。
        String projectPath = Global.getConfig("projectPath");
        if (StringUtils.isNotBlank(projectPath)) {
            return projectPath;
        }
        try {
            File file = new DefaultResourceLoader().getResource("").getFile();
            if (file != null) {
                while (true) {
                    File f = new File(file.getPath() + File.separator + "src" + File.separator + "main");
                    if (f == null || f.exists()) {
                        break;
                    }
                    if (file.getParentFile() != null) {
                        file = file.getParentFile();
                    } else {
                        break;
                    }
                }
                projectPath = file.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return projectPath;
    }


    //根据操作系统获取上传文件根目录
    public static String getUploadRootPath() {
        String os = MacUtils.getOSName();
        if (os != null && (os.startsWith("win") || os.startsWith("Win"))) {
            return Global.getConfig("upload.windows.path");
        }
        return Global.getConfig("upload.linux.path");
    }


    /**
     * 获取URL后缀
     */
    public static String getUrlSuffix() {
        return ".html";
    }

    /**
     * 视图文件前缀
     *
     * @return
     */
    public static String getWebViewPrefix() {
        return "/WEB-INF/views/";
    }

    /**
     * 视图文件后缀
     *
     * @return
     */
    public static String getWebViewSuffix() {
        return ".jsp";
    }

    /**
     * 获取文件访问前缀
     *
     * @return
     */
    public static String getFilePrefix() {
        return Global.getConfig("file.prefix");
    }

}
