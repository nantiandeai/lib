package com.lianyitech.core.config;


public class ConfigurerConstants {

    static final String JDBC_DRIVER = "jdbc.driver";

    static final String JDBC_URL = "lib3.school.jdbc.url";
    static final String JDBC_USERNAME = "lib3.school.jdbc.username";
    static final String JDBC_PASSWORD = "lib3.school.jdbc.password";
    static final String JDBC_CACHE_SIZE = "lib3.school.jdbc.prep.stmt.cache.size";
    static final String JDBC_CACHE_LIMIT = "lib3.school.jdbc.prep.stmt.cache.sql.limit";
    static final String JDBC_MIN_IDLE = "lib3.school.jdbc.connection.min.idle";
    static final String JDBC_MAX_ACTIVE = "lib3.school.jdbc.connection.max.active";

    public static final String REDIS_HOST = "redis.host";
    public static final String REDIS_PORT = "redis.port";

    static  final String BOOK_SOLR_URL = "book.solr.url";
    static final String SOLR_URL = "solr.url";
    static final String SOLR_MAX_RETRIES = "solr.maxRetries";
    static final String SOLR_CONNECTION_TIMEOUT = "solr.connectionTimeout";

    public static final String RABBIT_QUEUE_NAME = "lib3.school.rabbit.queue.name";
    static final String RABBIT_HOST = "rabbit.host";
    static final String RABBIT_USERNAME = "rabbit.username";
    static final String RABBIT_PASSWORD = "rabbit.password";
    static final String RABBIT_PORT = "rabbit.port";

    static final String PAGE_SIZE = "lib3.school.page.pageSize";

    public static final String OAUTH2_IP = "iportal.passport.url";
    public static final String IPORTAL_IPS_ADDR = "iportal.ips.url";
    public static final String INDUSTRY_URL = "lib3.industry.url";
    public static final String FILESHOW_PREFIX = "lib3.school.fileshow.prefix";

    public static final String REBUILD_QUENE = "lib3.mq.quene.isrebuild";
    public static final String IMPORT_ENABLE = "lib3.server.import.enable";
    public static final String IMPORT_QUEUE = "lib3.mq.queue";
    public static final String MQ_NUM = "lib3.mq.num";



    public static final String LIB3_EXPORT_PAGESIZE = "lib3.export.pagesize";
}
