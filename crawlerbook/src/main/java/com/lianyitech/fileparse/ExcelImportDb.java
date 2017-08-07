package com.lianyitech.fileparse;

import com.lianyitech.file.importer.Engine;
import com.lianyitech.file.importer.EngineFactory;
import com.lianyitech.file.importer.Importer;
import org.springframework.beans.factory.annotation.Value;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentNavigableMap;

/**
 南宁内置excel书目
 */
public class ExcelImportDb {
    @Value("${jdbc.driver}")
    private String driver = "oracle.jdbc.driver.OracleDriver";
    @Value("${jdbc.url}")
    private String url="jdbc:oracle:thin:@192.168.2.224:1521:moe1";
    @Value("${jdbc.username}")
    private String username="industry";
    @Value("${jdbc.password}")
    private String password="industry";
    private Map<String,String> best_age = new HashMap<>();
    public void execute() throws SQLException {
        best_age.put("6~12岁","0");
        best_age.put("18岁以上","2");
        best_age.put("13~18岁","1");
        best_age.put("12岁以上","1");//
        best_age.put("6~18岁","1");

        Connection conn = null;
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, username, password);
            String sql = "insert into INFO_BOOK_DIRECTORY1\n" +
                    "  (id,\n" +
                    "   isbn,\n" +
                    "   title,\n" +
                    "   TIED_TITLE,\n" +
                    "   SUB_TITLE,\n" +
                    "   AUTHOR,\n" +
                    "   TRANSLATOR,\n" +
                    "   PUBLISHING_NAME,\n" +
                    "   PUBLISHING_ADDRESS,\n" +
                    "   PUBLISHING_TIME,\n" +
                    "   LIBRARSORT_CODE,\n" +
                    "   LANGUAGE,\n" +
                    "   PRICE,\n" +
                    "   EDITION,\n" +
                    "   PAGE_NO,\n" +
                    "   MEASURE,\n" +
                    "   SUB_AUTHOR,\n" +
                    "   SUBJECT,\n" +
                    "   SERIES_NAME,\n" +
                    "   SERIES_EDITOR,\n" +
                    "   PART_NAME,\n" +
                    "   PART_NUM,\n" +
                    "   PURPOSE,\n" +
                    "   BINDING_FORM,\n" +
                    "   BEST_AGE)" +
                    "values\n" +
                    "  (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            final PreparedStatement stmt = conn.prepareStatement(sql);
            conn.setAutoCommit(true);
            Connection finalConn = conn;
            new Importer<>(
                    new XLSXExcelParser(new FileInputStream("D:/excel/核心书目小学.xlsx"),"name,test,date",3),
                    EngineFactory.concurrentSkipListMapDummy,
                    new Engine.Handler<ConcurrentNavigableMap<String, String>>() {
                        int i = 0;
                        @Override
                        public void handle(ConcurrentNavigableMap<String, String> data) {
                            try{
                                System.out.println(data);
                                if (data.size() > 0) {
                                    i++;
                                    String uuid = UUID.randomUUID().toString().replaceAll("-", "");
                                    stmt.setString(1, uuid);
                                    stmt.setString(2, data.get("A"));
                                    stmt.setString(3, data.get("B"));
                                    stmt.setString(4, data.get("C"));
                                    stmt.setString(5, data.get("D"));
                                    stmt.setString(6, data.get("E"));
                                    stmt.setString(7, data.get("F"));
                                    stmt.setString(8, data.get("G"));
                                    stmt.setString(9, data.get("H"));
                                    stmt.setString(10, data.get("I"));
                                    stmt.setString(11, data.get("G"));
                                    stmt.setString(12, data.get("L"));
                                    stmt.setString(13, data.get("M").trim());
                                    stmt.setString(14, data.get("P"));
                                    stmt.setString(15, data.get("Q"));
                                    stmt.setString(16, data.get("R"));
                                    stmt.setString(17, data.get("S"));
                                    stmt.setString(18, data.get("T"));
                                    stmt.setString(19, data.get("U"));
                                    stmt.setString(20, data.get("V"));
                                    stmt.setString(21, data.get("W"));
                                    stmt.setString(22, data.get("X"));
                                    stmt.setString(23, data.get("AA"));
                                    stmt.setString(24, data.get("AB"));
                                    stmt.setString(25, getreaderage(data.get("AE")));
                                    stmt.addBatch();
                                    if (i % 1000 == 0) {
                                        stmt.executeBatch();
                                    }
                                }
                            }catch (Exception e){
                                System.out.println(e.getMessage());
                                try {
                                    finalConn.rollback();
                                } catch (SQLException e1) {
                                    e1.printStackTrace();
                                }
                            }
                            try {
                                stmt.executeBatch();
                            } catch (SQLException e) {
                                e.printStackTrace();

                               System.out.println(e);
                            }
                        }
                    }
            ).doImport();
        } catch (Exception e) {

            e.printStackTrace();
        }finally {
            conn.close();
        }

    }
    public String getreaderage(String value){
        return best_age.get(value);
    }

    public static void main(String[] args) {
        ExcelImportDb ExcelImportDb = new ExcelImportDb();
        try {
            ExcelImportDb.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
