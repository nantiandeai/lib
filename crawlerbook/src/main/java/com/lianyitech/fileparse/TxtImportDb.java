package com.lianyitech.fileparse;

import com.lianyitech.cnclawler.CnCrawler;
import com.lianyitech.common.utils.StringUtils;
import com.lianyitech.file.importer.Engine;
import com.lianyitech.file.importer.EngineFactory;
import com.lianyitech.file.importer.Importer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.ConcurrentNavigableMap;


@Component
public class TxtImportDb {
    @Value("${jdbc.driver}")
    private String driver = "oracle.jdbc.driver.OracleDriver";
    @Value("${jdbc.url}")
    private String url="jdbc:oracle:thin:@192.168.2.224:1521:moe1";
    @Value("${jdbc.username}")
    private String username="industry";
    @Value("${jdbc.password}")
    private String password="industry";
    public void execute() throws SQLException {
        Connection conn = null;
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, username, password);
            String sql = "insert into CRAWLER_BOOK_DIRECTORY\n" +
                    "  (id,\n" +
                    "   isbn,\n" +
                    "   title,\n" +
                    "   author,\n" +
                    "   price,\n" +
                    "   publishing_name,\n" +
                    "   publishing_time,\n" +
                    "   measure,\n" +
                    "   page_no,\n" +
                    "   librarsort_code,\n" +
                    "   REMARKS,\n" +
                    "   binding_form)" +
                    "values\n" +
                    "  (?,?,?,?,?,?,?,?,?,?,?,?)";
            final PreparedStatement stmt = conn.prepareStatement(sql);
            conn.setAutoCommit(true);
            Connection finalConn = conn;
            new Importer<>(
                    new TxtFileReader(),
                    EngineFactory.concurrentSkipListMapDummy,
                    new Engine.Handler<ConcurrentNavigableMap<String, String>>() {
                        int i = 0;
                        @Override
                        public void handle(ConcurrentNavigableMap<String, String> data) throws Exception {
                            try{
                                System.out.println(i);
                                if (data.size() > 0) {
                                    i++;
                                    String uuid = UUID.randomUUID().toString().replaceAll("-", "");
                                    stmt.setString(1, uuid);
                                    stmt.setString(2, data.get("isbn"));
                                    stmt.setString(3, data.get("title"));
                                    stmt.setString(4, data.get("author"));
                                    stmt.setString(5, data.get("price").replace("￥",""));
                                    stmt.setString(6, data.get("publisher"));
                                    stmt.setString(7, data.get("pubTime"));
                                    stmt.setString(8, data.get("size"));
                                    stmt.setString(9, data.get("page"));
                                    stmt.setString(10, data.get("sort"));
                                    stmt.setString(11, sub_str(data.get("content"),85));
                                    stmt.setString(12, data.get("form"));
                                    stmt.addBatch();
                                    if (i % 1000 == 0) {
                                        stmt.executeBatch();
                                    }
                                }
                            }catch (Exception e){
                                finalConn.rollback();
                            }
                            stmt.executeBatch();
                        }
                    }
            ).doImport();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            conn.close();
        }

    }
    public String sub_str(String str,int len){
        if(str!=null){
            if(str.length()<=len){
                return str;
            }else{
                return str.substring(0,len);
            }
        }
        return "";
    }

    public static void main(String[] args) {

        TxtImportDb txtImportDb = new TxtImportDb();
        try {
            txtImportDb.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
