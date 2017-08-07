package com.lianyitech.file.importer;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;

/**
 * 引擎
 * Created by tangwei on 2016/11/23.
 */
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class Engine<T> implements Runnable {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private BlockingQueue<T> queue;
    private Handler<T> handler;
    private CountDownLatch cdl;
    private T dummy;

    public interface Handler<T> {
        void handle(T data) throws Exception;
        void clean() throws Exception;
    }

    public interface Parser<T> {
        void process(BlockingQueue<T> rowTaskQueue, boolean toQueue) throws Exception;
    }

    public void run() {
        boolean done = false;
        try {
            synchronized (this) {
                while (!done) {
                    T data = queue.take();
                    //logger.info(data.toString());
                    if (this.dummy.equals(data)) {
                        queue.put(dummy);
                        done = true;
                    } else {
                        handler.handle(data);
                    }
                }
                handler.clean();
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            try {
                Thread.interrupted();
            } catch (Exception ignored) {}
            try {
                queue.put(this.dummy);
            } catch (Exception ignored) {}
        } finally {
            cdl.countDown();
        }
    }
  
}  