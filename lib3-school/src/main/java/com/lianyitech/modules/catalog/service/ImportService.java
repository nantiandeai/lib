package com.lianyitech.modules.catalog.service;

import com.lianyitech.common.service.CrudService;
import com.lianyitech.modules.catalog.dao.ImportRecordDao;
import com.lianyitech.modules.catalog.entity.ImportRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by cjw on 2016/12/20.
 */
@Service
public class ImportService extends CrudService<ImportRecordDao, ImportRecord> {
    @Autowired
    ImportRecordDao importRecordDao;

    public ImportRecord initImportRecord(String fileName, String filePath, int i, int type) {
        ImportRecord record = new ImportRecord();
        record.preInsert();
        record.setFilePath(filePath);
        record.setFileName(fileName);
        record.setState(1);
        record.setType(type);
        record.setFileType(i);
        record.setResolveNum(new AtomicLong(0L));
        record.setSuccess(new AtomicLong(0L));
        record.setProgress("导入开始...");
        record.setStateName("导入开始");
        importRecordDao.insert(record);
        return record;
    }

    public ImportRecord updateImportRecord(ImportRecord importRecord) {
        importRecordDao.update(importRecord);
        return importRecord;
    }
}
