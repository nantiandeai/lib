package com.lianyitech.core.marc;

import org.marc4j.MarcStreamWriter;
import org.marc4j.marc.Record;

import java.io.ByteArrayOutputStream;

/**
 * Created by zengyuanmei on 2016/9/8.
 * 马克数据写入
 */
public class WriterBookMarc {
    //将马克写入字节流
    public static ByteArrayOutputStream writerMarcToByteStream(Record[] records){
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        MarcStreamWriter writer = new MarcStreamWriter(out,"GBK");
        if (records != null && records.length > 0) {
            for (Record record : records) {
                writer.write(record);
            }
            writer.close();
        }
        return out;
    }

}
