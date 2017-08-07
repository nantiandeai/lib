package com.lianyitech.modules.catalog.entity;

import com.lianyitech.common.utils.StringUtils;
import com.lianyitech.core.utils.DataEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

@Data
@EqualsAndHashCode(callSuper = false)
@ToString
public class ImportRecord extends DataEntity<ImportRecord> {

    private String fileName;//上传文件名
    private String filePath;//导入文件存储路径
    private Date finishTime;//完成时间
    private Integer state;//导入状态
    private String stateName;//导入状态-中文
    private long successNum;//成功记录
    private long failNum;//失败数量
    private String errorFileName;//错误文件名
    private Long time;//耗时
    private String progress;//导入进度
    private long totalNum;
    private AtomicLong success;
    private AtomicLong resolveNum;
    private Integer type ;//类型 1：书目 2：读者证 3:旧书导入（回溯建库）
    private Integer fileType;
    private String uploadTime;
    public String getStateName() {
        if (StringUtils.isBlank(stateName)) {
            if(state!=null && state==0) {
                stateName = "导入完成";
            } else if (state!=null && state==1){
                stateName = "正在导入。。。";
            }
        }
        return stateName;
    }

    public String getUploadTime(){
        if (createDate!=null) {
            return new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(createDate);
        }
        return "";
    }

}
