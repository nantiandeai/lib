/**
 * 
 */
package com.lianyitech.modules.lib.web;

import com.lianyitech.common.persistence.Page;
import com.lianyitech.common.web.ApiController;
import com.lianyitech.modules.lib.entity.WaitingHandle;
import com.lianyitech.modules.lib.service.WaitingHandleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

/**
 * 待办管理Controller
 * @author zengzy
 * @version 2016-09-09
 */
@RestController
@RequestMapping(value = "/api/lib/waitingHandle")
public class WaitingHandleController extends ApiController {

	@Autowired
	private WaitingHandleService waitingHandleService;
	
	//@RequiresPermissions("lib:waitingHandle:view")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<?> list(WaitingHandle waitingHandle, HttpServletRequest request, HttpServletResponse response) {
		Page<WaitingHandle> page = waitingHandleService.findPage(new Page<WaitingHandle>(request, response), waitingHandle);
		return new ResponseEntity<Page>(page, HttpStatus.OK);
	}

    //@RequiresPermissions("lib:waitingHandle:edit")
    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<?> save(WaitingHandle waitingHandle) {
        HashMap<String, Object> result = new HashMap<>();
        try {
            waitingHandleService.save(waitingHandle);
            result.put("code",0);
            result.put("message","操作成功");
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("操作失败",e);
            result.put("code",-1);
            result.put("message","操作失败");
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //@RequiresPermissions("lib:waitingHandle:edit")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable("id") String id) {
        try {
            waitingHandleService.delete(id);
            return new ResponseEntity<>("删除成功", HttpStatus.OK);
        } catch (Exception e) {
            logger.error("操作失败",e);
            return new ResponseEntity<>("删除失败", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}