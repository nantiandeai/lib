package com.lianyitech.core.excel;

import com.lianyitech.common.persistence.BaseEntity;
import com.lianyitech.common.service.CrudService;
import com.lianyitech.modules.catalog.entity.ImportRecord;
import com.lianyitech.modules.sys.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;

/**
 * ConcurrentNavigableMap 格式的解析器
 * Created by tangwei on 2016/11/23.
 */
public class Importer<T extends BaseEntity> {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    /**
     * 线程数
     */
    private final int TASK_THREAD = 10;
    /**
     * 队列大小
     */
    private final int TASK_LIST_SIZE = 500;
    /**
     * 解析器
     */
    private Engine.Parser<T> parser;
    /**
     * 记录处理器
     */
    private Engine.Handler<T> rowHandler;
    /**
     * 结束信号
     */
    private T dummy;

    private List errorList;

    private User user ;

    CrudService service;

    ImportRecord record;
    /**
     *
     * @param parser 解析器
     * @param dummy 结束信号
     * @param rowHandler 记录处理器
     */
    public Importer(Engine.Parser<T> parser, T dummy, Engine.Handler<T> rowHandler, List errorList, CrudService service,User user,ImportRecord record) {
        this.parser = parser;
        this.rowHandler = rowHandler;
        this.dummy = dummy;
        this.errorList = errorList;
        this.service = service;
        this.user = user;
        this.record = record;
    }

    public void doImport() {
//        long startGenTime = 0;
        try {
//            startGenTime = System.currentTimeMillis();
            logger.info(">>>>>>>>>>start importing");
            //设置队列
            BlockingQueue<T> rowTaskQueue = new ArrayBlockingQueue<>(TASK_LIST_SIZE);
            CountDownLatch rowCDL = new CountDownLatch(TASK_THREAD);
            //初始化多线程引擎
            Engine excelRowTask = EngineFactory.task(rowTaskQueue, rowCDL, rowHandler, dummy,errorList,service,user,record);
            for (int i = 0; i < TASK_THREAD; i++) {
                //开启多线程 TODO 可结合线程池使用
                new Thread(excelRowTask).start();
            }
            //处理文件（向队列中加入任务）
            parser.process(rowTaskQueue, true, record);
            //完成 （向队列中加入结束信号）
            rowTaskQueue.put(dummy);
            rowCDL.await();
        } catch (Exception e) {
            logger.error("import file error: " + e.getMessage(), e);
        } finally {
            //long endGenTime = System.currentTimeMillis();
            //logger.info("total spent>>>>" + (endGenTime - startGenTime) + "ms");
        }
    }
}
