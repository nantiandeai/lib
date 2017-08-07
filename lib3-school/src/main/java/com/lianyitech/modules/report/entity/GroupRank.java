package com.lianyitech.modules.report.entity;

import com.lianyitech.core.utils.DataEntity;

/**
 * Created by zcx on 2016/11/8.
 * 组织借阅排行实体类
 */
public class GroupRank extends DataEntity<GroupRank> {

    private static final long serialVersionUID = 1L;
    private String groupId;//组织id
    private String groupName;//组织名称
    private Integer groupNumber;//组织人数
    private String groupType;//组织类型
    private String groupTypeName;//组织类型名称
    private Integer borrowNum;//借阅册次
    private String ranks;//借阅排名
    private String orgId;//机构id
    private String beginTime;// 开始时间
    private String endTime;//结束时间

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Integer getGroupNumber() {
        return groupNumber;
    }

    public void setGroupNumber(Integer groupNumber) {
        this.groupNumber = groupNumber;
    }

    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }

    public Integer getBorrowNum() {
        return borrowNum;
    }

    public void setBorrowNum(Integer borrowNum) {
        this.borrowNum = borrowNum;
    }

    public String getRanks() {
        return ranks;
    }

    public void setRanks(String ranks) {
        this.ranks = ranks;
    }

    public String getGroupTypeName() {
        return groupTypeName;
    }

    public void setGroupTypeName(String groupTypeName) {
        this.groupTypeName = groupTypeName;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
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
}
