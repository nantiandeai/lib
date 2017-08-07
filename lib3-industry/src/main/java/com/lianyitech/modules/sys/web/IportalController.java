/**
 *
 */
package com.lianyitech.modules.sys.web;

import com.lianyitech.common.utils.StringUtils;
import com.lianyitech.common.web.ApiController;
import com.lianyitech.common.web.ResponseData;
import com.lianyitech.iportal.service.IportalApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

import static com.lianyitech.core.config.ConfigurerConstants.IPORTAL_IPS_ADDR;

/**
 * restful api
 */
@RestController
@RequestMapping(value = "/api/sys/iportal")
public class IportalController extends ApiController {

    @Autowired
    private AbstractEnvironment environment;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    IportalApi iportalApi;

    @Autowired
    CacheManager cacheManager;

    /**
     * 获得当前用户信息
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/userinfo", method = RequestMethod.GET)
    public ResponseEntity<ResponseData> userInfo(HttpServletRequest request) {
        try{
            Map userMap = iportalApi.getUserInfo(request.getHeader("Authorization"),environment.getProperty(IPORTAL_IPS_ADDR),cacheManager);
            return new ResponseEntity<>(success(userMap), HttpStatus.OK);
        }catch (Exception e) {
            logger.error("操作失败",e);
            return new ResponseEntity<>(fail("获取用户信息失败！"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 获取用户菜单/权限串列表
     *
     * @param appId
     * @param isShow 1菜单  0权限串
     * @return
     */
    @RequestMapping(value = "/menulist", method = RequestMethod.GET)
    public ResponseEntity<ResponseData> menuList(String appId, String isShow,HttpServletRequest request) {
        if (StringUtils.isEmpty(isShow)) {
            return new ResponseEntity<>(fail("参数isShow不能为空"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        try{
            List<Map<String,String>> menu = iportalApi.menuList(request.getHeader("Authorization"),appId,environment.getProperty(IPORTAL_IPS_ADDR),cacheManager);
            return new ResponseEntity<>(success(menu), HttpStatus.OK);
        }catch (Exception e) {
            logger.error("操作失败",e);
            return new ResponseEntity<>(fail("获取菜单失败"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
