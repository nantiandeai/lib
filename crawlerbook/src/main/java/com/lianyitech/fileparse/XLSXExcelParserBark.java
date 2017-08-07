package com.lianyitech.fileparse;

import com.lianyitech.file.importer.Engine;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.util.SAXHelper;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler.SheetContentsHandler;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFComment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import javax.xml.parsers.ParserConfigurationException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * excel解析器 poi event user model
 * Created by tangwei on 2016/11/23.
 */
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class XLSXExcelParserBark implements Engine.Parser<ConcurrentNavigableMap<String, String>> {
  
    private FileInputStream in;

    /**
     * 处理每个Sheet， 直接转换成Map格式存储每条记录
     * @param styles XSSFSheetXMLHandler构造方法参数
     * @param strings XSSFSheetXMLHandler构造方法参数
     * @param sheetInputStream XMLReader parse方法参数
     * @param queue 队列
     * @param toQueue 是否写入队列
     * @throws SAXException XML解析异常
     * @throws ParserConfigurationException XML解析异常
     * @throws IOException XML解析异常
     */
    private void processSheet(StylesTable styles,
                              ReadOnlySharedStringsTable strings,
                              InputStream sheetInputStream,
                              BlockingQueue<ConcurrentNavigableMap<String, String>> queue,
                              boolean toQueue)
            throws SAXException, ParserConfigurationException, IOException  {
        XMLReader sheetParser = SAXHelper.newXMLReader();
        sheetParser.setContentHandler(new XSSFSheetXMLHandler(styles, strings, new SheetContentsHandler() {
            private Logger logger = LoggerFactory.getLogger(this.getClass());
            private ConcurrentNavigableMap<String, String> dataMap = new ConcurrentSkipListMap<>();

            public String getCellName(String cellReference){
                Pattern p = Pattern.compile("[0-9]");
                Matcher matcher = p.matcher(cellReference);
                return matcher.replaceAll("");
            }
            @Override
            public void startRow(int rowNum) { }

            @Override
            public void endRow(int rowNum) {
                try {
                    if (dataMap.keySet().size() > 0) {
                        if (toQueue) {
                            queue.put(dataMap);
                        }
                        dataMap = new ConcurrentSkipListMap<>();
                    }
                } catch (Exception e) {
                    logger.error("put data into queue error: " + e.getMessage(), e);
                }
            }

            @Override
            public void cell(String cellReference, String formattedValue, XSSFComment xssfComment) {
                if(StringUtils.isNotEmpty(formattedValue) && StringUtils.isNotEmpty(formattedValue.trim()) && StringUtils.isNotEmpty(cellReference)&& StringUtils.isNotEmpty(cellReference.trim()))
                    this.dataMap.put(getCellName(cellReference), formattedValue);
            }

            @Override
            public void headerFooter(String text, boolean isHeader, String tagName) { }

        }, false));
        sheetParser.parse(new InputSource(sheetInputStream));
    }

    /**
     * 处理Excel文件，因为效率问题，只支持2007
     * @param queue 队列
     * @param toQueue 是否写入队列
     * @throws Exception 异常直接抛出来
     */
    public void process(BlockingQueue<ConcurrentNavigableMap<String, String>> queue, boolean toQueue) throws Exception{
        try {
            OPCPackage opcp = OPCPackage.open(in);
            ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(opcp);
            XSSFReader xssfReader = new XSSFReader(opcp);
            StylesTable styles = xssfReader.getStylesTable();
            Iterator<InputStream> sheets = xssfReader.getSheetsData();
            while (sheets.hasNext()) {
                InputStream stream = sheets.next();
                processSheet(styles, strings, stream, queue, toQueue);
                stream.close();
            }
        } finally {
            in.close();
        }
    }
}