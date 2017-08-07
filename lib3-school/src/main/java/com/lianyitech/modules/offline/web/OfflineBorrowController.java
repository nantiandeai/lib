package com.lianyitech.modules.offline.web;
import com.lianyitech.common.utils.StringUtils;
import com.lianyitech.common.web.ApiController;
import com.lianyitech.common.web.ResponseData;
import com.lianyitech.modules.offline.service.ServerAppService;
import com.lianyitech.modules.offline.utils.CustomException;
import com.lianyitech.modules.offline.utils.VerifyException;
import com.lianyitech.modules.sys.entity.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Map;

/**
 * 离线借阅服务端管理Controller
 * @version 2017-06-16
 */
@Api(value="离线借阅", description ="离线借阅服务端有关接口")
@RestController
@RequestMapping(value = "/api/offline/server")
public class OfflineBorrowController extends ApiController {
	@Autowired
	private ServerAppService serverAppService;

	@RequestMapping(value = "token", method = {RequestMethod.POST})
	@ApiOperation(value="获取token", notes = "根据用户中心用户名密码进行认证:只需要传用户中心的用户名和密码", response = ResponseEntity.class)
	public ResponseEntity<ResponseData> token(@ApiParam(required =true, name ="用户实体", value="获取token的相关参数") @RequestBody User user) {
		try {
			String token = serverAppService.getToken(user);
			if(StringUtils.isNotEmpty(token)){
				return new ResponseEntity<>(success(token), HttpStatus.OK);
			} else{
				return new ResponseEntity<>(fail("没有获取到token"), HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (HttpClientErrorException e){//用户名密码错误
			return new ResponseEntity<>(fail("303",e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e){
			return new ResponseEntity<>(fail("系统异常！"), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@RequestMapping(value = "auth", method = {RequestMethod.POST})
	@ApiOperation(value="身份认证", notes = "根据用户中心用户名密码进行认证:只需要传用户中心的用户名和密码", response = ResponseEntity.class)
	public ResponseEntity<ResponseData> auth(@ApiParam(required =true, name ="用户实体", value="身份认证的相关参数") @RequestBody User user) {
		try {
			return new ResponseEntity<>(success(serverAppService.auth(user)), HttpStatus.OK);
		} catch (CustomException e){
			return new ResponseEntity<>(fail(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}catch (Exception e) {
			return new ResponseEntity<>(fail("系统异常！"), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@RequestMapping(value = "check", method = {RequestMethod.POST})
	@ApiOperation(value="验证接口", notes = "根据用户中心用户名密码进行认验证:只需要传用户中心的用户名和密码", response = ResponseEntity.class)
	public ResponseEntity<ResponseData> check(@ApiParam(required =true, name ="用户实体", value="身份验证的相关参数") @RequestBody Map<String,String> map) {
		try {
		    serverAppService.check(map);
			return new ResponseEntity<>(success("验证成功"), HttpStatus.OK);
		} catch (VerifyException e) {
			return new ResponseEntity<>(fail("302",e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (CustomException e){
			return new ResponseEntity<>(fail(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			return new ResponseEntity<>(fail("系统异常"), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@RequestMapping(value = "", method = RequestMethod.GET)
	@ApiOperation(value="", notes = "得到服务器时间接口", response = ResponseEntity.class)
	public ResponseEntity<ResponseData> getDate() {
		return new ResponseEntity<>(success(), HttpStatus.OK);
	}
}