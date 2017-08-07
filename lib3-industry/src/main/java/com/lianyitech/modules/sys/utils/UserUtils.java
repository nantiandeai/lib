/**
 *
 */
package com.lianyitech.modules.sys.utils;

import com.lianyitech.common.utils.SpringContextHolder;
import com.lianyitech.core.utils.SystemUtils;
import com.lianyitech.iportal.service.IportalApi;
import com.lianyitech.modules.report.dao.AreaDao;
import com.lianyitech.modules.sys.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

import static com.lianyitech.core.config.ConfigurerConstants.IPORTAL_IPS_ADDR;

/**
 * 用户工具类
 *
 * @version 2016-07-05
 */
public class UserUtils {
    private  static Logger logger = LoggerFactory.getLogger(UserUtils.class);
    private static AreaDao areaDao = SpringContextHolder.getBean(AreaDao.class);
    private static AbstractEnvironment environment = SpringContextHolder.getBean(AbstractEnvironment.class);
    private static CacheManager cacheManager = SpringContextHolder.getBean(CacheManager.class);
    private static IportalApi iportalApi = SpringContextHolder.getBean(IportalApi.class);
    private static RestTemplate restTemplate;

    static {
        restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
    }

    /**
     * 获取当前登录用户
     *
     * @return
     */
    public static User getUser() {
        SecurityContext securityContex = SecurityContextHolder.getContext();
        Authentication authentication = securityContex.getAuthentication();
        if(authentication==null) {
            return null;
        }
        if (!authentication.getName().equals("anonymousUser")) {
            User user = new User();
            try{
                Map userMap = iportalApi.getUserInfo(getAuthentication(),environment.getProperty(IPORTAL_IPS_ADDR),cacheManager);
                SystemUtils.transMapToBean(userMap, user);
                String orgType = areaDao.findTypeByCode(user.getOrgId());
                user.setOrgType( orgType == null ? user.getOrgType() : orgType);
            }catch (Exception e) {
                logger.error("操作失败",e);
            }
            return user;
        }
        return null;
    }

    /**
     * 获取 authentication
     *
     * @return
     */
    public static String getAuthentication() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return request.getHeader("Authorization");
    }

    /**
     * 获取当前用户id
     *
     * @return
     */
    public static String getUserId() {
        String userId = "";
        User user = UserUtils.getUser();
        if (null != user) {
            userId = user.getId();
        }
        return userId;
    }

    /**
     * 获取当前用户机构id
     *
     * @return
     */
    public static String getOrgId() {
        String orgId = "";
        User user = UserUtils.getUser();
        if (null != user) {
            orgId = user.getOrgId();
        }
        return orgId;
    }


    /**
     * 获取登录用户名
     *
     * @return
     */
    public static String getLoginName() {
        SecurityContext securityContex = SecurityContextHolder.getContext();
        Authentication authentication = securityContex.getAuthentication();
        String loginName = authentication.getName();
        return loginName;
    }

}
