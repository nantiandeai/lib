package com.lianyitech.core.jxls;

import com.lianyitech.common.utils.StringUtils;
import com.lianyitech.modules.catalog.entity.Copy;
import com.lianyitech.modules.common.ReportCommon;
import com.lianyitech.modules.peri.entity.Binding;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenXiaoDing on 2016/9/8.
 * chenXiaoDing
 */
public class WriteBookLabelExportExcel {
    private static Logger logger = LoggerFactory.getLogger(ReadReaderDirExcel.class);

    private HSSFWorkbook workbook = null;

    /** 工作表. */
    private HSSFSheet sheet = null;

    /** 工作行. */
    private HSSFRow row = null;

    /** 单元 格. */
    private HSSFCell cell = null;

    /**
     * 书目书标excel导出
     * @param bookmarkList bookmarkList
     * @param exportType exportType 导出方式  1：仅索书号 2：索书号+条形码
     * @param out out
     * @throws Exception Exception
     */
    public void createSheet(List<Copy> bookmarkList, String filename, ServletOutputStream out, String exportType) throws Exception
    {
        File fi = new File(filename);
        if(!fi.exists())
        {
            return;
        }
        POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(fi));
        workbook = new HSSFWorkbook(fs);
        int rowSize = 10;
        int k = 0;
        //客服要求在20行的倍数的时候改成56.75行高
        int x = 1;

        sheet = workbook.getSheet("Sheet1");
        String indexnum = "";
        HSSFCellStyle style = workbook.createCellStyle();
        for(int j=0; j<bookmarkList.size(); j++)
        {
            indexnum = ReportCommon.getIndexNum(bookmarkList.get(j),exportType);
            if(StringUtils.isBlank(indexnum)) {
                continue;
            }
//            System.out.println("indexNum="+indexnum);
            int modSize = j % rowSize;
            if(modSize ==1 || modSize==2|| modSize==3 ||modSize==4
                    || modSize==6||modSize==7|| modSize==8|| modSize==9)
            {
                if(modSize == 1 || modSize == 6)
                {
                    modSize=3;
                }
                else if(modSize == 2 || modSize == 7)
                {
                    modSize=5;
                }
                else if(modSize == 3 || modSize == 8)
                {
                    modSize=7;
                }
                else if(modSize == 4 || modSize == 9)
                {
                    modSize = 9;
                }

                cell = row.createCell(modSize);
                style.setWrapText(true);
                cell.setCellValue(indexnum);
                style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                cell.setCellStyle(style);
//                // 创建HSSFPatriarch对象,HSSFPatriarch是所有注释的容器.
//                HSSFPatriarch patr = sheet.createDrawingPatriarch();
//                //注释的标签页大小
//                HSSFComment comment = patr.createComment( new HSSFClientAnchor( 0 , 0 , 0 , 0 , ( short ) 1 , (2) , ( short ) 1 , (3) ));
//                comment.setString( new HSSFRichTextString("批注"));
//                //把注释增加到cell里
//                cell.setCellComment(comment);
                k--;
            }
            else
            {

                row = sheet.createRow(j+k+1);
                cell = row.createCell(1);
                row.setHeight((short)1260);
                if( x!= 0 && x%10 == 0){
                    row.setHeight((short)1135);
                } else {
                    row.setHeight((short) 1080);
                }
                style.setWrapText(true);
                cell.setCellValue(indexnum);
                style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                cell.setCellStyle(style);

                k++;
                x++;
            }
        }
        createWrite(out);
    }

    public void createSheet1(List<Binding> bookmarkList, String filename, ServletOutputStream out, String exportType) throws Exception
    {

        File fi = new File(filename);
        if(!fi.exists())
        {
            return;
        }

        POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(fi));
        workbook = new HSSFWorkbook(fs);

        int rowSize = 10;
        int k = 0;

        sheet = workbook.getSheet("Sheet1");
        String somNo = "";
        HSSFCellStyle style = workbook.createCellStyle();
        for(int j=0; j<bookmarkList.size(); j++)
        {
            somNo = ReportCommon.getSomNo(bookmarkList.get(j),exportType);
            if(StringUtils.isBlank(somNo)) {
                continue;
            }
            int modSize = j % rowSize;

            if(modSize ==1 || modSize==2|| modSize==3 ||modSize==4
                    || modSize==6||modSize==7|| modSize==8|| modSize==9)
            {
                if(modSize == 1 || modSize == 6)
                {
                    modSize=3;
                }
                else if(modSize == 2 || modSize == 7)
                {
                    modSize=5;
                }
                else if(modSize == 3 || modSize == 8)
                {
                    modSize=7;
                }
                else if(modSize == 4 || modSize == 9)
                {
                    modSize = 9;
                }

                cell = row.createCell(modSize);
                style.setWrapText(true);
                cell.setCellValue(setHhstr(somNo));
                style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                cell.setCellStyle(style);
                k--;
            }
            else
            {
                row = sheet.createRow(j+k+1);
                cell = row.createCell(1);
                row.setHeight((short)1260);
                style.setWrapText(true);
                cell.setCellValue(setHhstr(somNo));
                style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                cell.setCellStyle(style);

                k++;
            }
        }
        createWrite(out);
    }


    /**
     * txt导出
     * @param out out
     */
    private void createWrite(ServletOutputStream  out)
    {
        try
        {
            workbook.write(out);
        }
        catch (FileNotFoundException e)
        {
            logger.error("BookMarkExport createWrite FileNotFoundException:"+e.getMessage());
        }
        catch (IOException e)
        {
            logger.error("BookMarkExport createWrite IOException:"+e.getMessage());
        }
        finally
        {
            if(out != null)
            {
                try
                {
                    out.flush();
                    out.close();
                }
                catch (IOException e)
                {
                    logger.error("BookMarkExport createWrite IOException:"+e.getMessage());
                }
            }
        }
    }



    //2016-2-25加换行符每10个字符一行
    public static String setHhstr(String paramStr){
        StringBuffer result= new StringBuffer();
        String[] array = paramStr.split("\n");
        for (String str : array) {
            int len = str.length();
            String zfc = "";
            if(len>10){
                int xhcs = len/10;
                int jq = len%10;
                for(int i=0;i<xhcs;i++){
                    zfc += str.substring(i*10,(i+1)*10);
                    if(i<xhcs-1||jq>0){
                        zfc+="\n";
                    }
                }
                zfc+=str.substring(xhcs*10,xhcs*10+jq);
            }else{
                zfc = str;
            }
            result.append(zfc).append("\n");
        }
        return result.substring(0,result.length() -1);
    }

    public void exportBookmarkList(
            List<Copy> bookmarkList,
            HttpServletResponse response,String exportType) throws Exception {
        // TODO Auto-generated method stub
        if (bookmarkList == null) {
            return;
        }
        BufferedOutputStream buffered = null;
        StringBuffer write = new StringBuffer();
        ServletOutputStream outstream = null;

        try {
            outstream = response.getOutputStream();
            buffered = new BufferedOutputStream(outstream);
            String indexnum ="";
            for (int i = 0; i < bookmarkList.size(); i++) {
                indexnum = ReportCommon.getIndexNumTxt(bookmarkList.get(i),exportType);
                if(StringUtils.isBlank(indexnum)) {
                    continue;
                }
                if (indexnum != null) {
                    write.append(indexnum + "\r\n");
                }
            }
            buffered.write(write.toString().getBytes("UTF-8"));
            buffered.flush();
        } catch (Exception e) {
            logger.error("exportbarcodetxt error: " + e.getMessage());
        } finally {
            if (buffered != null) {
                buffered.close();
            }

            if (outstream != null) {
                outstream.close();
            }
        }
    }

    public void exportBookmarkList1(
            List<Binding> bookmarkList,
            HttpServletResponse response ,String exportType) throws Exception {
        // TODO Auto-generated method stub
        if (bookmarkList == null) {
            return;
        }
        BufferedOutputStream buffered = null;
        StringBuffer write = new StringBuffer();
        ServletOutputStream outstream = null;

        try {
            outstream = response.getOutputStream();
            buffered = new BufferedOutputStream(outstream);
            String somNo ="";
            for (int i = 0; i < bookmarkList.size(); i++) {
                somNo = ReportCommon.getSomNoTxt(bookmarkList.get(i),exportType);
                if(StringUtils.isBlank(somNo)) {
                    continue;
                }
                if (somNo != null ) {
                    write.append(somNo + "\r\n");
                }
            }
            buffered.write(write.toString().getBytes("UTF-8"));
            buffered.flush();
        } catch (Exception e) {
            logger.error("exportbarcodetxt error: " + e.getMessage());
        } finally {
            if (buffered != null) {
                buffered.close();
            }

            if (outstream != null) {
                outstream.close();
            }
        }
    }

    public static void main(String[] args){
        List<Copy> list = new ArrayList<>();
        Copy copy = new Copy();
        copy.setIndexnum("A/123");
        Copy copy2 = new Copy();
        copy2.setIndexnum("A/1232");
        Copy copy3 = new Copy();
        copy3.setIndexnum("A/1233");
        Copy copy4 = new Copy();
        copy4.setIndexnum("A/1234");
        Copy copy5 = new Copy();
        copy5.setIndexnum("A/1235");
        Copy copy6 = new Copy();
        copy6.setIndexnum("A/1236");
        list.add(copy);
        list.add(copy2);
        list.add(copy3);
        list.add(copy4);
        list.add(copy5);
        list.add(copy6);

        WriteBookLabelExportExcel wblee = new WriteBookLabelExportExcel();
//        wblee.createSheet(list,);


    }


}
