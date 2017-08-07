package com.lianyitech.modules.catalog.utils;

import com.lianyitech.core.utils.CsvUtils;
import com.lianyitech.modules.catalog.entity.Copy;
import com.lianyitech.modules.peri.entity.Binding;
import com.lianyitech.modules.report.entity.BookDirectoryCopy;

public class ExportCsv {
    public static String parseCopyLine(Copy copy){
        StringBuffer resutl = new StringBuffer();
        resutl.append(CsvUtils.getStr(copy.getCollectionSiteName())).append(",").append(CsvUtils.getStr(copy.getBarcode())).append(",").append(CsvUtils.getStr(copy.getIsbn())).append(",")
                .append(CsvUtils.getStr(copy.getIndexnum())).append(",").append(CsvUtils.getStr(copy.getTitle())).append(",").append(CsvUtils.getStr(copy.getAuthor()))
                .append(",").append(CsvUtils.getStr(copy.getPublishingName())).append(",").append(CsvUtils.getStr(copy.getPublishingTime())).append(",")
                .append(copy.getPrice()!=null?copy.getPrice():"").append(",").append(CsvUtils.parseDate(copy.getCreateDate())).append(",").append(CsvUtils.getStr(copy.getStatus()));
        return resutl.toString();
    }
    public static String parseCopyDetailLine(BookDirectoryCopy copy){
        StringBuffer resutl = new StringBuffer();
        resutl.append(CsvUtils.getStr(copy.getIsbn())).append(",").append(CsvUtils.getStr(copy.getIndexNum())).append(",").append(CsvUtils.getStr(copy.getBarcode())).append(",")
                .append(CsvUtils.getStr(copy.getLibrarsortCode())).append(",").append(CsvUtils.getStr(copy.getTitle())).append(",").append(CsvUtils.getStr(copy.getAuthor()))
                .append(",").append(CsvUtils.getStr(copy.getPublishingName())).append(",").append(CsvUtils.getStr(copy.getPublishingTime())).append(",")
                .append(copy.getPrice()!=null?copy.getPrice():"").append(",").append(CsvUtils.getStr(copy.getPutDate())).append(",").append(CsvUtils.getStr(copy.getCollectionName())).append(",").append(CsvUtils.getStr(copy.getStatus()));
        return resutl.toString();
    }
    public static String parseCommonCopyLine(Copy copy,int type){
        StringBuffer resutl = new StringBuffer();
        resutl.append(CsvUtils.getStr(copy.getIsbn())).append(",").append(CsvUtils.getStr(copy.getIndexnum())).append(",").append(CsvUtils.getStr(copy.getBarcode())).append(",")
                .append(CsvUtils.getStr(copy.getLibrarsortCode())).append(",").append(CsvUtils.getStr(copy.getTitle())).append(",").append(CsvUtils.getStr(copy.getAuthor()))
                .append(",").append(CsvUtils.getStr(copy.getPublishingName())).append(",").append(CsvUtils.getStr(copy.getPublishingTime())).append(",")
                .append(copy.getPrice()!=null?copy.getPrice():"").append(",").append(CsvUtils.parseDate(copy.getCreateDate()));
        if(type==4){//报废
            resutl.append(",").append(CsvUtils.getStr(copy.getScrapDate()));
        }else{
            resutl.append(",").append(CsvUtils.getStr(copy.getWeedingDate()));
        }
        resutl.append(",").append(CsvUtils.getStr(copy.getCollectionSiteName())).append(",").append(CsvUtils.getStr(copy.getStatus()));
        return resutl.toString();
    }

    //污损清单
    public static String parseStainsCopyLine(Copy copy){
        StringBuffer resutl = new StringBuffer();
        resutl.append(CsvUtils.getStr(copy.getBarcode())).append(",")
                .append(CsvUtils.getStr(copy.getIndexnum())).append(",")
                .append(CsvUtils.getStr(copy.getIsbn())).append(",")
                .append(CsvUtils.getStr(copy.getTitle())).append(",")
                .append(CsvUtils.getStr(copy.getSeriesName())).append(",")
                .append(CsvUtils.getStr(copy.getAuthor())).append(",")
                .append(CsvUtils.getStr(copy.getPublishingName())).append(",")
                .append(CsvUtils.getStr(copy.getPublishingTime())).append(",")
                .append(copy.getPrice()!=null?copy.getPrice():"").append(",")
                .append(CsvUtils.getStr(copy.getCollectionSiteName())).append(",")
                .append(CsvUtils.getStr(copy.getStatus())).append(",")
                .append(CsvUtils.parseDate(copy.getCreateDate())).append(",")
                .append(CsvUtils.getStr(copy.getStainsDate()));
        return resutl.toString();
    }

    //复本统计
    public static String parseCopyTotalLine(BookDirectoryCopy bookDirectoryCopy){
        StringBuffer resutl = new StringBuffer();
        resutl.append(CsvUtils.getStr(bookDirectoryCopy.getIsbn())).append(",").append(CsvUtils.getStr(bookDirectoryCopy.getIndexNum())).append(",")
                .append(CsvUtils.getStr(bookDirectoryCopy.getLibrarsortCode())).append(",").append(CsvUtils.getStr(bookDirectoryCopy.getTitle())).append(",").append(CsvUtils.getStr(bookDirectoryCopy.getAuthor()))
                .append(",").append(CsvUtils.getStr(bookDirectoryCopy.getPublishingName())).append(",").append(CsvUtils.getStr(bookDirectoryCopy.getPublishingTime())).append(",")
                .append(bookDirectoryCopy.getBookNum());
        return resutl.toString();
    }
    //过刊清单导出
    public static String parseBindingLine(Binding binding){
        StringBuffer resutl = new StringBuffer();
        resutl.append(CsvUtils.getStr(binding.getBarcode())).append(",").append(CsvUtils.getStr(binding.getSomNo())).append(",")
                .append(CsvUtils.getStr(binding.getTitle())).append(",").append(CsvUtils.getStr(binding.getPublishingName())).append(",").append(CsvUtils.getStr(binding.getBindingNum()))
                .append(",").append(binding.getPrice()!=null?binding.getPrice():"").append(",").append(CsvUtils.getStr(binding.getIssn())).append(",")
                .append(binding.getPeriNum()).append(",").append(CsvUtils.getStr(binding.getEmailNum())).append(",").append(CsvUtils.getStr(binding.getPublishingYear()))
                .append(",").append(CsvUtils.getStr(binding.getCollectionSiteName())).append(",").append(CsvUtils.getStr(binding.getStatus()));
        return resutl.toString();
    }
}
