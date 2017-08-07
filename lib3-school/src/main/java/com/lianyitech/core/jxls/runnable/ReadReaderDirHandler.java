package com.lianyitech.core.jxls.runnable;

import com.lianyitech.common.service.CrudService;
import com.lianyitech.common.utils.IdGen;
import com.lianyitech.core.excel.Engine;
import com.lianyitech.modules.catalog.entity.BookDirectory;
import com.lianyitech.modules.catalog.entity.ImportRecord;
import com.lianyitech.modules.catalog.service.BookDirectoryService;
import com.lianyitech.modules.circulate.entity.Reader;
import com.lianyitech.modules.circulate.service.ReaderService;
import com.lianyitech.modules.sys.entity.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by cjw on 2016/11/25.
 */
public class ReadReaderDirHandler implements Engine.Handler<Reader> {
    volatile List<Reader> readerList = new ArrayList<Reader>();



    @Override
    public  void  handle(Reader reader, List errorList, CrudService service, User user, ImportRecord record) throws Exception {
//        record.getResolveNum().incrementAndGet();
        ReaderService readerService = (ReaderService)service;
        reader.setOrgId(user.getOrgId());
        boolean checkFlag = readerService.checkReader(reader);

        if (!checkFlag ){
            addErrorList(errorList,reader);
            putQuene(record,reader,checkFlag);
        } else {
            reader.setId(IdGen.uuid());
            reader.setUpdateBy(user.getId());
            reader.setCreateBy(user.getId());
            reader.setCreateDate(new Date());
            reader.setUpdateDate(new Date());
            putQuene(record,reader,checkFlag);
        }
        List saveList = putQuene(record,null,checkFlag);
        if(saveList!=null) {
            try{
                readerService.batchSave(saveList);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private synchronized List putQuene(ImportRecord record,Reader reader,boolean checkFlag) {
        boolean lastFlag = (record.getResolveNum().get()==record.getTotalNum());
        if(reader!=null) {
            record.getResolveNum().incrementAndGet();
            if(checkFlag) {
                readerList.add(reader);
                record.getSuccess().incrementAndGet();
            }

        } else if ((record.getSuccess().get()%50==0 || lastFlag ) && !readerList.isEmpty()) {
            List<Reader> tm = new ArrayList<Reader>();
            tm.addAll(readerList);
            readerList = new ArrayList<Reader>();
            return tm;
        }
        return null;
    }

    private synchronized void  addErrorList(List errorList,Reader reader){
        errorList.add(reader);
    }
}
