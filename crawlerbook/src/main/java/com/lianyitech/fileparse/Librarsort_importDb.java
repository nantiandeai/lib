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
import java.util.UUID;
import java.util.concurrent.ConcurrentNavigableMap;

/**
 中图法分类号码表导入
 */
public class Librarsort_importDb {
    @Value("${jdbc.driver}")
    private String driver = "oracle.jdbc.driver.OracleDriver";
    @Value("${jdbc.url}")
    private String url="jdbc:oracle:thin:@192.168.2.224:1521:moe1";
    @Value("${jdbc.username}")
    private String username="lib3school";
    @Value("${jdbc.password}")
    private String password="lib3school";
    public void execute() throws SQLException {
        Connection conn = null;
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, username, password);
            String sql = "insert into SYS_LIBRARSORT\n" +
                    "  (id,\n" +
                    "   CODE,\n" +
                    "   NAME,\n" +
                    "   PARENT_CODE,\n" +
                    "   LEVELS,\n" +
                    "   DEL_FLAG,\n" +
                    "   SUBJECTS)" +
                    "values\n" +
                    "  (?,?,?,?,?,?,?)";
            final PreparedStatement stmt = conn.prepareStatement(sql);
            conn.setAutoCommit(true);
            Connection finalConn = conn;
            new Importer<>(
                    new XLSXExcelParserBark(new FileInputStream("D:/excel/中图法第五版51856条.xlsx")),
                    EngineFactory.concurrentSkipListMapDummy,
                    new Engine.Handler<ConcurrentNavigableMap<String, String>>() {
                        int i = 0;
                        @Override
                        public void handle(ConcurrentNavigableMap<String, String> data) {
                            try{
                                System.out.println(i);
                                if (data.size() > 0) {
                                    i++;
                                    String uuid = UUID.randomUUID().toString().replaceAll("-", "");
                                    stmt.setString(1, uuid);
                                    stmt.setString(2, data.get("A"));
                                    stmt.setString(3, data.get("B"));
                                    stmt.setString(4, data.get("C"));
                                    stmt.setString(5, data.get("D"));
                                    stmt.setString(6, "0");
                                    stmt.setString(7,data.get("E"));
                                    stmt.addBatch();
                                    if (i % 1000 == 0) {
                                        stmt.executeBatch();
                                    }
                                }
                            }catch (Exception e){
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

    public static void main(String[] args) {
        Librarsort_importDb ExcelImportDb = new Librarsort_importDb();
        try {
            ExcelImportDb.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
