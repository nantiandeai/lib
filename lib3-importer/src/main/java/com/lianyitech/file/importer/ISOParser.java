package com.lianyitech.file.importer;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.marc4j.MarcPermissiveStreamReader;
import org.marc4j.MarcStreamWriter;
import org.marc4j.marc.DataField;
import org.marc4j.marc.Record;
import org.marc4j.marc.Subfield;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

import static com.lianyitech.file.importer.Engine.Parser;

/**
 * Marc数据解析
 * Created by tangwei on 2016/11/24.
 */
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class ISOParser implements Parser<ConcurrentNavigableMap<String, String>> {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    /**
     * 文件输入流
     */
    private FileInputStream in;

    /**
     * 接口实现，处理文件信息
     * @param rowTaskQueue 线程队列
     * @param toQueue 是否加入队列
     * @throws Exception 不处理异常
     */
    @Override
    public void process(BlockingQueue<ConcurrentNavigableMap<String, String>> rowTaskQueue, boolean toQueue) throws Exception {
        int i = 0;
        MarcPermissiveStreamReader reader = new MarcPermissiveStreamReader(in, true, true, "GBK");
        while (reader.hasNext()) {
            try {
                Record record = reader.next();
                handleRecode(record, rowTaskQueue, toQueue);
            } catch (Exception e) {
                i++;
                logger.error("", e);
            }
        }
        logger.error(i + "条记录解析错误");
    }

    /**
     * 处理每一条记录
     * @param record 记录对象
     * @param rowTaskQueue 线程队列
     * @param toQueue 是否加入队列
     * @throws Exception
     */
    private void handleRecode(
            Record record,
            BlockingQueue<ConcurrentNavigableMap<String, String>> rowTaskQueue,
            boolean toQueue) throws Exception {
        ConcurrentNavigableMap<String, String> map = new ConcurrentSkipListMap<>();
        for (DataField data : record.getDataFields()) {
            for (Subfield subField : data.getSubfields()) {
                map.put(data.getTag()+ "$" + subField.getCode(), subField.getData());
            }
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        new MarcStreamWriter(out, "GBK").write(record);
        map.put("marc64", out.toString("GBK"));
        if (toQueue)
            rowTaskQueue.put(map);
    }
}
