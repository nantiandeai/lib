package com.lianyitech.modules.sys.service;

import com.lianyitech.common.utils.Global;
import com.lianyitech.common.utils.StringUtils;
import com.lianyitech.core.utils.CsvUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

/**
 * Created by Administrator on 2017/6/9.
 */
@Service
public class FileService {

    @Autowired
    Environment environment;

    public  String generateFilePath(){
        String uploadFileName = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_TIME).replaceAll(":", "") + "_" + UUID.randomUUID().toString().replaceAll("-", "")+".csv";
        String uploadPath = String.join(File.separator, new String[]{environment.getProperty("upload.linux.path"), LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE).replaceAll("-", ""), "exportTempDir"});
        File file = new File(uploadPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        return uploadPath + File.separator + uploadFileName;
    }


    public  String generateFilePath(String fileName){
        String rootPath = Global.getUploadRootPath() ;
        String uploadPath = Global.UPLOADFILES_BASE_URL + File.separator + "images" + File.separator + String.join(File.separator, new String[]{ LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE).replaceAll("-", "")});
        File file = new File(rootPath + File.separator+ uploadPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        return uploadPath + File.separator + fileName;
    }

    public void writeCsvFile(List<String> list, String path, HttpServletResponse response) throws IOException {
        try(
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(path),true), "GBK"));
                InputStream in = new FileInputStream(path);) {
            CsvUtils.exportCommonCsv(bw, list);
            CsvUtils.writeResponse(in, response);
        }
        response.flushBuffer();
    }
}
