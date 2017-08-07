/**
 *
 */
package com.lianyitech.modules.sys.entity;

import com.lianyitech.core.utils.DataEntity;
import com.lianyitech.modules.sys.utils.UserUtils;
import org.hibernate.validator.constraints.Length;

import javax.xml.crypto.Data;
import java.util.Date;

/**
 * 馆藏地管理Entity
 *
 * @author zengzy
 * @version 2016-09-02
 */
public class CollectionSite extends DataEntity<CollectionSite> {

    private static final long serialVersionUID = 1L;
    private String orgId;        // 机构id
    private String name;        // 馆藏地
    private String stockType;        // 库存类型
    private String stockAttr;        // 流通方式  0不可外借  1可外借
    private String beginTime;   //开始时间--查询专用
    private String endTime;       //结束时间--查询专用
    private String stockTypeName;//库存类型名称

    private String status; //默认状态 0默认1不默认

    private String[] idList;//ID用,分开传

    public String[] getIdList() {
        return idList;
    }

    public void setIdList(String[] idList) {
        this.idList = idList;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public CollectionSite() {
        super();
    }

    public CollectionSite(String id) {
        super(id);
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStockType() {
        return stockType;
    }

    public void setStockType(String stockType) {
        this.stockType = stockType;
    }

    public String getStockAttr() {
        return stockAttr;
    }

    public void setStockAttr(String stockAttr) {
        this.stockAttr = stockAttr;
    }

    public String getStockTypeName() {
        return stockTypeName;
    }

    public void setStockTypeName(String stockTypeName) {
        this.stockTypeName = stockTypeName;
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