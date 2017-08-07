package com.lianyitech.modules.analysis.entity;

import com.lianyitech.modules.common.ReportCommon;
import com.lianyitech.modules.report.entity.CommonEntity;
import io.swagger.annotations.ApiModel;

/**
 * Created by jordan jiang on 2017/4/6.
 */
public class InputAnalysis extends CommonEntity<InputAnalysis> {

    private static final long serialVersionUID = 1L;
    /**
     * 注册学校的数量
     */
    private Integer registerCount;
    /**
     * 无流通数据的学校数量
     */
    private Integer noInput;
    /**
     * 有流通数据的学校数量
     */
    private Integer yesInput;
    /**
     * 录入率 = 已应用流通学校/注册学校*100%
     */
    private String inputRate;

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

    public Integer getRegisterCount() {
        return registerCount;
    }

    public void setRegisterCount(Integer registerCount) {
        this.registerCount = registerCount;
    }

    public Integer getNoInput() {
        return noInput;
    }

    public void setNoInput(Integer noInput) {
        this.noInput = noInput;
    }

    public Integer getYesInput() {
        return yesInput;
    }

    public void setYesInput(Integer yesInput) {
        this.yesInput = yesInput;
    }

    public String getInputRate() {
        return ReportCommon.comdivision(yesInput!=null?yesInput*100:0,registerCount);
    }

    public void setInputRate(String inputRate) {
        this.inputRate = inputRate;
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
