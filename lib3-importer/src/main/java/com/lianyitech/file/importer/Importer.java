package com.lianyitech.file.importer;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;

/**
 * ConcurrentNavigableMap 格式的解析器
 * Created by tangwei on 2016/11/23.
 */
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class Importer<T> {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    /**
     * 解析器
     */
    private Engine.Parser<T> parser;
    /**
     * 结束信号
     */
    private T dummy;
    /**
     * 记录处理器
     */
    private Engine.Handler<T> rowHandler;

    public void doImport() throws Exception{
        long startGenTime = 0;
        try {
            startGenTime = System.currentTimeMillis();
            logger.info(">>>>>>>>>>start importing");
            //设置队列
            /*
              队列大小
             */
            int TASK_LIST_SIZE = 25000;
            BlockingQueue<T> rowTaskQueue = new ArrayBlockingQueue<>(TASK_LIST_SIZE);
            /*
              线程数
             */
            int TASK_THREAD = 1;
            CountDownLatch rowCDL = new CountDownLatch(TASK_THREAD);
            //初始化多线程引擎
            Engine excelRowTask = EngineFactory.task(rowTaskQueue, rowHandler, rowCDL, dummy);
            for (int i = 0; i < TASK_THREAD; i++) {
                //开启多线程 TODO 可结合线程池使用
                new Thread(excelRowTask).start();
            }
            //处理文件（向队列中加入任务）
            parser.process(rowTaskQueue, true);
            //完成 （向队列中加入结束信号）
            rowTaskQueue.put(dummy);
            rowCDL.await();
        } catch (Exception e) {
            logger.error("import file error: " + e.getMessage(), e);
            throw e;
        } finally {
            long endGenTime = System.currentTimeMillis();
            logger.info("total spent>>>>" + (endGenTime - startGenTime) + "ms");
        }
    }
}
