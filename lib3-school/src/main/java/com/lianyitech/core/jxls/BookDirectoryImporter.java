package com.lianyitech.core.jxls;

import com.lianyitech.common.utils.BeanUtilsExt;
import com.lianyitech.common.utils.PropertiesLoader;
import com.lianyitech.common.utils.StringUtils;
import com.lianyitech.modules.catalog.entity.BookDirectoryForImport;
import com.lianyitech.modules.catalog.entity.ImportRecord;
import com.lianyitech.modules.catalog.service.BookImportService;
import com.lianyitech.modules.common.DictTransformer;
import com.lianyitech.modules.sys.entity.Dict;
import com.lianyitech.modules.sys.entity.User;
import lombok.*;
import org.apache.commons.beanutils.PropertyUtils;
import org.marc4j.MarcPermissiveStreamReader;
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
public class BookDirectoryImporter {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public enum Type {
        XLS,
        XLSX,
        ISO
    }

    @NonNull
    private FileInputStream fin;


    @NonNull
    private ImportRecord record;

    @NonNull
    private String errorFilePath;

    @NonNull
    private BookImportService service;

    @NonNull
    private User user;

    private List<BookDirectoryForImport> errors = Collections.synchronizedList(new ArrayList<>());

    private final List<String> cols = Arrays.asList("isbn", "title", "subTitle", "author", "subAuthor", "edition", "publishingAddress",
            "publishingName", "publishingTime", "unitprice", "barcode", "librarsortCode", "tanejiNo",
            "bookNo", "assNo", "collectionSiteName", "measure", "pageNo", "bindingForm", "content",
            "bestAge", "purpose", "tiedTitle", "partName", "partNum", "seriesName", "seriesEditor",
            "translator", "subject", "language", "attachmentNote", "batchNo");

    private final static Map<String, String> colsMap = new HashMap<>();
    static {
        colsMap.put("isbn","A");
        colsMap.put("title" , "B");
        colsMap.put("subTitle","C");
        colsMap.put("author","D");
        colsMap.put("subAuthor","E");
        colsMap.put("edition","F");
        colsMap.put("publishingAddress","G");
        colsMap.put("publishingName","H");
        colsMap.put("publishingTime","I");
        colsMap.put("unitprice","J");
        colsMap.put("barcode","K");
        colsMap.put("librarsortCode","L");
        colsMap.put("tanejiNo","M");
        colsMap.put("bookNo","N");
        colsMap.put("assNo","O");
        colsMap.put("collectionSiteName","P");
        colsMap.put("measure","Q");
        colsMap.put("pageNo","R");
        colsMap.put("bindingForm","S");
        colsMap.put("content","T");
        colsMap.put("bestAge","U");
        colsMap.put("purpose","V");
        colsMap.put("tiedTitle","W");
        colsMap.put("partName","X");
        colsMap.put("partNum","Y");
        colsMap.put("seriesName","Z");
        colsMap.put( "seriesEditor","AA");
        colsMap.put( "translator","AB");
        colsMap.put( "subject","AC");
        colsMap.put( "language","AD");
        colsMap.put( "attachmentNote","AE");
        colsMap.put( "batchNo","AF");
    }

    private List<BookDirectoryForImport> temp = Collections.synchronizedList(new ArrayList<>());

    public void handle(BookDirectoryForImport data) throws Exception {
        data.setOrgId(user.getOrgId());
        data.setUserLoginName(user.getLoginName());
        data.setCreateBy(user.getLoginName());
        data.setUpdateBy(user.getLoginName());
        data.setBindingFormName(data.getBindingForm());
        data.setBestAgeName(data.getBestAge());
        data.setPurposeName(data.getPurpose());
        data.setLanguageName(data.getLanguage());
        if (service.checkDirectory(data,record.getType())) {
            data.preInsert();
            temp.add(data);
        } else {
            //错误日志以复本表为准
            setErrorInfo(data);
        }
        //每50条做一次插入
        if(temp.size() >= 30) {
            save ();
        }
    }
    private void setErrorInfo(BookDirectoryForImport data) throws InvocationTargetException, IllegalAccessException {
        if (StringUtils.isNotEmpty(data.getBarcode())) {
            String[] codes = data.getBarcode().split(",");
            for (String barcode : codes) {
                BookDirectoryForImport errorDate = new BookDirectoryForImport();
                BeanUtilsExt.copyProperties(errorDate, data);
                errorDate.setBarcode(barcode);
                errors.add(errorDate);
            }
        }else{//条形码为空
            errors.add(data);
        }
    }
    private void setErrorInfoByException(List<BookDirectoryForImport> temp) throws InvocationTargetException, IllegalAccessException {
        for(BookDirectoryForImport data : temp){
            data.setErrorinfo("此条数据所在的批导入中出现异常，需要整理重新导入（检查是否存在超长字段等）");
            this.setErrorInfo(data);
        }
    }
    private void save () throws InvocationTargetException, IllegalAccessException {
        try {
            String bookTemp = service.subTableName("book_" + user.getOrgId());
            String copyTemp = service.subTableName("copy_"+user.getOrgId());
            service.batchSaveTemp(temp,bookTemp,copyTemp);
        } catch (Exception e) {
            //由于sql异常抛出来的错
            setErrorInfoByException(temp);
            throw e;
        } finally {
            temp = Collections.synchronizedList(new ArrayList<>());
        }
    }

    public void clean() throws Exception {
        try {
            save ();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    public void doImport() throws Exception {
        long t1 = System.currentTimeMillis();
        String bookTemp = service.subTableName("book_"+user.getOrgId());
        String copyTemp = service.subTableName("copy_"+user.getOrgId());
        service.dropTempTable(bookTemp);
        service.dropTempTable(copyTemp);
        service.createBookTemp(bookTemp);
        service.createCopyTemp(copyTemp);
        Optional<Type> first = Arrays.stream(Type.values()).filter(t -> t.ordinal() == record.getFileType()).findFirst();
        try {
            if (first.isPresent()) {
                IParser parser = null;
                switch (first.get()) {
                    case ISO:
                        parser = new ISOPaser();
                        break;
                    case XLS:
                        parser = new XLSPaser();
                        break;
                    case XLSX:
                        parser = new XLSXPaser();
                        break;
                }
                parser.process();
                clean();
            }
            //TODO 插入正式表
            int SuccessNum = service.importBookFromTemp(user, bookTemp, copyTemp);
            record.setSuccessNum(SuccessNum);
            List<BookDirectoryForImport> errorList = service.findErrorListTemp(bookTemp, copyTemp);
            errors.addAll(errorList);
            record.setFailNum(errors.size());
            //写错误列表
            if (errors.size() > 0) {
                try (OutputStream os = new FileOutputStream(errorFilePath)) {
                    new ExcelGenerator(
                            "jxls/bookDirTemplate-jxls.xlsx",
                            os,
                            errors,
                            this.getDictTransformer()
                    ).generate();
                }catch (Exception e){
                    record.setProgress("错误日志生成异常");
                    throw e;
                }
                record.setErrorFileName(errorFilePath.substring(errorFilePath.lastIndexOf(File.separator) + 1, errorFilePath.length()));
            }
            record.setProgress("导入完成");
        } finally {
            long t2 = System.currentTimeMillis();
            long t3 = t2 - t1;
            record.setTime(t3);
            record.setFinishTime(new Date());
            record.setState(0);
            record.setStateName("导入完成");
            service.dropTempTable(bookTemp);
            service.dropTempTable(copyTemp);
        }

    }
    private DictTransformer getDictTransformer(){
        //初始化字典表为了错误日志输出
        Map<String,Map<String,Dict>> dicmap = new HashMap<>();
        //1:语种 2:装帧形式 3:适读年龄 4:图书用途
        for (int i = 1; i <= 4; i++) {
            dicmap.put(""+i,service.getDictMap(""+i));
        }
        return new DictTransformer(dicmap);
    }

    public interface IParser<T> {
        void process() throws Exception;
    }

    @RequiredArgsConstructor
    class RowMapper implements ExcelReader.IRowMapper {

        @Override
        public void getRows(int sheetIndex, int curRow, List<String> rowlist) {
            if(curRow > 1 && rowlist.size()>0 && checkIsNotNull(rowlist)) {
                BookDirectoryForImport bd = new BookDirectoryForImport();
                for (int col = 0; col < cols.size(); col++) {
                    if(rowlist.size()>col){
                        String s = rowlist.get(col);
                        if (Objects.equals(s, "")) {
                            s = null;
                        } else {
                            s = s == null ? null : s.trim();
                        }
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
            if(curRow > 1 && rowMap.size()>0 && checkIsNotNull(rowMap)) {
                BookDirectoryForImport bd = new BookDirectoryForImport();
                for (String col1 : cols) {
                    String s = rowMap.get(colsMap.get(col1) + "" + (curRow + 1));
                    if (Objects.equals(s, "")) {
                        s = null;
                    } else {
                        s = s == null ? null : s.trim();
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

    public class XLSPaser implements IParser<BookDirectoryForImport> {
        @Override
        public void process() throws Exception {
            ExcelReader.Excel2003Reader reader = new ExcelReader.Excel2003Reader(new RowMapper(), fin);
            reader.process();
        }
    }

    public class XLSXPaser implements IParser<BookDirectoryForImport> {
        @Override
        public void process() throws Exception {
            ExcelReader.Excel2007Reader reader = new ExcelReader.Excel2007Reader(new RowMapper(), fin);
            reader.process();
        }
    }

    public class ISOPaser implements IParser<BookDirectoryForImport> {

        private Map<String, String> marcConf = new HashMap<>();

        ISOPaser() {
            new PropertiesLoader("marcConf.properties").getProperties().forEach(
                    (key, value) -> marcConf.put(String.valueOf(key), String.valueOf(value))
            );
        }

        @Override
        public void process() throws Exception {
            MarcPermissiveStreamReader reader = new MarcPermissiveStreamReader(fin, true, true, "GBK");
            while (reader.hasNext()) {
                org.marc4j.marc.Record record = reader.next();
                handleRecode(record);
            }
        }

        @Data
        @AllArgsConstructor
        class KV {
            private String key;
            private String value;
        }

        /**
         * 处理每一条记录
         * @param record 记录对象
         */
        private void handleRecode(
                org.marc4j.marc.Record record) throws Exception {
            BookDirectoryForImport bd = new BookDirectoryForImport();
            record.getDataFields().forEach(
                    field -> field.getSubfields().stream().filter(
                            subfield -> marcConf.containsKey(field.getTag() + "$" + subfield.getCode())
                    ).map(
                            subfield -> new KV(marcConf.get(field.getTag() + "$" + subfield.getCode()), subfield.getData())
                    ).forEach(
                            kv -> setProperty(bd, kv.getKey(), kv.getValue())
                    )
            );
            try {
                handle(bd);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void setProperty(final Object bean, final String name, final Object value) {
        try {
            PropertyUtils.setProperty(bean, name, value);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
