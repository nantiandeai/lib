package com.lianyitech.modules.circulate.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lianyitech.core.utils.DataEntity;

/**
 * 流通操作DTO
 * Created by tangwei on 2016/9/10.
 */
public class CirculateDTO extends DataEntity<CirculateDTO> {//这里改成继承此类，是为了将创建人、创建时间带过去

    private String billId;
    /**
     * 读者证号
     */
    private String card;
    /**
     * 图书条形码
     */
    private String barcode;
    /**
     * 操作类型
     */
    private String type;

    private String billType;

    /**
     *状态
     */
    private String status;
    /**
     * 读者id
     */
    private String readerid;
    /**
     * 修改状态
     */
    private String updateStatus;
    /**
     * 机构id orgId
     */
    private String orgId;

    /**
     * 借阅规则id
     */
    private String ruleId;

    /**
     * 书目类型 0图书 1期刊
     */
    private String dirType="0";

    private CompenRecord compenRecord;

    public CompenRecord getCompenRecord() {
        return compenRecord;
    }

    public void setCompenRecord(CompenRecord compenRecord) {
        this.compenRecord = compenRecord;
    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public String getBillType() {
        return billType;
    }

    public void setBillType(String billType) {
        this.billType = billType;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    public String getReaderid() {
        return readerid;
    }

    public void setReaderid(String readerid) {
        this.readerid = readerid;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUpdateStatus() {
        return updateStatus;
    }

    public void setUpdateStatus(String updateStatus) {
        this.updateStatus = updateStatus;
    }


    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    @JsonIgnore
    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    public String getDirType() {
        return dirType;
    }

    public void setDirType(String dirType) {
        this.dirType = dirType;
    }
}
