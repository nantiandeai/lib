package com.lianyitech.modules.offline.web;

import com.lianyitech.common.web.ApiController;
import com.lianyitech.common.web.ResponseData;
import com.lianyitech.modules.offline.entity.ServerApp;
import com.lianyitech.modules.offline.entity.ServerTime;
import com.lianyitech.modules.offline.service.ServerAppService;
import com.lianyitech.modules.offline.service.ServerTimeService;
import com.lianyitech.modules.offline.utils.VerifyException;
import com.lianyitech.modules.offline.utils.CustomException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import java.util.Date;
import java.util.Iterator;


/**
 * 离线客户端有关web端接口
 * @version 2017-06-22
 */
@Api(value="在线web端接口", description ="离线客户端在线接口")
@RestController
@RequestMapping(value = "/api/web/server")
public class WebManageController extends ApiController {

	@Autowired
	private ServerAppService serverAppService;
	@Autowired
	private ServerTimeService serverTimeService;

	@Secured({"ROLE_lib3school.api.offline.unbind"})
	@RequestMapping(value = "unbind", method = {RequestMethod.POST})
	@ApiOperation(value="解绑", notes = "解绑离线客户端接口", response = ResponseEntity.class)
	public ResponseEntity<ResponseData> unbind() {
		try {
			ServerApp serverApp = serverAppService.unbind();
			return new ResponseEntity<>(success(serverApp), HttpStatus.OK);
		} catch (CustomException e) {
			return new ResponseEntity<>(fail(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e){
			return new ResponseEntity<>(fail("出现异常"), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured({"ROLE_lib3school.api.offline.unbind"})
	@RequestMapping(value = "unbindConfirm", method = {RequestMethod.POST})
	@ApiOperation(value="解绑确认接口", notes = "解绑确认接口", response = ResponseEntity.class)
	public ResponseEntity<ResponseData> unbindConfirm() {
		try{
			String msg = serverAppService.unbindConfirm();
			return new ResponseEntity<>(success(msg), HttpStatus.OK);
		} catch (CustomException e){
			return new ResponseEntity<>(fail(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		catch (Exception e){
			return new ResponseEntity<>(fail("异常"), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured({"ROLE_lib3school.api.offline.auth"})
	@RequestMapping(value = "authConfirm", method = {RequestMethod.POST})
	@ApiOperation(value="身份认证确认", notes = "根据token来确认认证通过", response = ResponseEntity.class)
	public ResponseEntity<ResponseData> authConfirm() {
		try{
			serverAppService.authConfirm();
		} catch (RuntimeException e){
			return new ResponseEntity<>(fail("操作失败"), HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e){
			return new ResponseEntity<>(fail("身份认证确认异常"), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(success("身份认证确认通过"), HttpStatus.OK);
	}
	@Secured({"ROLE_lib3school.api.offline.upload"})
	@RequestMapping(value = "upFile", method = RequestMethod.POST)
	@ApiOperation(value="", notes = "上传流通记录接口", response = ResponseEntity.class)
	public ResponseEntity<ResponseData> uploadFile(MultipartHttpServletRequest multipartRequest, @ApiParam(required =true, name ="用户实体", value="身份认证的相关参数") ServerTime serverTime) {
		serverTime.setCreateDate(new Date());
		for (Iterator<String> it = multipartRequest.getFileNames(); it.hasNext(); ) {
			MultipartFile multiFile = multipartRequest.getFile(it.next());
			try {
				serverTimeService.upCirculateFile(multiFile, serverTime);
			} catch (CustomException e) {
				return new ResponseEntity<>(fail("操作失败"), HttpStatus.INTERNAL_SERVER_ERROR);
			} catch (VerifyException e) {
				return new ResponseEntity<>(fail("302",e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
			} catch (Exception e) {
				return new ResponseEntity<>(fail("上传出现异常"), HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		return new ResponseEntity<>(success("上传成功"), HttpStatus.OK);
	}
}