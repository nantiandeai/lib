/**
 *
 */
package com.lianyitech.modules.sys.web;

import com.lianyitech.common.web.ApiController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 字典管理Controller
 *
 * @author zengzy
 * @version 2016-09-02
 */
@RestController
@RequestMapping(value = "/api/sys/enmu")
public class EnmuController extends ApiController {

    /**
     * 根据枚举类型获取相应的列表值
     *
     * @param type
     * @return
     */
    @RequestMapping(value = "/{type}", method = RequestMethod.GET)
    public ResponseEntity<?> list(@PathVariable("type") String type) {
        List<?> list = new ArrayList<>();
        try {
            Class cls = Class.forName("com.lianyitech.core.enmu." + type);
            Method method = cls.getMethod("list");
            list = (List<?>) method.invoke(null);
        } catch (Exception e) {

        }
        return new ResponseEntity<List<?>>(list, HttpStatus.OK);
    }


}