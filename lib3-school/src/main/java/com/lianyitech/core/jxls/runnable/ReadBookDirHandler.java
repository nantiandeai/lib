package com.lianyitech.core.jxls.runnable;

import com.lianyitech.common.service.CrudService;
import com.lianyitech.common.utils.IdGen;
import com.lianyitech.common.utils.SpringContextHolder;
import com.lianyitech.core.excel.Engine;
import com.lianyitech.modules.catalog.entity.BookDirectory;
import com.lianyitech.modules.catalog.entity.ImportRecord;
import com.lianyitech.modules.catalog.service.BookDirectoryService;
import com.lianyitech.modules.sys.entity.User;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicReferenceArray;

/**
 * Created by cjw on 2016/11/25.
 */
public class ReadBookDirHandler implements Engine.Handler<BookDirectory> {
    volatile ArrayList<BookDirectory> bookDirectoryList = new ArrayList<BookDirectory>();

    @Override
    public  void  handle(BookDirectory bookDirectory, List errorList, CrudService service, User user, ImportRecord record) throws Exception {
        BookDirectoryService bookDirectoryService = (BookDirectoryService)service;

//        if(record.getResolveNum().incrementAndGet()%4000==0){//该代码会拿不到数据库连接，暂时屏蔽
//            long t = record.getTotalNum()-record.getResolveNum().get();
//            String progress = "当前已处理："+record.getResolveNum()+"条记录,剩余："+t +"条";
//            record.setProgress(progress);
//            bookDirectoryService.updateProgress(record);
//        }
        bookDirectory.setOrgId(user.getOrgId());
        bookDirectory.setUserLoginName(user.getLoginName());
        boolean checkFlag = false;// bookDirectoryService.checkBookDirectory(bookDirectory);
        if (!checkFlag ){
            addErrorList(errorList,bookDirectory);
            putQuene(record,bookDirectory,checkFlag);
        } else {
            bookDirectory.setId(IdGen.uuid());
            bookDirectory.setOrgId(user.getOrgId());
            bookDirectory.setUpdateBy(user.getId());
            bookDirectory.setCreateBy(user.getId());
            bookDirectory.setCreateDate(new Date());
            bookDirectory.setUpdateDate(new Date());
//            bookDirectoryList.add(bookDirectory);
//            record.getSuccess().incrementAndGet();
            putQuene(record,bookDirectory,checkFlag);
        }
        List saveList = putQuene(record,null,checkFlag);
        if(saveList!=null){
            goSave(bookDirectoryService,saveList);
        }
    }

    private synchronized List putQuene(ImportRecord record,BookDirectory bookDirectory,boolean checkFlag) {
        boolean lastFlag = (record.getResolveNum().get()==record.getTotalNum());
        if(bookDirectory!=null) {
            record.getResolveNum().incrementAndGet();
            if(checkFlag) {
                bookDirectoryList.add(bookDirectory);
                record.getSuccess().incrementAndGet();
            }

        } else if ((record.getSuccess().get()%50==0 || lastFlag ) && !bookDirectoryList.isEmpty()) {
                List<BookDirectory> tm = new ArrayList<BookDirectory>();
                tm.addAll(bookDirectoryList);
                bookDirectoryList = new ArrayList<BookDirectory>();
                return tm;
        }
        return null;
    }

    private  void goSave(BookDirectoryService service,List<BookDirectory> list){
        try{
            service.batchSave(list);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private synchronized void  addErrorList(List errorList,BookDirectory directory){
        errorList.add(directory);
    }
}
