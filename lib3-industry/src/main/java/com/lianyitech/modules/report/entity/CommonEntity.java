package com.lianyitech.modules.report.entity;

import com.lianyitech.common.utils.StringUtils;
import com.lianyitech.core.enmu.EnumOrgType;
import com.lianyitech.core.utils.DataEntity;
import com.lianyitech.modules.sys.entity.User;
import com.lianyitech.modules.sys.utils.UserUtils;

/**
 * 报表中某些属性可以公用
 * Created by on 2016/10/31.
 */
public  class CommonEntity<T> extends DataEntity<T>{
    /**
     * 省
     */
    private String province;
    /**
     * 市
     */
    private String city;
    /**
     * 县
     */
    private String county;
    /**
     * 学区
     */
    private String schoolArea;
    /**
     * 机构类型
     */
    private String orgType;
    /**
     * 单位代码
     */
    private String unitCode;
    /**
     * 单位名称
     */
    private String unitName;
    /**
     * 前端条件查询：1：学校   2：学区   3：全部
     */
    private int conditionQuery;
    /**
     * 1: 书     2：期刊
     */
    private int bookType;

    //加一个构造函数--拦截器没启动作用目前在这里设置下
    public CommonEntity() {
        User user = UserUtils.getUser();
        if (user != null) {
            String orgId = user.getOrgId();
            String orgType = user.getOrgType();
            if (StringUtils.isNotEmpty(orgType)) {
                if (orgType.equals(EnumOrgType.PROVINCE.getValue())) {//省级用户
                    this.province = orgId;
                    conditionQuery = 3;
                } else if (orgType.equals(EnumOrgType.CITY.getValue())) {//市级用户
                    this.city = orgId;
                    conditionQuery = 3;
                } else if (orgType.equals(EnumOrgType.COUNTY.getValue())) {//县级用户
                    this.county = orgId;
                    conditionQuery = 1;
                } else if (orgType.equals(EnumOrgType.SCHOOLAREA.getValue())) {//学区用户
                    this.schoolArea = orgId;
                    conditionQuery = 1;
                }else{//不是省市县用户则不让访问数据
                    this.province = null;//随便设置为了其他用户(权限没控制好的情况)进来查询所有的数据
                }
            }else{
                this.province = null;//随便设置为了其他用户(权限没控制好的情况)进来查询所有的数据
            }
        }
    }

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getSchoolArea() {
        return schoolArea;
    }

    public void setSchoolArea(String schoolArea) {
        this.schoolArea = schoolArea;
    }

    public int getConditionQuery() {
        return conditionQuery;
    }

    public void setConditionQuery(int conditionQuery) {
        this.conditionQuery = conditionQuery;
    }

    public String getOrgType() {
        return orgType;
    }

    public void setOrgType(String orgType) {
        this.orgType = orgType;
    }

    public int getBookType() {
        return bookType;
    }

    public void setBookType(int bookType) {
        this.bookType = bookType;
    }
}
