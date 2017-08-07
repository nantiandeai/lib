/**
 *
 */
package com.lianyitech.modules.sys.entity;

import com.lianyitech.core.utils.DataEntity;

/**
 * 编目设置
 */
public class CatalogSet extends DataEntity<CatalogSet> {

    private static final long serialVersionUID = 1L;
    private String orgId;
    private String cnfType;//配置类型 0索书号 1条形码
    private String cnfMethod;//配置方式

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getCnfType() {
        return cnfType;
    }

    public void setCnfType(String cnfType) {
        this.cnfType = cnfType;
    }

    public String getCnfMethod() {
        return cnfMethod;
    }

    public void setCnfMethod(String cnfMethod) {
        this.cnfMethod = cnfMethod;
    }
}