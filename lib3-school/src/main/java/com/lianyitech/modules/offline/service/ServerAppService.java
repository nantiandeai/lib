package com.lianyitech.modules.offline.service;
import com.lianyitech.common.service.CrudService;
import com.lianyitech.common.utils.IdGen;
import com.lianyitech.common.utils.StringUtils;
import com.lianyitech.core.utils.AuthToken;
import com.lianyitech.core.utils.SystemUtils;
import com.lianyitech.iportal.service.IportalApi;
import com.lianyitech.iportal.service.IportalServiceImpl;
import com.lianyitech.modules.common.SystemService;
import com.lianyitech.modules.offline.dao.ServerAppDao;
import com.lianyitech.modules.offline.dao.ServerSyncDao;
import com.lianyitech.modules.offline.entity.ServerApp;
import com.lianyitech.modules.offline.entity.ServerSync;
import com.lianyitech.modules.offline.utils.*;
import com.lianyitech.modules.sys.entity.User;
import com.lianyitech.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.util.*;
import static com.lianyitech.core.config.ConfigurerConstants.IPORTAL_IPS_ADDR;
import static com.lianyitech.core.config.ConfigurerConstants.OAUTH2_IP;

@Service
@Transactional(readOnly = false)
public class ServerAppService extends CrudService<ServerAppDao,ServerApp> {
    @Autowired
    private Environment environment;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private IportalServiceImpl iportalService;
    @Autowired
    private CacheManager cacheManager;
    @Autowired
    private ServerAppDao serverAppDao;
    @Autowired
    IportalApi iportalApi;

    @Autowired
    ServerSyncDao serverSyncDao;

    @Autowired
    ServerAppCacheService serverAppCacheService;


    /**
     * 验证用户名密码是否有单管权限
     * @param user 用户信息（账号、密码）
     * @return 验证通过返回对用用户信息 否则则是空或者抛异常
     */
    public User validateUser(User user) throws Exception {
        try {
            String token = getToken(user);
            //身份认证成功则需要根据对应的信息取得到用户信息包括机构id
            if(StringUtils.isNotEmpty(token)){
                Map userMap = iportalService.getUserInfo(token,environment.getProperty(IPORTAL_IPS_ADDR),cacheManager);
                SystemUtils.transMapToBean(userMap, user);
                if(user!=null && user.getOrgId()!=null && user.getOrgName()!=null){
                    //验证是否有权限--根据菜单来验证
                    List<Map<String,String>> menu = iportalService.menuList(token,"1",environment.getProperty(IPORTAL_IPS_ADDR),cacheManager);
                    if (menu == null || menu.size() <= 0)
                        throw new CustomException("该用户没有权限！");
                    user.setPassword(token);
                    return user;
                }
            }
        } catch (HttpClientErrorException e) {
            throw  new CustomException("用户名或密码错误!");
        }
        throw  new CustomException("用户名或密码错误!");
    }

    /**
     * 获取token值
     * @param user 用户参数
     * @return 返回token
     */
    public String getToken(User user){
        String authUrl = environment.getProperty(OAUTH2_IP) + "/auth/password/token?username="+user.getLoginName()+"&password="+ SystemService.entryptPassword(user.getPassword())+"&clientId=lib3school_client";
        ResponseEntity<AuthToken> authData = restTemplate.getForEntity(authUrl, AuthToken.class);
        AuthToken authToken;
        if (null != authData && authData.getStatusCode().equals(HttpStatus.OK)) {
            authToken = authData.getBody();
            String token = authToken.getToken_type() + " " + authToken.getAccess_token();
            //身份认证成功则需要根据对应的信息取得到用户信息包括机构id
                if(StringUtils.isNotEmpty(token)){
                   return token;
                }
            }
        return null;
    }


    public User check(Map<String,String> map) throws Exception {
        String certKey = map.get("certKey");
        if(StringUtils.isEmpty(certKey)){
            throw new CustomException("请传认证key");
        }
        User user = new User();
        user.setLoginName(map.get("loginName"));
        user.setPassword(map.get("password"));
        user = validateUser(user);
        user.setPassword(null);
        ServerApp serverApp = new ServerApp();
        serverApp.setCertKey(certKey);
        serverApp.setStatus("0");//设置可用
        List<ServerApp> list = this.listServerApp(serverApp);
        if(list!=null&&list.size()>0){
            serverApp = list.get(0);
            if (serverApp.getOrgId().equals(user.getOrgId()) && serverApp.getUserName().equals(user.getLoginName())) {
                return user;
            } else{
                throw new CustomException("请输入正确的学校账号和密码");
            }
        }
        else{
            throw new VerifyException("该学校没有绑定离线客户端或者已经解绑");
        }
    }
    /**
     * 认证申请接口
     * @param user 用户实体类
     * @return 返回Map 带机构信息的（前端需要得到机构id和机构名称）
     */
    public Map<String,Object> auth(User user) throws Exception {
        user = validateUser(user);
        Map<String,Object> map = new HashMap<>();
        map.put("orgId",user.getOrgId());
        map.put("orgName",user.getOrgName());
        //检查是否此机构离线客户端已经绑定了（根据机构id）
        ServerApp serverApp = new ServerApp();
        serverApp.setOrgId(user.getOrgId());
        serverApp.setStatus("0");//设置可用
        List<ServerApp> list = serverAppDao.getList(serverApp);
        if (list != null && list.size() > 0) {
            throw new CustomException("该学校已经绑定了客户端，不可多个账号绑定!");
        }
        //查询是否申请过-如果申请过直接返回申请过的认证key(以免产生多条记录)
        serverApp.setStatus("1");
        List<ServerApp> listCz = serverAppDao.getList(serverApp);
        if (listCz != null && listCz.size() > 0) {
            serverApp = listCz.get(0);
            serverApp.setCreateDate(new Date());
            serverApp.setUpdateDate(new Date());
            serverAppDao.updateDate(serverApp);
            map.put("certKey", listCz.get(0).getCertKey());
            return map;
        }
        //认证通过需要将信息保存到认证申请表
        String certKey = IdGen.uuid();
        serverApp.setUserName(user.getLoginName());
        serverApp.setStatus("1");//设置不可用
        serverApp.setCertKey(certKey);
        serverApp.setId(IdGen.uuid());
        serverApp.setCreateDate(new Date());
        serverApp.setUpdateDate(new Date());
        serverAppDao.insert(serverApp);
        map.put("certKey", certKey);
        return map;
    }

    /**
     * 解绑接口
     * @return 返回Map
     */
    public ServerApp unbind() throws CustomException {
        //检查是否此机构离线客户端已经绑定了（根据机构id）
        User user = UserUtils.getUser();
        if(user==null || StringUtils.isEmpty(user.getOrgId())){
            throw new CustomException("没有登陆不能解绑");
        }
        ServerApp serverApp = new ServerApp();
        serverApp.setOrgId(user.getOrgId());
        serverApp.setStatus("0");//设置可用
        List<ServerApp> list = serverAppDao.getList(serverApp);
        if (list == null || list.size() <= 0) {
            throw new CustomException("您所在的学校没有绑定客户端");
        }
        serverApp = list.get(0);
        serverApp.setOrgName(user.getOrgName());
        serverApp.setCertKey(null);
        return serverApp;
    }

    /**
     * 解绑确认接口
     * @return 返回参数
     */
    public String unbindConfirm() throws CustomException {
        User user = UserUtils.getUser();
        if(user==null){
            throw new CustomException("没有登陆不能解绑");
        }
        //认证通过需要将信息保存到认证申请表
        ServerApp serverApp = new ServerApp();
        serverApp.setOrgId(user.getOrgId());
        serverApp.setStatus("1");//设置不可用
        serverApp.setUpdateDate(new Date());
        serverAppDao.update(serverApp);
        //将同步记录清掉
        ServerSync serverSync = new ServerSync();
        serverSync.setOrgId(user.getOrgId());
        serverSyncDao.delete(serverSync);
        //需要将对应的文件删掉
        delFileDirectory(user.getOrgId());
        return "解绑成功";
    }

    /**
     * 认证确认接口
     */
    public void authConfirm(){
        ServerApp serverApp = new ServerApp();
        serverApp.setOrgId(UserUtils.getOrgId());
        List<ServerApp> list = serverAppDao.getList(serverApp);
        if(list!=null && list.size()>0){
            ServerApp obj = list.get(0);
            if (obj != null && "0".equals(obj.getStatus())) {
                throw new RuntimeException("此学校离线客户端已经绑定了！");
            }
            //修改认证申请为可用状态
            serverApp.setStatus("0");
            serverApp.setUpdateDate(new Date());
            serverAppDao.update(serverApp);
            //将同步记录清掉
            ServerSync serverSync = new ServerSync();
            serverSync.setOrgId(serverApp.getOrgId());
            serverSyncDao.delete(serverSync);

        }else{
            throw new RuntimeException("不存在此离线客户端申请记录");
        }
    }

    /**
     * 查询所有注册了离线客户端的信息
     * @return list 认证申请集合
     */
    public List<ServerApp> listServerApp(ServerApp serverApp){
        return serverAppDao.getList(serverApp);
    }
    /**
     * 删除对于文件目录
     * @param orgId 机构id
     * @return true or false
     */
   private boolean delFileDirectory(String orgId){
       String uploadPath = String.join(File.separator, new String[]{environment.getProperty("upload.linux.path"), "offline", environment.getProperty("config.area"), orgId});
       File file = new File(uploadPath);
       return file.exists()&&file.isDirectory()&&OfflineFileUtils.DeleteFolder(uploadPath);
   }
    /**
     * 从缓存获取数据返回，若没有取到足够的数据则需要查询数据库的状态是否已经完成，如果完成则续传标志为0
     * @param type 下载类型 1 读者 2 馆藏 3流通同步结果 4期刊
     * @return map 集合
     */
    public Map<String,Object> getData(String type) {
        Map<String,Object> resultMap = new HashMap<>();
        String hasMore = "1";
        String orgId = UserUtils.getOrgId();
        String status =  serverAppCacheService.get(orgId+"_"+type+"_status");
        List<String> dataList = serverAppCacheService.getUpData(orgId,type,500);
        if(dataList.size()<500) {
            ServerSync serverSync = new ServerSync();
            serverSync.setOrgId(orgId);
            serverSync.setType(type);
            if(StringUtils.isNotBlank(status) && status.equals("1")) {
                hasMore = "0";
            }
        }
        resultMap.put("data",dataList);
        resultMap.put("hasMore",hasMore);
        return resultMap;
    }


}
