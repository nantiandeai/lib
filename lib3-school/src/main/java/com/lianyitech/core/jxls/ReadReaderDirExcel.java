package com.lianyitech.core.jxls;

import com.lianyitech.common.utils.StringUtils;
import com.lianyitech.core.excel.Engine;
import com.lianyitech.modules.catalog.entity.ImportRecord;
import com.lianyitech.modules.circulate.entity.Reader;
import org.apache.commons.io.IOUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.jxls.reader.ReaderBuilder;
import org.jxls.reader.XLSReaderImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

/**
 * 使用jxls报表生成工具，把java实体类导出生成 Excel文件或导入 Excel 插入数据库
 * 读取
 * @author jordan jiang
 * @version 2016/8/29
 */
public class ReadReaderDirExcel implements Engine.Parser<Reader>{
    private static Logger logger = LoggerFactory.getLogger(ReadReaderDirExcel.class);
    private final static String xmlConfig="jxls/readerCard.xml";

    private String uploadFilePath;
    /**
     * 构造器
     * @param uploadFilePath Excel文件路径
     */
    public ReadReaderDirExcel(String uploadFilePath) {
        this.uploadFilePath = uploadFilePath;
    }

    @Override
    public void process(BlockingQueue<Reader> rowTaskQueue, boolean toQueue, ImportRecord record) {
        List<Reader> voList = new ArrayList<Reader>();
        InputStream inputXML = null;
        InputStream inputXLS = null;
        String suffix = uploadFilePath.substring(uploadFilePath.lastIndexOf(".")+1);
        try{
            if (suffix.equalsIgnoreCase("xls") || suffix.equalsIgnoreCase("xlsx")) {
                inputXML = ReadReaderDirExcel.class.getClassLoader().getResourceAsStream(xmlConfig);
                XLSReaderImpl mainReader;
                mainReader = (XLSReaderImpl)ReaderBuilder.buildFromXML(inputXML );
                inputXLS = new BufferedInputStream(new FileInputStream(uploadFilePath));
                Reader vo=new Reader();
                Map<String,Object> beans = new HashMap<>();
                beans.put("readerCard", vo);
                beans.put("readerCards", voList);
                mainReader.read(inputXLS, beans);
            }
            record.setTotalNum(voList.size()-1);
            for(int i = 0 ; i < voList.size()-1 ; i++) {
                Reader reader = voList.get(i);
                if (StringUtils.isNotBlank(reader.getCard())) {
                    reader.setCard(reader.getCard().trim());
                }
                if (StringUtils.isNotBlank(reader.getName())) {
                    reader.setName(reader.getName().trim());
                }
                if (StringUtils.isNotBlank(reader.getReaderTypeName())) {
                    reader.setReaderTypeName(reader.getReaderTypeName().trim());
                }
                if (StringUtils.isNotBlank(reader.getGroupName())) {
                    reader.setGroupName(reader.getGroupName().trim());
                }
                if (StringUtils.isNotBlank(reader.getCard())) {
                    reader.setCard(reader.getCard().trim());
                }
                rowTaskQueue.put(voList.get(i));
            }
            Reader reader = new Reader();
            reader.setId("stop");
            rowTaskQueue.put(reader);
        }catch (IOException e){
            logger.error("操作失败",e);
        } catch (InterruptedException e) {
            logger.error("操作失败",e);
        } catch (SAXException e) {
            logger.error("操作失败",e);
        } catch (InvalidFormatException e) {
            logger.error("操作失败",e);
        } finally {
            IOUtils.closeQuietly(inputXLS);
            IOUtils.closeQuietly(inputXML);
        }

    }
}
