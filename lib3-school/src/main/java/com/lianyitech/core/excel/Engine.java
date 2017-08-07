package com.lianyitech.core.excel;

import com.lianyitech.common.persistence.BaseEntity;
import com.lianyitech.common.service.CrudService;
import com.lianyitech.modules.catalog.entity.ImportRecord;
import com.lianyitech.modules.sys.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;

/**
 * 引擎
 * Created by tangwei on 2016/11/23.
 */
public class Engine<T extends  BaseEntity > implements Runnable {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private CountDownLatch cdl;
    private T dummy;
    private BlockingQueue<T> queue;
    private Handler<T> handler;
    private List errorList;
    private CrudService service;
    private User user ;
    private ImportRecord record;

    Engine(BlockingQueue<T> queue, Handler<T> handler, CountDownLatch cdl, T dummy,List errorList,CrudService service,User user,ImportRecord record) {
        this.cdl = cdl;
        this.queue = queue;
        this.handler = handler;
        this.dummy = dummy;
        this.errorList = errorList;
        this.service = service;
        this.user = user;
        this.record = record ;
    }

    public interface Handler<T> {
        void handle(T data,List errorList,CrudService service,User user,ImportRecord record) throws Exception;
    }

    public interface Parser<T> {
        void process(BlockingQueue<T> rowTaskQueue, boolean toQueue,ImportRecord record) throws Exception;
    }

    public void run() {
        boolean done = false;
        try {
                while (!done) {
                    T data = queue.take();
                    if (data.getId()!=null && data.getId().equals("stop")) {
                        queue.put(data);
                        done = true;
                    } else {
                        handler.handle(data,errorList,service,user,record);
                    }
                }
        } catch (Exception e) {
            logger.error("import file into db error:" + e.getMessage(), e);
            try {
                Thread.interrupted();
            } catch (Exception ignored) {
                logger.error(ignored.getMessage());
            }
            try {
                queue.put(this.dummy);
            } catch (Exception ignored) {
                logger.error(ignored.getMessage());
            }
        } finally {
            cdl.countDown();
        }
    }
  
}  