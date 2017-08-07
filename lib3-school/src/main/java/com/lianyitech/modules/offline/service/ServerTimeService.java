package com.lianyitech.modules.offline.service;
import com.lianyitech.common.service.CrudService;
import com.lianyitech.common.utils.IdGen;
import com.lianyitech.modules.offline.dao.ServerAppDao;
import com.lianyitech.modules.offline.dao.ServerTimeDao;
import com.lianyitech.modules.offline.entity.ServerApp;
import com.lianyitech.modules.offline.entity.ServerSync;
import com.lianyitech.modules.offline.entity.ServerTime;
import com.lianyitech.modules.offline.utils.VerifyException;
import com.lianyitech.modules.offline.utils.CustomException;
import com.lianyitech.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.lianyitech.core.config.ConfigurerConstants.LIB3_EXPORT_PAGESIZE;

@Service
public class ServerTimeService extends CrudService<ServerTimeDao,ServerTime> {
    @Autowired
    private ServerAppDao serverAppDao;
    @Resource
    private ServerCirculateLogService serverCirculateLogService;
    @Autowired
    private ServerTimeDao serverTimeDao;
    @Autowired
    private Environment environment;
    @Autowired
    private CreateDataService createDataService;
    /**
     * 流通记录上传
     * @param multiFile 上传文件
     * @param serverTime 上传参数
     * @throws IOException 异常
     */
    @Transactional
    public void upCirculateFile(MultipartFile multiFile, ServerTime serverTime) throws CustomException, IOException, VerifyException {
        String orgId = UserUtils.getOrgId();
        if (serverTime != null && serverTime.getConStr() != null) {
            String conStr = serverTime.getConStr();
            try {
                serverTime.setClientDate(new SimpleDateFormat("yyyy-MM-dd/HH:mm:ss").parse(conStr));
            } catch (ParseException e) {
                logger.error("客户端时间传的错误");
            }
        }
        if(!(serverTime!=null && serverTime.getClientDate()!=null)){
            throw new CustomException("请传入对应的参数：客户端时间");
        }
        String fileName = multiFile.getOriginalFilename();
        String extension = fileName.substring(fileName.lastIndexOf("."), fileName.length());
        final List<String> extensionList = Arrays.asList(".txt");
        if(extensionList.indexOf(extension) == -1) {
            throw new CustomException("不支持的类型，支持txt格式。");
        }
        //根据认证key得到机构id
        ServerApp app = new ServerApp();
        app.setOrgId(orgId);
        app.setStatus("0");
        List<ServerApp> listApp = serverAppDao.getList(app);
        if (listApp != null && listApp.size() > 0) {
            String id = IdGen.uuid();
            String uploadFileName = id+".txt";
            String tempFileName = id+".temp";
            serverTime.setId(id);
            serverTime.setOrgId(orgId);
            String uploadPath = String.join(File.separator, new String[]{environment.getProperty("upload.linux.path"),  "offline",environment.getProperty("config.area"),orgId,"circulateData"});
            File file = new File(uploadPath);
            if (!file.exists()) {
                file.mkdirs();
            }
            String filePath = uploadPath + File.separator + uploadFileName;
            String tempPath = uploadPath + File.separator + tempFileName;
            file = new File(tempPath);
            if (file.createNewFile()) {
                FileCopyUtils.copy(multiFile.getBytes(), file);
                file.renameTo(new File(filePath));
                //插入对应的记录
                serverTimeDao.insert(serverTime);
                //异步调用处理流通数据和文件
                serverCirculateLogService.deleteRedis(orgId + "_handle_circulation_data");
                serverCirculateLogService.executeSync(UserUtils.getOrgId(),true);
            }
        } else {
            throw new VerifyException("该学校没有绑定离线客户端或者解绑，不能上传文件！");
        }
    }

    /**
     * 异步生成数据服务到redis
     * @param serverSync 基本条件
     * @param mapDate 各类型同步时间
     */
    @Async
    public void createRedisData(ServerSync serverSync,Map<String,Date> mapDate){
        serverSync.setPageSize(Integer.parseInt(environment.getProperty(LIB3_EXPORT_PAGESIZE)));
        //1：读者数据生成
        createDataService.createReaderData(serverSync,mapDate.get("1"));
        //2：馆藏复本数据生成
        createDataService.createCopyData(serverSync,mapDate.get("2"));
        //3：流通同步结果数据生成
        createDataService.createSyncData(serverSync,mapDate.get("3"));
        //4:期刊同步结果数据生成
        createDataService.createPeriodicalData(serverSync,mapDate.get("4"));
    }
    public void deleteRedisStatus(String orgId){
        createDataService.deleteRedisStatus(orgId,"1");
        createDataService.deleteRedisStatus(orgId,"2");
        createDataService.deleteRedisStatus(orgId,"3");
        createDataService.deleteRedisStatus(orgId,"4");
    }
    public void updateRedisStatus(String orgId){
        createDataService.updateRedisStatus(orgId,"1");
        createDataService.updateRedisStatus(orgId,"2");
        createDataService.updateRedisStatus(orgId,"3");
        createDataService.updateRedisStatus(orgId,"4");
    }

}
