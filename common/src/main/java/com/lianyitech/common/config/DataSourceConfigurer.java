package com.lianyitech.common.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.SQLException;

public class DataSourceConfigurer {

    public static HikariDataSource hikariDataSource(String url, String username, String password, String driver, int prepStmtCacheSize, int prepStmtCacheSqlLimit) {
        return DataSourceConfigurer.hikariDataSource(url, username, password, driver, prepStmtCacheSize, prepStmtCacheSqlLimit, 100, 100);
    }

    public static HikariDataSource hikariDataSource(String url, String username, String password, String driver, int prepStmtCacheSize, int prepStmtCacheSqlLimit, int minimumIdle, int maximumPoolSize) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(password);
        config.setDriverClassName(driver);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", prepStmtCacheSize);
        config.addDataSourceProperty("prepStmtCacheSqlLimit", prepStmtCacheSqlLimit);
        config.setMinimumIdle(minimumIdle);
        config.setMaximumPoolSize(maximumPoolSize);
        return new HikariDataSource(config);
    }

    public static DruidDataSource druidDataSource(String url, String username, String password, String driver, int prepStmtCacheSize, int prepStmtCacheSqlLimit, int minimumIdle, int maximumPoolSize) throws SQLException {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setDriverClassName(driver);
        dataSource.setPoolPreparedStatements(true);
        dataSource.setMaxPoolPreparedStatementPerConnectionSize(prepStmtCacheSize);


        dataSource.setInitialSize(minimumIdle);
        dataSource.setMinIdle(minimumIdle);
        dataSource.setMaxActive(maximumPoolSize);
        dataSource.setMinEvictableIdleTimeMillis(300000);
        dataSource.setMaxWait(6000 );
        dataSource.setValidationQuery("SELECT 1 FROM dual");
        dataSource.setTestWhileIdle(true);
        dataSource.setTestOnBorrow(false);
        dataSource.setTestOnReturn(false);

        dataSource.setFilters("stat");
        return dataSource;
    }

    public static DruidDataSource druidDataSource(String url, String username, String password, String driver, int prepStmtCacheSize, int prepStmtCacheSqlLimit) throws SQLException {
        return DataSourceConfigurer.druidDataSource(url, username, password, driver, prepStmtCacheSize, prepStmtCacheSqlLimit, 100, 100);
    }
}
