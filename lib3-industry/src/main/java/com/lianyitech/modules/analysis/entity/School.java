package com.lianyitech.modules.analysis.entity;

import com.lianyitech.modules.report.entity.CommonEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by jordan jiang on 2017/4/7.
 */
public class School extends CommonEntity<School> {

    private static final long serialVersionUID = 1L;
    /**
     * 联系人
     */
    private String master;
    /**
     * 联系电话
     */
    private String phone;
    /**
     * 查询类型：1：无馆藏  2：无读者   3：无流通
     */
    private String selectType;
    /**
     * 年
     */
    private String year;
    /**
     * 月
     */
    private String month;
    /**
     * 季
     */
    private String quarter;

    public String getMaster() {
        return master;
    }

    public void setMaster(String master) {
        this.master = master;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSelectType() {
        return selectType;
    }

    public void setSelectType(String selectType) {
        this.selectType = selectType;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getQuarter() {
        return quarter;
    }

    public void setQuarter(String quarter) {
        this.quarter = quarter;
    }
}
