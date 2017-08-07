package com.lianyitech.cnclawler;

import com.lianyitech.Proxy.IfCanUseProxy;
import com.lianyitech.file.importer.*;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentNavigableMap;

/**
 * 将中国可供书目爬虫到Excel文本（---这里之所以这么弄是中国可供书目访问太过频繁ip被封--可以直接通过多处爬到txt）
 */
public class CnImportExcel {
    public static void main(String[] args) throws Exception {
        WritableWorkbook book = Workbook.createWorkbook(new File("/cnexcel.xls"));
        String info[] = {"isbn","title"};

        Map<String, String> IpPorts = new HashMap<>();
        IfCanUseProxy.readProxy(IpPorts);

        new Importer<>(
                new CnCrawler(IpPorts),
                EngineFactory.concurrentSkipListMapDummy,
                new Engine.Handler<ConcurrentNavigableMap<String, String>>() {
                    int row_index = 0;
                    int i = 0;
                    int sheet_index = 0;
                    WritableSheet sheet = null;
                    @Override
                    public void handle(ConcurrentNavigableMap<String, String> data) throws Exception {
                        if (i % 3 == 0) {
                            sheet = book.createSheet("sheet"+sheet_index, sheet_index);
                            for(int j=0;j<info.length;j++) {
                                Label label = new Label(j, 0, info[j]);
                                sheet.addCell(label);
                            }
                            row_index = 0;
                            sheet_index++;
                        }
                        row_index++;
                        sheet.addCell(new Label(0,row_index,"ddddd"));
                        sheet.addCell(new Label(1,row_index,"wuwuw"));
                        i++;
                    }
                }
        ).doImport();
        if(book!=null){
            try {
                book.write();
                book.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
