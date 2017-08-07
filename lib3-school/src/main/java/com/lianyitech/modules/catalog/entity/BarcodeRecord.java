package com.lianyitech.modules.catalog.entity;

import com.lianyitech.core.utils.DataEntity;

/**
 * Created by zcx on 2017/2/27.
 * BarcodeRecord 条码置换记录
 */
public class BarcodeRecord extends DataEntity<BarcodeRecord> {
    private static final long serialVersionUID = 1L;
    private String orgId;//机构id
    private String title;//题名
    private String oldBarcode;//老条码
    private String newBarcode;//新条码
    private String barcode;//输入框条码传参（老条码/新条码）
    private String status;//馆藏状态

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOldBarcode() {
        return oldBarcode;
    }

    public void setOldBarcode(String oldBarcode) {
        this.oldBarcode = oldBarcode;
    }

    public String getNewBarcode() {
        return newBarcode;
    }

    public void setNewBarcode(String newBarcode) {
        this.newBarcode = newBarcode;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }
}
