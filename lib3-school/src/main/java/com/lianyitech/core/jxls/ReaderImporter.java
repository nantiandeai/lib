package com.lianyitech.core.jxls;

import com.lianyitech.common.utils.StringUtils;
import com.lianyitech.core.utils.CommonUtils;
import com.lianyitech.modules.catalog.dao.BookDirectoryDao;
import com.lianyitech.modules.catalog.entity.ImportRecord;
import com.lianyitech.modules.catalog.service.BookDirectoryService;
import com.lianyitech.modules.catalog.service.BookImportService;
import com.lianyitech.modules.circulate.entity.Reader;
import com.lianyitech.modules.circulate.service.ReaderService;
import com.lianyitech.modules.sys.entity.User;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

import static com.lianyitech.core.utils.CommonUtils.checkIsNotNull;

@RequiredArgsConstructor
public class ReaderImporter {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public enum ReaderFileType {
        XLS,
        XLSX
    }

    @NonNull
    private FileInputStream fin;

    @NonNull
    private ImportRecord record;

    @NonNull
    private String errorFilePath;

    @NonNull
    private ReaderService service;



    @NonNull
    private User user;

    private List<Reader> errors = Collections.synchronizedList(new ArrayList<>());

    private final List<String> cols = Arrays.asList(
            "card", "name", "sex","readerTypeName", "groupName", "terminationDate",
            "certName", "certNum", "phone", "oldReaderId");
    private final static Map<String, String> colsMap = new HashMap<>();

    static {
        colsMap.put("card", "A");
        colsMap.put("name", "B");
        colsMap.put("sex", "C");
        colsMap.put("readerTypeName", "D");
        colsMap.put("groupName", "E");
        colsMap.put("terminationDate", "F");
        colsMap.put("certName", "G");
        colsMap.put("certNum", "H");
        colsMap.put("phone", "I");
        colsMap.put("oldReaderId", "J");
    }
    private List<Reader> temp = Collections.synchronizedList(new ArrayList<>());

    private void handle(Reader data) throws Exception {
        data.setOrgId(user.getOrgId());
        if (service.checkReader(data)) {
            data.preInsert();
            temp.add(data);
        } else {
            //错误日志以复本表为准
            errors.add(data);
        }
        //每50条做一次插入
        if(temp.size() >= 30) {
            save ();
        }
    }
    private void save () throws InvocationTargetException, IllegalAccessException {
        try {
            String tempTableName = "reader_" + user.getOrgId();
            if (tempTableName.length() > 30) {
                tempTableName = tempTableName.substring(0, 30);
            }
            service.batchSaveTemp(temp, tempTableName);
        } catch (Exception e) {
            //由于sql异常抛出来的错
            setErrorInfoByException(temp);
            throw e;
        } finally {
            temp = Collections.synchronizedList(new ArrayList<>());
        }
    }
    private void setErrorInfoByException(List<Reader> temp) throws InvocationTargetException, IllegalAccessException {
        for(Reader data : temp){
            data.setErrorinfo("此条数据所在的批量导入中出现异常，需要整理重新导入（检查是否存在超长字段等）");
            errors.add(data);
        }
    }
    private void clean() throws Exception {
        try {
            save ();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
    public void doImport() throws Exception {
        long t1 = System.currentTimeMillis();

        String tempTableName = "reader_" + user.getOrgId();
        if (tempTableName.length() > 30) {
            tempTableName = tempTableName.substring(0, 30);
        }

        service.dropTableTemp(tempTableName);
        service.createTableTemp(tempTableName);

        Optional<ReaderFileType> first = Arrays.stream(ReaderFileType.values())
                .filter(t -> t.ordinal() == record.getFileType())
                .findFirst();
        try {
            if (first.isPresent()) {
                ReaderImporter.IParser parser = null;
                switch (first.get()) {
                    case XLS:
                        parser = new ReaderImporter.XLSPaser();
                        break;
                    case XLSX:
                        parser = new ReaderImporter.XLSXPaser();
                        break;
                }
                parser.process();

                clean();
            }



            service.insertNotExistsGroup(tempTableName);
            service.updateGroupInfoTemp(tempTableName);
            service.checkGroupInfoTemp(tempTableName);
            service.checkReaderCardTemp(user.getOrgId(), tempTableName);
            service.updateTempByItself(tempTableName);
            service.insertNotExistsReaderCard(tempTableName);
            Date updateDate = new Date();
            int SuccessNum = service.insertReaderFromTemp(updateDate,tempTableName);

            List<Reader> errorList = service.findErrorListTemp(tempTableName);
            errors.addAll(errorList);

            //写错误列表
            if (errors.size() > 0) {
                try (OutputStream os = new FileOutputStream(errorFilePath)) {
                    new ExcelGenerator(
                            "jxls/readerTemplate-jxls.xlsx",
                            os,
                            errors,
                            null
                    ).generate();
                }
                record.setErrorFileName(errorFilePath.substring(errorFilePath.lastIndexOf(File.separator) + 1, errorFilePath.length()));
            }

            long t2 = System.currentTimeMillis();
            long t3 = t2 - t1;

            record.setTime(t3);
            record.setFinishTime(new Date());
            record.setProgress("导入完成");
            record.setSuccessNum(SuccessNum);
            record.setFailNum(errors.size());
            record.setState(0);
            record.setStateName("导入完成");
        } finally {
             service.dropTableTemp(tempTableName);
        }
    }

    public interface IParser {
        void process() throws Exception;
    }

    class RowMapper implements ExcelReader.IRowMapper {

        @Override
        public void getRows(int sheetIndex, int curRow, List<String> rowlist) {
            if (curRow > 1 && rowlist.size() > 0 && checkIsNotNull(rowlist)) {
                Reader bd = new Reader();
                for (int col = 0; col < cols.size(); col++) {
                    if (rowlist.size() > col) {
                        String s = rowlist.get(col).trim();
                        setProperty(bd, cols.get(col), s);
                    }
                }
                try {
                    handle(bd);
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }

        @Override
        public void getRows(int sheetIndex, int curRow, Map<String, String> rowMap) {
            if (curRow > 1 && rowMap.size() > 0 && checkIsNotNull(rowMap)) {
                Reader bd = new Reader();
                for (String col1 : cols) {
                    String s = rowMap.get(colsMap.get(col1) + "" + (curRow + 1));
                    if (s != null) {
                        s = s.trim();
                    }
                    setProperty(bd, col1, s);
                }
                try {
                    handle(bd);
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
    }

    public class XLSPaser implements IParser {
        @Override
        public void process() throws Exception {
            ExcelReader.Excel2003Reader reader = new ExcelReader.Excel2003Reader(new ReaderImporter.RowMapper(), fin);
            reader.process();
        }
    }

    public class XLSXPaser implements IParser {
        @Override
        public void process() throws Exception {
            ExcelReader.Excel2007Reader reader = new ExcelReader.Excel2007Reader(new ReaderImporter.RowMapper(), fin);
            reader.process();
        }
    }

    private void setProperty(final Object bean, final String name, final Object value) {
        try {
            PropertyUtils.setProperty(bean, name, value);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    /**
     * 读者导入长度校验，如果所有字段校验通过则返回true,如果不通过返回false并写入error
     * @param  reader
     * @return
     */
    public static boolean checkLength (Reader reader) {
        boolean flag = true ;
        StringBuffer errorStr = new StringBuffer();
        if(!CommonUtils.checkLength(reader.getCard(),60)) {
            flag = false ;
            errorStr.append("读者证太长，不能超过20个字符；");
        }
        if(!CommonUtils.checkLength(reader.getName(),50)) {
            flag = false ;
            errorStr.append("读者姓名太长，不能超过16个字符；");
        }
        if(!CommonUtils.checkLength(reader.getGroupName(),50)) {
            flag = false ;
            errorStr.append("读者组织太长，不能超过16个字符；");
        }
        setErrorInfo(reader,errorStr.toString());
        return flag ;
    }


    public static void setErrorInfo(Reader reader,String errorInfo){
        String str = reader.getErrorinfo();
        if(StringUtils.isBlank(str)) {
            str = "";
        }
        if(StringUtils.isNotBlank(errorInfo)) {
            str = str + errorInfo;
        }
        reader.setErrorinfo(str);
    }

}
