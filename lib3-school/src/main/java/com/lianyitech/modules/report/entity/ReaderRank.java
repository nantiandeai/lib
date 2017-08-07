package com.lianyitech.modules.report.entity;

import com.lianyitech.common.utils.StringUtils;
import com.lianyitech.core.enmu.EnumReaderType;
import com.lianyitech.core.utils.DataEntity;

/**
 * Created by zcx on 2016/11/8.
 * 读者和组织借阅排行实体类
 */
public class ReaderRank extends DataEntity<ReaderRank> {

    private static final long serialVersionUID = 1L;
    private String orgId;//机构id
    private String readerId;//读者id
    private String readerName;//读者姓名
    private String groupName;//组织名称
    private String readerType;//读者类型
    private  String readerTypeName;//读者类型名称
    private Integer borrowNum;//读者借阅册次
    private String beginTime;// 开始时间
    private String endTime;//结束时间
    private String ranks;//借阅排名

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getReaderId() {
        return readerId;
    }

    public void setReaderId(String readerId) {
        this.readerId = readerId;
    }

    public String getReaderName() {
        return readerName;
    }

    public void setReaderName(String readerName) {
        this.readerName = readerName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getReaderType() {
        return readerType;
    }

    public void setReaderType(String readerType) {
        this.readerType = readerType;
    }

    public String getReaderTypeName() {
        if(StringUtils.isNotEmpty(readerType)){
            readerTypeName= EnumReaderType.parse(readerType).getName();
        }
        return readerTypeName;
    }

    public void setReaderTypeName(String readerTypeName) {
        this.readerTypeName = readerTypeName;
    }

    public Integer getBorrowNum() {
        return borrowNum;
    }

    public void setBorrowNum(Integer borrowNum) {
        this.borrowNum = borrowNum;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getRanks() {
        return ranks;
    }

    public void setRanks(String ranks) {
        this.ranks = ranks;
    }
}
