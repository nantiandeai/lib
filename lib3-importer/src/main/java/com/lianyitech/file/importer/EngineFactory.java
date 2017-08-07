package com.lianyitech.file.importer;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.CountDownLatch;

/**
 * 工厂
 * Created by tangwei on 2016/11/23.
 */
public class EngineFactory {
    public static final ConcurrentSkipListMap<String, String> concurrentSkipListMapDummy = new ConcurrentSkipListMap<String, String>(){{
        put("dummy", "dummy");
    }};

    static <T> Engine<T> task(
            BlockingQueue<T> queue,
            Engine.Handler<T> handler,
            CountDownLatch cdl,
            T dummy) {
        return new Engine<>(queue, handler, cdl, dummy);
    }

}  