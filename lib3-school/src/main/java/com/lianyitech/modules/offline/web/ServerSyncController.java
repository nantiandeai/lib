package com.lianyitech.modules.offline.web;
import com.lianyitech.common.utils.StringUtils;
import com.lianyitech.common.web.ApiController;
import com.lianyitech.common.web.ResponseData;
import com.lianyitech.modules.offline.service.ServerAppService;
import com.lianyitech.modules.offline.service.ServerSyncService;
import com.lianyitech.modules.offline.utils.VerifyException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;


/**
 * 离线客户端数据同步---将之前生成文件下载形式改成redis缓存形式
 * 此控制层需要权限
 * @version 2017-07-11
 */
@Api(value="在线web端接口", description ="离线客户端同步接口")
@RestController
@RequestMapping(value = "/api/offline/sync")
public class ServerSyncController extends ApiController {
    @Autowired
    private ServerSyncService serverSyncService;
    @Autowired
    private ServerAppService serverAppService;

    @Secured({"ROLE_lib3school.api.offline.download"})
    @RequestMapping(value = "preSync", method = RequestMethod.POST)
    @ApiOperation(value="", notes = "数据同步接口", response = ResponseEntity.class)
    public ResponseEntity<ResponseData> preSync() {
        try {
            return new ResponseEntity<>(success(serverSyncService.preSync()), HttpStatus.OK);
        } catch (VerifyException e) {
            return new ResponseEntity<>(fail("302",e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }catch (Exception e) {
            return new ResponseEntity<>(fail("系统异常"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @Secured({"ROLE_lib3school.api.offline.download"})
    @RequestMapping(value = "upData", method = RequestMethod.POST)
    @ApiOperation(value="", notes = "数据下载接口", response = ResponseEntity.class)
    public ResponseEntity<ResponseData> upData(@ApiParam(required =true, name ="请求数据类型", value="1：读者 2：馆藏 3：流通")String type) {
        if(StringUtils.isBlank(type)) {
            return new ResponseEntity<>(fail("缺少参数：type"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        Map map = serverAppService.getData(type);
        return new ResponseEntity<>(success(map), HttpStatus.OK);
    }
}