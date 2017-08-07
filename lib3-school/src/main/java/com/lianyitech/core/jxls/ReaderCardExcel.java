package com.lianyitech.core.jxls;

import com.lianyitech.common.utils.Global;
import com.lianyitech.modules.circulate.entity.Reader;
import net.sf.jxls.exception.ParsePropertyException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.jxls.common.Context;
import org.jxls.reader.ReaderBuilder;
import org.jxls.reader.XLSReadStatus;
import org.jxls.reader.XLSReader;
import org.jxls.util.JxlsHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 导入读者数据
 * 读取
 * @author jordan jiang
 * @version 2016/8/29
 */
public class ReaderCardExcel {
    private static Logger logger = LoggerFactory.getLogger(ReaderCardExcel.class);
    private final static String xmlConfig="jxls/readerCard.xml";

    public static List readExcel(String path){
        InputStream inputXML = null;
        InputStream inputXLS=null;
        XLSReader mainReader;
        try {
            inputXML = ReaderCardExcel.class.getClassLoader().getResourceAsStream(xmlConfig);
            mainReader = ReaderBuilder.buildFromXML(inputXML );
            inputXLS = new BufferedInputStream(new FileInputStream(path));
            Reader vo=new Reader();
            List voList = new ArrayList();
            Map<String,Object> beans = new HashMap<>();
            beans.put("readerCard", vo);
            beans.put("readerCards", voList);
            XLSReadStatus readStatus = mainReader.read(inputXLS, beans);
            return voList;
        } catch (IOException e) {
            logger.error("操作失败",e);
        } catch (SAXException e) {
            logger.error("操作失败",e);
        } catch (InvalidFormatException e) {
            logger.error("操作失败",e);
        }finally {
            try{
                if(null!=inputXLS){
                    inputXLS.close();
                }
                if(null!=inputXML){
                    inputXML.close();
                }
            }catch (Exception e) {
                logger.error("操作失败",e);
            }
        }
        return null;
    }

    public static void writeExcel(List list, String errorFileName){
        Context context = new Context();
        context.putVar("readersList", list);
        String path=WriteBookDirExcel.class.getClassLoader().getResource("").getPath();
        String uploadFilePath = Global.getUploadRootPath() + Global.UPLOADFILES_BASE_URL + "/tmp";
        OutputStream outputStream = null ;
        InputStream inputStream = null ;
        try {
            outputStream = new FileOutputStream(new File(uploadFilePath + File.separator +errorFileName));
            inputStream = new FileInputStream(new File(path+"jxls/readerTemplate-jxls.xlsx"));
            JxlsHelper.getInstance().processTemplate(inputStream,outputStream,context);
        } catch (ParsePropertyException e) {
            logger.error("操作失败",e);
        } catch (IOException e) {
            logger.error("操作失败",e);
        } finally {
            try{
                if(null!=inputStream){
                    inputStream.close();
                }
                if(null!=outputStream){
                    outputStream.close();
                }
            }catch (Exception e) {
                logger.error("操作失败",e);
            }
        }
    }

    public static void main(String[] args) {
        List<Reader> list=ReaderCardExcel.readExcel("tmp/读者导入模板.xls");
    }
}
