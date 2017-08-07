package com.lianyitech.core.utils;

import com.lianyitech.common.persistence.AbstractDataEntity;
import com.lianyitech.modules.sys.utils.UserUtils;

/**
 * Created by tangwei on 2016/10/29.
 */
public class DataEntity<T> extends AbstractDataEntity<T> {


    public DataEntity() {
        this.delFlag = "0";
    }

    public DataEntity(String id) {
        super(id);
    }

    @Override
    protected String getCurrentUserId() {
        return UserUtils.getUser() != null ? UserUtils.getUser().getLoginName() : null;
    }
}
