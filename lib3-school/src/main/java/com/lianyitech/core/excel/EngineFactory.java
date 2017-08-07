package com.lianyitech.core.excel;

import com.lianyitech.common.persistence.BaseEntity;
import com.lianyitech.common.service.CrudService;
import com.lianyitech.modules.catalog.entity.ImportRecord;
import com.lianyitech.modules.sys.entity.User;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.CountDownLatch;

/**
 * 工厂
 * Created by tangwei on 2016/11/23.
 */
public class EngineFactory {
    public static final ConcurrentSkipListMap<String, String> excelRowDummy = new ConcurrentSkipListMap<String, String>(){{
        put("dummy", "dummy");
    }};

    static <T extends BaseEntity> Engine<T> task(
            BlockingQueue<T> queue,
            CountDownLatch cdl,
            Engine.Handler<T> handler, T dummy,List errorList,CrudService service,User user,ImportRecord record) {
        return new Engine<T>(queue, handler, cdl, dummy,errorList, service,user,record);
    }

}  