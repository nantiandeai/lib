package com.lianyitech.fileparse;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.apache.poi.hssf.eventusermodel.EventWorkbookBuilder.SheetRecordCollectingListener;
import org.apache.poi.hssf.eventusermodel.*;
import org.apache.poi.hssf.eventusermodel.dummyrecord.LastCellOfRowDummyRecord;
import org.apache.poi.hssf.eventusermodel.dummyrecord.MissingCellDummyRecord;
import org.apache.poi.hssf.model.HSSFFormulaParser;
import org.apache.poi.hssf.record.*;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import com.lianyitech.file.importer.Engine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;


/**
 *excel解析器 poi 2003
 */
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class XLSExcelParser implements Engine.Parser<ConcurrentNavigableMap<String, String>> {
    private FileInputStream in;
    private String col_title;
    private int star_row_index;
    public void process(BlockingQueue<ConcurrentNavigableMap<String, String>> queue, boolean toQueue) throws Exception{
        POIFSFileSystem fs = new POIFSFileSystem(in);
        ExcelXlsParser excelXlsParser = new ExcelXlsParser(fs,col_title,star_row_index,queue,toQueue);
        excelXlsParser.process();
    }

    private static class ExcelXlsParser implements HSSFListener{
        private Logger logger = LoggerFactory.getLogger(this.getClass());

        private POIFSFileSystem fs;
        private String title;
        private int start_row;
        BlockingQueue<ConcurrentNavigableMap<String, String>> rowTaskQueue;
        boolean to_Queue;
        //----------------------------------------------------------------------//
        private int minColumns;
        private int lastRowNumber;
        private int lastColumnNumber;

        /** Should we output the formula, or the value it has? */
        private boolean outputFormulaValues = true;
        /** For parsing Formulas */
        private SheetRecordCollectingListener workbookBuildingListener;
        private HSSFWorkbook stubWorkbook;

        // Records we pick up as we process
        private SSTRecord sstRecord;
        private FormatTrackingHSSFListener formatListener;

        /** So we known which sheet we're on */
        private int sheetIndex = -1;
        private BoundSheetRecord[] orderedBSRs;

        private ArrayList boundSheetRecords = new ArrayList();

        // For handling formulas with string results
        private int nextRow;
        private int nextColumn;
        private boolean outputNextStringRecord;

        private int curRow;
        private List<String> row_list;
        @SuppressWarnings( "unused")
        private String sheetName;
        private ExcelXlsParser(POIFSFileSystem fs, String title,int start_row,BlockingQueue<ConcurrentNavigableMap<String, String>> rowTaskQueue,boolean to_Queue)
                throws SQLException {
            this.row_list = new ArrayList<>();
            this.fs = fs;
            this.title = title;
            this.minColumns = -1;
            this.curRow = 0;
            this.start_row = start_row;
            this.to_Queue = to_Queue;
            this.rowTaskQueue = rowTaskQueue;
        }
        /**
         * 遍历 excel 文件
         */
        private void process() throws IOException {
            MissingRecordAwareHSSFListener listener = new MissingRecordAwareHSSFListener(this);
            formatListener = new FormatTrackingHSSFListener(listener);
            HSSFEventFactory factory = new HSSFEventFactory();
            HSSFRequest request = new HSSFRequest();
            if (outputFormulaValues) {
                request.addListenerForAllRecords(formatListener);
            } else {
                workbookBuildingListener = new EventWorkbookBuilder.SheetRecordCollectingListener(formatListener);
                request.addListenerForAllRecords(workbookBuildingListener);
            }
            factory.processWorkbookEvents(request, fs);
        }

        @Override
        public void processRecord(Record record) {
            int thisRow = -1;
            int thisColumn = -1;
            String thisStr = null;
            String value;
            switch (record.getSid()) {
                case BoundSheetRecord.sid:
                    boundSheetRecords.add(record);
                    break;
                case BOFRecord.sid:
                    BOFRecord br = (BOFRecord) record;
                    if (br.getType() == BOFRecord.TYPE_WORKSHEET) {
                        // 如果有需要，则建立子工作薄
                        if (workbookBuildingListener != null && stubWorkbook == null) {
                            stubWorkbook = workbookBuildingListener.getStubHSSFWorkbook();
                        }
                        sheetIndex++;
                        if (orderedBSRs == null) {
                           orderedBSRs = BoundSheetRecord.orderByBofPosition(boundSheetRecords);
                        }
                        sheetName = orderedBSRs[sheetIndex].getSheetname();
                    }
                    break;
                case SSTRecord.sid:
                    sstRecord = (SSTRecord) record;
                    break;
                case BlankRecord.sid:
                    BlankRecord brec = (BlankRecord) record;
                    thisRow = brec.getRow();
                    thisColumn = brec.getColumn();
                    thisStr = "";
                    row_list.add(thisColumn, thisStr);
                    break;
                case BoolErrRecord.sid: //单元格为布尔类型
                    BoolErrRecord berec = (BoolErrRecord) record;
                    thisRow = berec.getRow();
                    thisColumn = berec.getColumn();
                    thisStr = berec.getBooleanValue()+"";
                    row_list.add(thisColumn, thisStr);
                    break;

                case FormulaRecord.sid: //单元格为公式类型
                    FormulaRecord frec = (FormulaRecord) record;
                    thisRow = frec.getRow();
                    thisColumn = frec.getColumn();
                    if (outputFormulaValues) {
                        if (Double.isNaN(frec.getValue())) {
                            // Formula result is a string
                            // This is stored in the next record
                            outputNextStringRecord = true;
                            nextRow = frec.getRow();
                            nextColumn = frec.getColumn();
                        } else {
                            thisStr = formatListener.formatNumberDateCell(frec);
                        }
                    } else {
                        thisStr = '"' + HSSFFormulaParser.toFormulaString(stubWorkbook,
                                frec.getParsedExpression()) + '"';
                    }
                    row_list.add(thisColumn,thisStr);
                    break;
                case StringRecord.sid://单元格中公式的字符串
                    if (outputNextStringRecord) {
                        // String for formula
                       /* StringRecord srec = (StringRecord) record;
                        thisStr = srec.getString();*/
                        thisRow = nextRow;
                        thisColumn = nextColumn;
                        outputNextStringRecord = false;
                    }
                    break;
                case LabelRecord.sid:
                    LabelRecord lrec = (LabelRecord) record;
                    curRow = thisRow = lrec.getRow();
                    thisColumn = lrec.getColumn();
                    value = lrec.getValue().trim();
                    value = value.equals("")?" ":value;
                    this.row_list.add(thisColumn, value);
                    break;
                case LabelSSTRecord.sid:  //单元格为字符串类型
                    LabelSSTRecord lsrec = (LabelSSTRecord) record;
                    curRow = thisRow = lsrec.getRow();
                    thisColumn = lsrec.getColumn();
                    if (sstRecord == null) {
                        row_list.add(thisColumn, " ");
                    } else {
                        value =  sstRecord.getString(lsrec.getSSTIndex()).toString().trim();
                        value = value.equals("")?" ":value;
                        row_list.add(thisColumn,value);
                    }
                    break;
                case NumberRecord.sid:  //单元格为数字类型
                    NumberRecord numrec = (NumberRecord) record;
                    curRow = thisRow = numrec.getRow();
                    thisColumn = numrec.getColumn();
                   // System.out.println(formatListener.getFormatString(numrec));
                    value = formatListener.formatNumberDateCell(numrec).trim();
                    value = value.equals("")?" ":value;
                    // 向容器加入列值
                    row_list.add(thisColumn, value);
                    break;
                default:
                    break;
            }
            // 遇到新行的操作
            if (thisRow != -1 && thisRow != lastRowNumber) {
                lastColumnNumber = -1;
            }

            // 空值的操作
            if (record instanceof MissingCellDummyRecord) {
                MissingCellDummyRecord mc = (MissingCellDummyRecord) record;
                curRow = thisRow = mc.getRow();
                thisColumn = mc.getColumn();
                row_list.add(thisColumn," ");
            }

            // 更新行和列的值
            if (thisRow > -1)
                lastRowNumber = thisRow;
            if (thisColumn > -1)
                lastColumnNumber = thisColumn;

            // 行结束时的操作
            if (record instanceof LastCellOfRowDummyRecord) {
                if (minColumns > 0) {
                    // 列值重新置空
                    if (lastColumnNumber == -1) {
                        lastColumnNumber = 0;
                    }
                }
                // 行结束时， 调用 optRows() 方法
                lastColumnNumber = -1;
                try {
                    optRows(curRow, row_list);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                row_list.clear();
            }
        }
        private void optRows(int curRow, List<String> row_list) throws SQLException {
            if(curRow<start_row){//从哪行开始
                return;
            }
            String[] titleStr = title.split(",");
            ConcurrentNavigableMap<String, String> dataMap = new ConcurrentSkipListMap<>();
            int col = row_list.size();
            for(int i = 0; i < titleStr.length; i++) {
                if(col >= titleStr.length)
                    dataMap.put(titleStr[i], row_list.get(i).trim());
                else
                    dataMap.put(titleStr[i], "");
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