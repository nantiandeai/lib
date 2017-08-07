package com.lianyitech.modules.catalog.entity;

import com.lianyitech.core.utils.DataEntity;
import com.lianyitech.modules.sys.utils.UserUtils;

/**
 * Created by luzhihuai on 2016/9/27.
 */
public class NewbookNotifiyDetail extends DataEntity<NewbookNotifiy> {

    private static final long serialVersionUID = 1L;
    private String newbookNotifiyId; //新书通报主表id
    private String copyId; //复本id

    public String getNewbookNotifiyId() {
        return newbookNotifiyId;
    }

    public void setNewbookNotifiyId(String newbookNotifiyId) {
        this.newbookNotifiyId = newbookNotifiyId;
    }

    public String getCopyId() {
        return copyId;
    }

    public void setCopyId(String copyId) {
        this.copyId = copyId;
    }

}
