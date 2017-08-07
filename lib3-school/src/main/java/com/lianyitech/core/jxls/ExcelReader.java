package com.lianyitech.core.jxls;

import org.apache.poi.hssf.eventusermodel.*;
import org.apache.poi.hssf.eventusermodel.dummyrecord.LastCellOfRowDummyRecord;
import org.apache.poi.hssf.eventusermodel.dummyrecord.MissingCellDummyRecord;
import org.apache.poi.hssf.model.HSSFFormulaParser;
import org.apache.poi.hssf.record.*;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.util.SAXHelper;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFComment;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import javax.xml.parsers.ParserConfigurationException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

class ExcelReader {

    public static class Excel2003Reader implements HSSFListener {
        private POIFSFileSystem fs;

        /** Should we output the formula, or the value it has? */
        private boolean outputFormulaValues = true;

        /** For parsing Formulas */
        private EventWorkbookBuilder.SheetRecordCollectingListener workbookBuildingListener;
        private HSSFWorkbook stubWorkbook;

        // Records we pick up as we process
        private SSTRecord sstRecord;
        private FormatTrackingHSSFListener formatListener;

        /** So we known which sheet we're on */
        private int sheetIndex = -1;
        private int curRow = 0;
        private BoundSheetRecord[] orderedBSRs;
        private List<BoundSheetRecord> boundSheetRecords = new ArrayList<>();

        // For handling formulas with string results
        private boolean outputNextStringRecord;

        public IRowMapper rowMapper;

        private List<String> list = new ArrayList<>();
        Excel2003Reader(IRowMapper rowMapper,InputStream in) throws IOException {
            this.fs = new POIFSFileSystem(in);
            this.rowMapper = rowMapper;
        }

        /**
         * Initiates the processing of the XLS file to CSV
         */
        public void process() throws IOException {
            MissingRecordAwareHSSFListener listener = new MissingRecordAwareHSSFListener(this);
            formatListener = new FormatTrackingHSSFListener(listener);

            HSSFEventFactory factory = new HSSFEventFactory();
            HSSFRequest request = new HSSFRequest();

            if(outputFormulaValues) {
                request.addListenerForAllRecords(formatListener);
            } else {
                workbookBuildingListener = new EventWorkbookBuilder.SheetRecordCollectingListener(formatListener);
                request.addListenerForAllRecords(workbookBuildingListener);
            }

            factory.processWorkbookEvents(request, fs);
        }

        /**
         * Main HSSFListener method, processes events, and outputs the
         *  CSV as the file is processed.
         */
        @Override
        public void processRecord(Record record) {
            String thisStr = null;

            switch (record.getSid())
            {
                case BoundSheetRecord.sid:
                    boundSheetRecords.add((BoundSheetRecord)record);
                    break;
                case BOFRecord.sid:
                    BOFRecord br = (BOFRecord)record;
                    if(br.getType() == BOFRecord.TYPE_WORKSHEET) {
                        // Create sub workbook if required
                        if(workbookBuildingListener != null && stubWorkbook == null) {
                            stubWorkbook = workbookBuildingListener.getStubHSSFWorkbook();
                        }
                        sheetIndex++;
                        if(orderedBSRs == null) {
                            orderedBSRs = BoundSheetRecord.orderByBofPosition(boundSheetRecords);
                        }
                        //System.out.println(orderedBSRs[sheetIndex].getSheetname() +" [" + (sheetIndex+1) + "]:");
                    }
                    break;
                case SSTRecord.sid:
                    sstRecord = (SSTRecord) record;
                    break;
                case BlankRecord.sid:
                    thisStr = "";
                    break;
                case BoolErrRecord.sid:
                    thisStr = "";
                    break;

                case FormulaRecord.sid:
                    FormulaRecord frec = (FormulaRecord) record;
                    if(outputFormulaValues) {
                        if(Double.isNaN( frec.getValue() )) {
                            // Formula result is a string
                            // This is stored in the next record
                            outputNextStringRecord = true;
                        } else {
                            thisStr = formatListener.formatNumberDateCell(frec);
                        }
                    } else {
                        thisStr = HSSFFormulaParser.toFormulaString(stubWorkbook, frec.getParsedExpression());
                    }
                    break;
                case StringRecord.sid:
                    if(outputNextStringRecord) {
                        // String for formula
                        StringRecord srec = (StringRecord)record;
                        thisStr = srec.getString();
                        outputNextStringRecord = false;
                    }
                    break;

                case LabelRecord.sid:
                    LabelRecord lrec = (LabelRecord) record;
                    curRow = lrec.getRow();
                    thisStr =  lrec.getValue() ;
                    break;
                case LabelSSTRecord.sid:
                    LabelSSTRecord lsrec = (LabelSSTRecord) record;
                    curRow = lsrec.getRow();
                    if(sstRecord == null) {
                        thisStr =  "(No SST Record, can't identify string)";
                    } else {
                        thisStr =  sstRecord.getString(lsrec.getSSTIndex()).toString() ;
                    }
                    break;
                case NumberRecord.sid:
                    NumberRecord numrec = (NumberRecord) record;
                    curRow = numrec.getRow();
                    // Format
                    thisStr = formatListener.formatNumberDateCell(numrec);
                    break;
                default:
                    break;
            }

            // Handle missing column
            if(record instanceof MissingCellDummyRecord) {
                MissingCellDummyRecord mc = (MissingCellDummyRecord)record;
                curRow = mc.getRow();
                thisStr = "";
            }

            // If we got something to print out, do so
            if(thisStr != null) {
                list.add(thisStr.replaceAll("  ", ""));
            }

            // Handle end of row
            if(record instanceof LastCellOfRowDummyRecord) {
                // End the row
                try {
                    rowMapper.getRows(sheetIndex, curRow, list);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    list.clear();
                }
            }
        }

    }

    public static class Excel2007Reader implements XSSFSheetXMLHandler.SheetContentsHandler {
        private Map<String, String> rowMap = new HashMap<>();
        private IRowMapper rowMapper;
        private int sheetIndex = 0;
        OPCPackage opcp;

        Excel2007Reader(IRowMapper rowMapper, FileInputStream fin) throws IOException, InvalidFormatException {
            this.rowMapper = rowMapper;
            this.opcp = OPCPackage.open(fin);
        }

        void process() throws IOException, OpenXML4JException, SAXException, ParserConfigurationException {
            ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(opcp);
            XSSFReader xssfReader = new XSSFReader(opcp);
            StylesTable styles = xssfReader.getStylesTable();
            Iterator<InputStream> sheets = xssfReader.getSheetsData();
            while (sheets.hasNext()) {
                try(InputStream stream = sheets.next()) {
                    XMLReader sheetParser = SAXHelper.newXMLReader();
                    XSSFSheetXMLHandler handler = new XSSFSheetXMLHandler(styles, strings, this, true);
                    sheetParser.setContentHandler(handler);
                    sheetParser.parse(new InputSource(stream));
                }
                sheetIndex++;
            }
        }

        @Override
        public void startRow(int i) { }

        @Override
        public void endRow(int i) {
            rowMapper.getRows(sheetIndex, i, rowMap);
            rowMap.clear();
        }

        @Override
        public void cell(String s, String s1, XSSFComment xssfComment) {
            rowMap.put(s, s1!=null?s1.replaceAll("  ",""):s1);
        }

        @Override
        public void headerFooter(String s, boolean b, String s1) { }
    }

    public interface IRowMapper {
        void getRows(int sheetIndex, int curRow, List<String> rowlist) throws InterruptedException;
        void getRows(int sheetIndex, int curRow, Map<String, String> rowMap);
    }
}