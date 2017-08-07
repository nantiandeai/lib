package com.lianyitech.modules.pub.web;

import com.lianyitech.common.utils.UploadUtils;
import com.lianyitech.common.web.ApiController;
import com.lianyitech.common.web.ResponseData;
import com.lianyitech.core.interceptor.SubmitToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

import static com.lianyitech.core.config.ConfigurerConstants.FILESHOW_PREFIX;

/**
 * 公用Controller
 *
 * @author zengzy
 * @version 2016-09-02
 */
@RestController
@RequestMapping(value = "/api/pub")
public class PublicController extends ApiController {

    /**
     * 上传文件
     * @return code
     */
    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    public ResponseData uploadFile(HttpServletRequest request) {
        //response.setHeader("X-Frame-Options", "SAMEORIGIN");
        UploadUtils uploadUtils = new UploadUtils();
        String[] infos = uploadUtils.uploadFile(request);
        if ("true".equals(infos[0])) {
            Map<String, String> map = new HashMap<>();
            map.put("path", infos[1]);
            return success(map);
        } else {
            return fail(infos[1]);
        }
    }

    /**
     * 获取防反复提交token公共接口
     * @return token
     */
    @SubmitToken(save = true)
    @RequestMapping(value = "/token", method = RequestMethod.POST)
    public ResponseData token(HttpServletRequest request) {
        return success(request.getSession().getAttribute("token"));
    }
}