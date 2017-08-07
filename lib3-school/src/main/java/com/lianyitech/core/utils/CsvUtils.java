package com.lianyitech.core.utils;

import com.lianyitech.common.utils.StringUtils;
import com.lianyitech.core.enmu.EnumCertStatus;
import com.lianyitech.core.enmu.EnumReaderType;
import com.lianyitech.modules.catalog.entity.Copy;
import com.lianyitech.modules.catalog.entity.NewbookNotifiy;
import com.lianyitech.modules.circulate.entity.CirculateLogDTO;
import com.lianyitech.modules.circulate.entity.CompenRecord;
import com.lianyitech.modules.circulate.entity.DepositRecord;
import com.lianyitech.modules.circulate.entity.Reader;
import com.lianyitech.modules.common.ExcelTransformer;
import com.lianyitech.modules.report.entity.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.lianyitech.modules.common.ExcelTransformer.pastDayName;
import static com.lianyitech.modules.common.ExcelTransformer.readerType;

/**
 * Created by Administrator on 2017/6/7.
 */
public class CsvUtils {

    /**
     * 导出
     *
     * @param  out csv文件(路径+文件名)，csv文件不存在会自动创建
     * @param dataList 数据
     * @return
     */
    public static boolean exportCsv(OutputStream out, List<String> dataList){
        boolean isSucess=false;
        //FileOutputStream out=null;
        OutputStreamWriter osw=null;
        BufferedWriter bw=null;
        try {
            //out = new FileOutputStream(file);
            osw = new OutputStreamWriter(out,"GBK");
            bw =new BufferedWriter(osw);
            if(dataList!=null && !dataList.isEmpty()){

                for(String str: dataList){
                    bw.append(str).append("\r");
                }
            }
            isSucess=true;
        } catch (Exception e) {
            isSucess=false;
        }finally{
            if(bw!=null){
                try {
                    bw.close();
                    bw=null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(osw!=null){
                try {
                    osw.close();
                    osw=null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(out!=null){
                try {
                    out.close();
                    out=null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        return isSucess;
    }


    public static void writeResponse(InputStream in, HttpServletResponse response) throws IOException {
        byte[] bytes = new byte[1024];
        while(in.read(bytes)!=-1) {
            response.getOutputStream().write(bytes);
            bytes = new byte[1024];
        }
    }

    /**
     * 导出
     *
     * @param dataList 数据
     * @return
     */
    public static boolean exportBookReportCsv(BufferedWriter bw, List<String> dataList){
        boolean isSucess=false;
        try{
            if(dataList!=null && !dataList.isEmpty()){
                for(String str: dataList){
                    bw.write(str+"\r");
                }
            }
            bw.flush();
            isSucess=true;
        } catch (Exception e) {
            isSucess=false;
        }
        return isSucess;
    }

    public static String parseDepositRecord(DepositRecord depositRecord,int i){
        StringBuffer result = new StringBuffer();
        String opType = "";
        if("0".equals(depositRecord.getOpType())){
            opType = "交押金";
        }else if("1".equals(depositRecord.getOpType())){
            opType = "退押金";
        }else if("2".equals(depositRecord.getOpType())){
            opType = "旧证转出";
        }else if("3".equals(depositRecord.getOpType())){
            opType = "新证转入";
        }
        result.append(i+1).append(",").append(getStr(depositRecord.getReaderCard())).append(",").append(getStr(depositRecord.getReaderName())).append(",").append(getStr(depositRecord.getAmount()+"")).append(",").append(getStr(opType)).append(",")
                .append(getStr(depositRecord.getCreateBy())).append(",").append(parseDate(depositRecord.getCreateDate()));
        return result.toString();

    }

    /**
     * 导出
     *
     * @param dataList 数据
     * @return
     */
    public static boolean exportCommonCsv(BufferedWriter bw, List<String> dataList){
        boolean isSucess=false;
        try{
            if(dataList!=null && !dataList.isEmpty()){
                for(String str: dataList){
                    bw.write(str+"\r");
                }
            }
            bw.flush();
            isSucess=true;
        } catch (Exception e) {
            isSucess=false;
        }
        return isSucess;
    }


    public static String parseLine(Copy copy){
        StringBuffer result = new StringBuffer();
        result.append(getStr(copy.getIsbn())).append(",").append(getStr(copy.getIndexnum())).append(",").append(getStr(copy.getBarcode())).append(",")
                .append(getStr(copy.getLibrarsortCode())).append(",").append(getStr(copy.getTitle())).append(",").append(getStr(copy.getSeriesName()))
                .append(",").append(getStr(copy.getAuthor())).append(",").append(getStr(copy.getPublishingName())).append(",").append(getStr(copy.getPublishingTime()))
                .append(",").append(copy.getPrice()!=null?copy.getPrice():"").append(",").append(parseDate(copy.getCreateDate()))
                .append(",").append(getStr(copy.getCollectionSiteName())).append(",").append(getStr(copy.getStatus()));
        return result.toString();
    }

    public static String parseReader(Reader reader){
        StringBuffer result = new StringBuffer();
        String sex = "";
        if(reader.getSex()!=null && "0".equals(reader.getSex())){
            sex = "男";
        }
        if(reader.getSex()!=null && "1".equals(reader.getSex())){
            sex = "女";
        }
        result.append(getStr(reader.getCard())).append(",").append(getStr(reader.getName())).append(",").append(getStr(sex)).append(",").append(getStr(reader.getGroupName())).append(",")
                .append(getStr(EnumReaderType.parse(reader.getReaderType()).getName())).append(",").append(getStr(reader.getDeposit().toString())).append(",").append(parseDate(reader.getStartDate())).append(",").append(parseDate(reader.getEndDate()))
                .append(",").append(getStr(EnumCertStatus.parse(reader.getStatus()).getName()));
        return result.toString();
    }

    public static String parseRenew(Reader reader,int index){
        StringBuffer result = new StringBuffer();
        result.append(index+1).append(",").append(parseDate(reader.getUpdateDate())).append(",").append(getStr(reader.getName())).append(",")
                .append(getStr(reader.getGroupName())).append(",").append(getStr(EnumReaderType.parse(reader.getReaderType()).getName())).append(",").append(getStr(reader.getCard()))
                .append(",").append(getStr(reader.getNewCard()));
        return result.toString();
    }

    public static String parserNewBookReports(NewbookNotifiy newbookNotifiy){
        StringBuffer result = new StringBuffer();
        result.append(getStr(newbookNotifiy.getIsbn())).append(",").append(getStr(newbookNotifiy.getIndexnum())).append(",").append(getStr(newbookNotifiy.getTitle())).append(",")
                .append(getStr(newbookNotifiy.getAuthor())).append(",").append(getStr(newbookNotifiy.getPublishingName())).append(",").append(getStr(newbookNotifiy.getPublishingTime())).append(",").append(getStr(newbookNotifiy.getPrice()));
        return result.toString();
    }

    public static String parserCirculate(CirculateLogDTO circulateLogDTO,int index){
        StringBuffer result = new StringBuffer();
        result.append(index+1).append(",").append(getStr(circulateLogDTO.getReaderCardId())).append(",").append(getStr(circulateLogDTO.getName())).append(",")
                .append(getStr(circulateLogDTO.getBarcode())).append(",").append(getStr(circulateLogDTO.getTitle())).append(",").append(parseCommon(circulateLogDTO.getPrice())).append(",").append(parseDate(circulateLogDTO.getBorrowDate()))
                .append(",").append(parseDate(circulateLogDTO.getShouldReturnDate())).append(",").append(parseDate(circulateLogDTO.getReturnDate())).append(",").append(getStr(circulateLogDTO.getStatusName()))
                .append(",").append(parseDate(circulateLogDTO.getCreateDate()));
        return result.toString();
    }

    public static String parserPastDayInfo(CirculateLogDTO circulateLogDTO,int index){
        StringBuffer result = new StringBuffer();
        result.append(index + 1).append(",").append(getStr(circulateLogDTO.getReaderCardId())).append(",").append(getStr(circulateLogDTO.getName())).append(",").append(getStr(circulateLogDTO.getGroupName())).append(",")
                .append(getStr(circulateLogDTO.getBarcode())).append(",").append(getStr(circulateLogDTO.getTitle())).append(",").append(parseDate(circulateLogDTO.getBorrowDate()))
                .append(",").append(parseDate(circulateLogDTO.getShouldReturnDate())).append(",").append(getStr(pastDayName(circulateLogDTO))).append(",").append(parseCommon(circulateLogDTO.getPastDay()));
        return result.toString();
    }

    public static String parserListBorrowing(CirculateLogDTO circulateLogDTO,int index){
        StringBuffer result = new StringBuffer();
        result.append(index + 1).append(",").append(getStr(circulateLogDTO.getBarcode())).append(",").append(getStr(circulateLogDTO.getTitle())).append(",").append(parseDate(circulateLogDTO.getBorrowDate()))
                .append(",").append(parseDate(circulateLogDTO.getShouldReturnDate())).append(",").append(getStr(circulateLogDTO.getReaderCardId())).append(",").append(getStr(circulateLogDTO.getName())).append(",").append(getStr(circulateLogDTO.getGroupName()))
                .append(",").append(getStr(readerType(circulateLogDTO.getReaderType()))).append(",").append(getStr(pastDayName(circulateLogDTO))).append(",").append(parseCommon(pastDay(circulateLogDTO.getPastDay())));
        return result.toString();
    }
    public static Integer pastDay(Integer pasetDay) {
        return (pasetDay!=null&&pasetDay>0)?pasetDay:0;
    }

    public static String parseGroupRank(GroupRank groupRank, int ranking){
        StringBuffer resutl = new StringBuffer();
        resutl.append(parseCommon(ranking)).append(",").append(getStr(groupRank.getGroupName())).append(",").append(parseCommon(groupRank.getGroupNumber())).append(",").append(parseCommon(groupRank.getBorrowNum()));
        return resutl.toString();
    }

    public static String parseReaderRank(ReaderRank readerRank, int ranking){
        StringBuffer resutl = new StringBuffer();
        resutl.append(parseCommon(ranking)).append(",").append(getStr(readerRank.getReaderName())).append(",").append(getStr(readerRank.getReaderTypeName())).append(",").append(getStr(readerRank.getGroupName())).append(",").append(parseCommon(readerRank.getBorrowNum()));
        return resutl.toString();
    }

    public static String parserClassRateExport(CirculateStatistics circulateStatistics){
        StringBuffer result = new StringBuffer();
        result.append(getStr(circulateStatistics.getFiveClass())).append(",").append(getStr(circulateStatistics.getFiveName())).append(",").append(parseCommon(circulateStatistics.getBookSpecies())).append(",")
                .append(getStr(circulateStatistics.getSpeciesRate())).append(",").append(parseCommon(circulateStatistics.getBookNum())).append(",")
                .append(getStr(circulateStatistics.getBookNumRate())).append(",").append(parseCommon(circulateStatistics.getClassPrice())).append(",").append(getStr(circulateStatistics.getPriceRate()));
        return result.toString();
    }

    public static String parserBookDistributeRate(CirculateStatistics circulateStatistics){
        StringBuffer result = new StringBuffer();
        result.append(getStr(circulateStatistics.getClassCode())).append(",").append(getStr(circulateStatistics.getClassName())).append(",").append(parseCommon(circulateStatistics.getBookSpecies())).append(",")
                .append(getStr(circulateStatistics.getSpeciesRate())).append(",").append(parseCommon(circulateStatistics.getBookNum())).append(",")
                .append(getStr(circulateStatistics.getBookNumRate())).append(",").append(parseCommon(circulateStatistics.getClassPrice())).append(",").append(getStr(circulateStatistics.getPriceRate()));
        return result.toString();
    }

    public static String parserCirculateRate(CirculateStatistics circulateStatistics){
        StringBuffer result = new StringBuffer();
        result.append(getStr(circulateStatistics.getClassCode())).append(",").append(getStr(circulateStatistics.getClassName())).append(",").append(parseCommon(circulateStatistics.getBookNum())).append(",")
                .append(parseCommon(circulateStatistics.getBorrowNum())).append(",").append(getStr(circulateStatistics.getCirculateRate()));
        return result.toString();
    }

    public static String parserLiterature(Statements statements, int index){
        StringBuffer resutl = new StringBuffer();
        resutl.append(index+1).append(",")
                .append(getStr(statements.getTitle())).append(",")
                .append(getStr(statements.getAuthor())).append(",")
                .append(getStr(statements.getPublishingName())).append(",")
                .append(getStr(statements.getPublishingTime())).append(",")
                .append(statements.getBorrow());

        return resutl.toString();
    }

    public static String parserDocumentCirculate(Statements statements,int index){
        StringBuffer resutl = new StringBuffer();
        resutl.append(index+1).append(",")
                .append(getStr(statements.getBarcode())).append(",")
                .append(getStr(statements.getTitle())).append(",")
                .append(getStr(statements.getAuthor())).append(",")
                .append(getStr(statements.getPublishingName())).append(",")
                .append(getStr(statements.getPublishingTime())).append(",")
                .append(statements.getBorrow()).append(",")
                .append(statements.getRenew()).append(",")
                .append(statements.getSubscribe()).append(",")
                .append(statements.getOrderBorrow());

        return resutl.toString();
    }

    public static String parserBorrowingStatistics(Statements statements,int index){
        StringBuffer resutl = new StringBuffer();
        resutl.append(index+1).append(",")
                .append(getStr(statements.getCertNum())).append(",")
                .append(getStr(statements.getName())).append(",")
                .append(getStr(statements.getReaderTypeName())).append(",")
                .append(getStr(statements.getGroupName())).append(",")
                .append(parseCommon(statements.getBorrow())).append(",")
                .append(parseCommon(statements.getRenew())).append(",")
                .append(parseCommon(statements.getSubscribe())).append(",")
                .append(parseCommon(statements.getOrderBorrow())).append(",")
                .append(parseCommon(statements.getExceed())).append(",")
                .append(parseCommon((statements.getCompenCount())));
        return resutl.toString();
    }

    public static String parserGroupStatistics(Statements statements, int index){
        StringBuffer resutl = new StringBuffer();
        resutl.append(index+1).append(",")
                .append(getStr(statements.getGroupName())).append(",")
                .append(statements.getBorrow()).append(",")
                .append(statements.getRenew()).append(",")
                .append(statements.getSubscribe()).append(",")
                .append(statements.getOrderBorrow()).append(",")
                .append(statements.getExceed()).append(",")
                .append(statements.getCompenCount());

        return resutl.toString();
    }

    public static String parserfiveClass(CirculateStatistics circulateStatistics){
        StringBuffer resutl = new StringBuffer();
        resutl.append(getStr(circulateStatistics.getFiveClass())).append(",")
                .append(getStr(circulateStatistics.getFiveName())).append(",")
                .append(circulateStatistics.getBookSpecies()).append(",")
                .append(getStr(circulateStatistics.getSpeciesRate())).append(",")
                .append(circulateStatistics.getBookNum()).append(",")
                .append(getStr(circulateStatistics.getBookNumRate())).append(",")
                .append(circulateStatistics.getClassPrice()).append(",")
                .append(getStr(circulateStatistics.getPriceRate()));

        return resutl.toString();
    }

    public static String parserbookDistribute(CirculateStatistics circulateStatistics){
        StringBuffer resutl = new StringBuffer();
        resutl.append(getStr(circulateStatistics.getClassCode())).append(",")
                .append(getStr(circulateStatistics.getClassName())).append(",")
                .append(circulateStatistics.getBookSpecies()).append(",")
                .append(getStr(circulateStatistics.getSpeciesRate())).append(",")
                .append(circulateStatistics.getBookNum()).append(",")
                .append(getStr(circulateStatistics.getBookNumRate())).append(",")
                .append(circulateStatistics.getClassPrice()).append(",")
                .append(getStr(circulateStatistics.getPriceRate()));

        return resutl.toString();
    }

    public static String parsercollectBook(CollectBookStatistics collectBookStatistics){
        StringBuffer resutl = new StringBuffer();
        resutl.append(getStr(ExcelTransformer.isSite(collectBookStatistics))).append(",")
                .append(getStr(collectBookStatistics.getBarcode())).append(",")
                .append(getStr(collectBookStatistics.getIndexNum())).append(",")
                .append(getStr(collectBookStatistics.getIsbn())).append(",")
                .append(getStr(collectBookStatistics.getTitle())).append(",")
                .append(getStr(collectBookStatistics.getAuthor()));

        return resutl.toString();
    }

    public static String parserCompen(CompenRecord compenRecord){
        StringBuffer result = new StringBuffer();
        result.append(getStr(compenRecord.getReaderCard())).append(",").append(getStr(compenRecord.getReaderName())).append(",").append(getStr(compenRecord.getReaderGroup()+"")).append(",")
                .append(getStr(compenRecord.getBarcode())).append(",").append(getStr(compenRecord.getTitle())).append(",").append(getStr(compenRecord.getOpTypeName())).append(",")
                .append(getStr(compenRecord.getCompenTypeName())).append(",").append(parseCommon(compenRecord.getAmount())).append(",").append(getStr(compenRecord.getNewBarcode()))
                .append(",").append(getStr(compenRecord.getCreateBy())).append(",").append(parseDate(compenRecord.getCreateDate()));
        return result.toString();
    }

    public static String parserCompenBook(CompenRecord compenRecord){
        StringBuffer result = new StringBuffer();
        result.append(getStr(compenRecord.getReaderCard())).append(",").append(getStr(compenRecord.getReaderName())).append(",").append(getStr(compenRecord.getReaderGroup()+"")).append(",")
                .append(getStr(compenRecord.getBarcode())).append(",").append(getStr(compenRecord.getTitle())).append(",").append(getStr(compenRecord.getOpTypeName())).append(",")
                .append(getStr(compenRecord.getCompenTypeName())).append(",").append(getStr(compenRecord.getNewBarcode())).append(",").append(getStr(compenRecord.getRemarks()))
                .append(",").append(getStr(compenRecord.getCreateBy())).append(",").append(parseDate(compenRecord.getCreateDate()));
        return result.toString();
    }

    public static String parseCommon(Number d) {
        String result = "" ;
        if(d!=null)
            result = "\"" + d + "\"\t";
        return result;
    }

    public static String parseDate(Date date) {
        String result = "" ;
        if(date!=null)
         result = "\"" + new SimpleDateFormat("yyyy-MM-dd").format(date) + "\"\t";
        return result;
    }

    public static String getStr(String str) {
        String result = "";
        if(StringUtils.isNotBlank(str)) {
            result = "\"" + str + "\"\t";
        }
        return result;
    }
}
