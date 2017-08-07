package com.lianyitech.Proxy;

import com.lianyitech.common.web.ApiController;
import com.lianyitech.common.web.ResponseData;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by jordan jiang on 2017/3/2.
 */
@RestController
@RequestMapping(value = "/pub/crawler")
public class CrawlerController extends ApiController {

    /**
     * 获取最新代理
     *
     * @param
     * @return result
     */
    @RequestMapping(value = "/getProxy", method = RequestMethod.GET)
    public ResponseEntity<ResponseData> getProxy() {
        try {
            IfCanUseProxy.crawlerProxy();
        }catch (Exception e){
            return new ResponseEntity<>(fail("获取代理出错！"), HttpStatus.OK);
        }
        return new ResponseEntity<>(success("获取最新代理完成！"), HttpStatus.OK);
    }
}
