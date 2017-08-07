package com.lianyitech.modules.catalog.service;

import com.lianyitech.common.service.CrudService;
import com.lianyitech.modules.catalog.dao.BarcodeRecordDao;
import com.lianyitech.modules.catalog.entity.BarcodeRecord;
import org.springframework.stereotype.Service;

/**
 * Created by zcx on 2017/3/1.
 * BarcodeRecordService
 */
@Service
public class BarcodeRecordService extends CrudService<BarcodeRecordDao,BarcodeRecord> {
}
