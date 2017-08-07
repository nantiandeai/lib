/**
 * 
 */
package com.lianyitech.modules.catalog.dao;

import com.lianyitech.common.persistence.CrudDao;
import com.lianyitech.common.persistence.annotation.MyBatisDao;
import com.lianyitech.modules.catalog.entity.BookDirectory;
import com.lianyitech.modules.catalog.entity.Copy;
import com.lianyitech.modules.catalog.entity.ImportRecord;
import com.lianyitech.modules.sys.entity.CollectionSite;

import java.util.List;

/**
 * 导入记录
 * @author zengzy
 * @version 2016-08-26
 */
@MyBatisDao
public interface ImportRecordDao extends CrudDao<ImportRecord> {
    public void updateProgress(ImportRecord importRecord);

}