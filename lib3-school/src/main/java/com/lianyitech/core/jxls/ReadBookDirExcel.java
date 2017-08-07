package com.lianyitech.core.jxls;

import com.lianyitech.common.utils.StringUtils;
import com.lianyitech.core.excel.Engine;
import com.lianyitech.core.marc.ReadBookDirMarc;
import com.lianyitech.modules.catalog.entity.BookDirectory;
import com.lianyitech.modules.catalog.entity.ImportRecord;
import org.apache.commons.io.IOUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.jxls.reader.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.BlockingQueue;

/**
 * 使用jxls报表生成工具，把java实体类导出生成 Excel文件或导入 Excel 插入数据库
 * 读取
 * @author jordan jiang
 * @version 2016/8/29
 */
public class ReadBookDirExcel  implements Engine.Parser<BookDirectory>{
    private static Logger logger = LoggerFactory.getLogger(ReadBookDirExcel.class);
    private final static String xmlConfig="jxls/bookdir.xml";

    private String uploadFilePath;
    /**
     * 构造器
     * @param uploadFilePath Excel文件路径
     */
    public ReadBookDirExcel(String uploadFilePath) {
        this.uploadFilePath = uploadFilePath;
    }



    @Override
    public void process(BlockingQueue<BookDirectory> rowTaskQueue, boolean toQueue, ImportRecord record) {
        List<BookDirectory> voList = new ArrayList<BookDirectory>();
        String suffix = uploadFilePath.substring(uploadFilePath.lastIndexOf(".")+1);
        if ( suffix.equalsIgnoreCase("iso") ){
            ReadBookDirMarc readBookDirMarc = new ReadBookDirMarc();
            try {
                voList = readBookDirMarc.readMarcDataLine(uploadFilePath);
//							list = readBookDirMarc.readMarcData(uploadFilePath + File.separator + impFileName +"-"+ fileName);
            } catch (IOException e) {
               logger.error("操作失败",e);
            }
        } else if (suffix.equalsIgnoreCase("xls") || suffix.equalsIgnoreCase("xlsx")) {
            XLSReaderImpl mainReader;
            InputStream inputXML = null;
            InputStream inputXLS = null;
            BookDirectory vo=new BookDirectory();
            Map<String,Object> beans = new HashMap<>();
            beans.put("booklib", vo);
            beans.put("booklibs", voList);
            try {
                inputXML = ReadBookDirExcel.class.getClassLoader().getResourceAsStream(xmlConfig);
                mainReader = (XLSReaderImpl)ReaderBuilder.buildFromXML(inputXML );
                inputXLS = new BufferedInputStream(new FileInputStream(uploadFilePath));
                mainReader.read(inputXLS, beans);
                record.setTotalNum(voList.size()-1);
                for(int i = 0 ; i < voList.size()-1 ; i++) {
                    BookDirectory bookDirectory =  voList.get(i);
                    if (StringUtils.isNotBlank(bookDirectory.getTitle())) {
                        bookDirectory.setTitle(bookDirectory.getTitle().trim());
                    }
                    if (StringUtils.isNotBlank(bookDirectory.getAuthor())) {
                        bookDirectory.setAuthor(bookDirectory.getAuthor().trim());
                    }
                    if (StringUtils.isNotBlank(bookDirectory.getBarcode())) {
                        bookDirectory.setBarcode(bookDirectory.getBarcode().trim());
                    }
                    if (StringUtils.isNotBlank(bookDirectory.getIsbn())) {
                        bookDirectory.setIsbn(bookDirectory.getIsbn().trim());
                    }
                    if (StringUtils.isNotBlank(bookDirectory.getPublishingName())) {
                        bookDirectory.setPublishingName(bookDirectory.getPublishingName().trim());
                    }
                    if (StringUtils.isNotBlank(bookDirectory.getPublishingTime())) {
                        bookDirectory.setPublishingTime(bookDirectory.getPublishingTime().trim());
                    }
                    if (StringUtils.isNotBlank(bookDirectory.getLibrarsortCode())) {
                        bookDirectory.setLibrarsortCode(bookDirectory.getLibrarsortCode().trim());
                    }
                    if (StringUtils.isNotBlank(bookDirectory.getTanejiNo())) {
                        bookDirectory.setTanejiNo(bookDirectory.getTanejiNo().trim());
                    }
                    rowTaskQueue.put(voList.get(i));
                }
                BookDirectory directory = new BookDirectory();
                directory.setId("stop");
                rowTaskQueue.put(directory);
            } catch (IOException e) {
                logger.error("操作失败",e);
            } catch (SAXException e) {
                logger.error("操作失败",e);
            } catch (InvalidFormatException e) {
                logger.error("操作失败",e);
            } catch (InterruptedException e) {
                logger.error("操作失败",e);
            } finally {
                IOUtils.closeQuietly(inputXLS);
                IOUtils.closeQuietly(inputXML);
            }
        }

    }
}
