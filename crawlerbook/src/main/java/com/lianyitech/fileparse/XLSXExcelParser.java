package com.lianyitech.fileparse;

import com.lianyitech.file.importer.Engine;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFComment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *excel解析器 poi 2007
 */
//@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class XLSXExcelParser implements Engine.Parser<ConcurrentNavigableMap<String, String>> {
    private FileInputStream in;

    public XLSXExcelParser(FileInputStream in, String col_title, int star_row_index) {
        this.in = in;
        this.col_title = col_title;
        this.star_row_index = star_row_index;
    }

    private String col_title;
    private int star_row_index;
    public void process(BlockingQueue<ConcurrentNavigableMap<String, String>> queue, boolean toQueue) throws Exception{
        OPCPackage pkg = OPCPackage.open(in);
        ExcelXlsxParser ExcelXlsxParser = new ExcelXlsxParser(pkg,col_title,star_row_index,queue,toQueue);
        ExcelXlsxParser.process();
    }
    private static class ExcelXlsxParser extends DefaultHandler {
        private Logger logger = LoggerFactory.getLogger(this.getClass());
        private OPCPackage pkg;
        private String title;
        private int start_row;
        BlockingQueue<ConcurrentNavigableMap<String, String>> rowTaskQueue;
        boolean to_Queue;
        //------------------------------------------------------//

        private Map<String,Object> row_map = new HashMap<>();

        private ExcelXlsxParser(OPCPackage pkg,String title,int start_row,BlockingQueue<ConcurrentNavigableMap<String, String>> rowTaskQueue,boolean to_Queue){
            this.pkg = pkg;
            this.title = title;
            this.start_row = start_row;
            this.rowTaskQueue = rowTaskQueue;
            this.to_Queue = to_Queue;
           // curRow = 0;
            //curCol = 0;
        }


        /**
         * 读取所有工作簿的入口方法
         * @throws Exception
         */
        private void process() throws Exception {
            XSSFReader r = new XSSFReader(pkg);
            ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(pkg);
            StylesTable styles = r.getStylesTable();
            //SharedStringsTable sst = r.getSharedStringsTable();
            XMLReader parser = fetchSheetParser(styles,strings);
            Iterator<InputStream> sheets = r.getSheetsData();
            while (sheets.hasNext()) {
                //curRow = 0;
               // curCol = 0;
               // sheetIndex++;
                InputStream sheet = sheets.next();
                InputSource sheetSource = new InputSource(sheet);
                parser.parse(sheetSource);
                sheet.close();
            }
        }

        private XMLReader fetchSheetParser(StylesTable styles,ReadOnlySharedStringsTable strings) throws SAXException {
            XMLReader parser = XMLReaderFactory.createXMLReader();
            //this.sst = sst;
            parser.setContentHandler(new XSSFSheetXMLHandler(styles, strings, new XSSFSheetXMLHandler.SheetContentsHandler() {
                private String getCellName(String cellReference){
                    Pattern p = Pattern.compile("[0-9]");
                    Matcher matcher = p.matcher(cellReference);
                    return matcher.replaceAll("");
                }
                @Override
                public void startRow(int rowNum) {
                    row_map.clear();
                    //curRow = rowNum;
                    ///curCol = 0;
                }

                @Override
                public void endRow(int rowNum) {
                    //curRow = rowNum;
                    try {
                        optRows(rowNum, row_map);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void cell(String cellReference, String formattedValue, XSSFComment xssfComment) {

                    // Cell c = r.getCell(cn, Row.RETURN_BLANK_AS_NULL);
                  /*  if(StringUtils.isNotEmpty(formattedValue) && StringUtils.isNotEmpty(formattedValue.trim()) && StringUtils.isNotEmpty(cellReference)&& StringUtils.isNotEmpty(cellReference.trim())){
                        row_map.put(ExcelUtil.getCellNum(getCellName(cellReference))+"",formattedValue);
                    }else{
                        row_map.put(ExcelUtil.getCellNum(getCellName(cellReference))+"","");
                    }
                    //curCol++;
                       // this.dataMap.put(getCellName(cellReference), formattedValue);
                       */
                    if (cellReference == null) {
                        //cellReference = new CellAddress(currentRow, currentCol).formatAsString();
                    }
                    CellAddress a = new CellAddress(0, 0);
                    int thisCol = (new CellReference(cellReference)).getCol();

                   // XSSFComment comment = comments.findCellComment(cellRef);
                   System.out.print((new CellReference(cellReference)).formatAsString());
                    // int missedCols = thisCol - currentCol - 1;//处理数据中间存在空白
                   // for (int i = 0; i < missedCols; i++) {
                   //     this.lRows.add("");
                   // }
                   // currentCol = thisCol;

                    // TODO 数据类型处理
                    try {

                       System.out.print((new CellReference(cellReference)).convertNumToColString(thisCol));
                    } catch (NumberFormatException e) {

                    }
                }

                @Override
                public void headerFooter(String text, boolean isHeader, String tagName) {
                    System.out.println("ddddd");
                }

            }, false));
            return parser;
        }

        private void optRows(int curRow, Map<String,Object> row_map) throws SQLException {
            if(curRow<start_row){//从哪行开始
                return;
            }
            String[] titleStr = title.split(",");
            ConcurrentNavigableMap<String, String> dataMap = new ConcurrentSkipListMap<>();
            int col = row_map.size();
            for(int i = 0; i < titleStr.length; i++) {
                if(col >= titleStr.length)
                    dataMap.put(titleStr[i], row_map.get(i+"")!=null?row_map.get(i+"").toString():"");
                else
                    dataMap.put(titleStr[i], "");
                //            System.out.print(rowlist.get(i) + "   ");
            }
            try {
                if (dataMap.keySet().size() > 0) {
                    if (to_Queue) {
                        rowTaskQueue.put(dataMap);
                    }
                }
            } catch (Exception e) {
                logger.error("put data into queue error: " + e.getMessage(), e);
            }
        }
    }

    }
