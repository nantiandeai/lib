package com.lianyitech.cnclawler;

import com.lianyitech.Proxy.IfCanUseProxy;
import com.lianyitech.file.importer.Engine;
import com.lianyitech.common.utils.StringUtils;
import com.lianyitech.file.importer.EngineFactory;
import com.lianyitech.file.importer.Importer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentNavigableMap;

/**
 * 将中国可供书目爬虫到数据库----
 */
@Component
@PropertySource("classpath:project.properties")
public class CnImportDb {
    @Value("${jdbc.driver}")
    private String driver;
    @Value("${jdbc.url}")
    private String url;
    @Value("${jdbc.username}")
    private String username;
    @Value("${jdbc.password}")
    private String password;
    public  void execute() throws SQLException {
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

            Map<String, String> IpPorts = new HashMap<>();
            IfCanUseProxy.readProxy(IpPorts);

            new Importer<>(
                    new CnCrawler(IpPorts),
                    EngineFactory.concurrentSkipListMapDummy,
                    new Engine.Handler<ConcurrentNavigableMap<String, String>>() {
                        int i = 0;
                        @Override
                        public void handle(ConcurrentNavigableMap<String, String> data) throws Exception {
                            try{
                                System.out.println(i);
                                if (data.size() > 0 && StringUtils.isNotEmpty(data.get("isbn"))&&StringUtils.isNotEmpty(data.get("title"))&&StringUtils.isNotEmpty(data.get("author"))&&StringUtils.isNotEmpty(data.get("publishing_name"))) {
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
                                    stmt.setString(11, data.get("content"));
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

}
