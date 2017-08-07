package com.lianyitech.modules.lib.web;

import com.lianyitech.common.persistence.Page;
import com.lianyitech.common.web.ApiController;
import com.lianyitech.common.web.ResponseData;
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

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<ResponseData> list(WaitingHandle waitingHandle, HttpServletRequest request, HttpServletResponse response) {
		Page<WaitingHandle> page = waitingHandleService.findPage(new Page<>(request, response), waitingHandle);
		return new ResponseEntity<>(success(page), HttpStatus.OK);
	}

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<ResponseData> save(WaitingHandle waitingHandle) {
		try {
            waitingHandleService.save(waitingHandle);
        } catch (Exception e) {
            logger.error("操作失败",e);
            return new ResponseEntity<>(fail("操作失败"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(success("操作成功"), HttpStatus.OK);
	}

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<ResponseData> delete(@PathVariable("id") String id) {
         try {
            waitingHandleService.delete(id);
        } catch (Exception e) {
             logger.error("操作失败",e);
            return new ResponseEntity<>(fail("删除失败"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(success("删除成功"), HttpStatus.OK);
	}

}