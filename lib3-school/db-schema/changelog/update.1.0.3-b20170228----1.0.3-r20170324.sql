--liquibase formatted sql
--changeset lianyitech:2

CREATE TABLE SYS_CATE_CONFIG
(
    ID VARCHAR2(64) PRIMARY KEY NOT NULL,
    ORG_ID VARCHAR2(64),
    CNF_TYPE VARCHAR2(64),
    CNF_METHOD VARCHAR2(64),
    CREATE_BY VARCHAR2(64),
    CREATE_DATE TIMESTAMP(6),
    UPDATE_BY VARCHAR2(64),
    UPDATE_DATE TIMESTAMP(6),
    REMARKS NVARCHAR2(255),
    DEL_FLAG CHAR(1)
);
COMMENT ON COLUMN SYS_CATE_CONFIG.ID IS '主键id';
COMMENT ON COLUMN SYS_CATE_CONFIG.ORG_ID IS '机构id';
COMMENT ON COLUMN SYS_CATE_CONFIG.CNF_TYPE IS '配置类型 0索书号 1条形码';
COMMENT ON COLUMN SYS_CATE_CONFIG.CNF_METHOD IS '配置方式';
COMMENT ON COLUMN SYS_CATE_CONFIG.CREATE_BY IS '创建人';
COMMENT ON COLUMN SYS_CATE_CONFIG.CREATE_DATE IS '创建时间';
COMMENT ON COLUMN SYS_CATE_CONFIG.UPDATE_BY IS '更新人';
COMMENT ON COLUMN SYS_CATE_CONFIG.UPDATE_DATE IS '更新时间';
COMMENT ON COLUMN SYS_CATE_CONFIG.REMARKS IS '备注';
COMMENT ON COLUMN SYS_CATE_CONFIG.DEL_FLAG IS '删除标示 0未删除 1已删除';
CREATE TABLE CATALOG_BARCODE_RECORD
(
    ID VARCHAR2(64) PRIMARY KEY NOT NULL,
    ORG_ID VARCHAR2(64),
    TITLE VARCHAR2(512),
    OLD_BAR_CODE VARCHAR2(50),
    NEW_BAR_CODE VARCHAR2(50),
    CREATE_BY VARCHAR2(64),
    CREATE_DATE TIMESTAMP(6),
    UPDATE_BY VARCHAR2(64),
    UPDATE_DATE TIMESTAMP(6),
    REMARKS NVARCHAR2(255),
    DEL_FLAG CHAR(1)
);
COMMENT ON COLUMN CATALOG_BARCODE_RECORD.ID IS '主键id';
COMMENT ON COLUMN CATALOG_BARCODE_RECORD.ORG_ID IS '机构id';
COMMENT ON COLUMN CATALOG_BARCODE_RECORD.TITLE IS '题目';
COMMENT ON COLUMN CATALOG_BARCODE_RECORD.OLD_BAR_CODE IS '老条码编号';
COMMENT ON COLUMN CATALOG_BARCODE_RECORD.NEW_BAR_CODE IS '新条码编号';
COMMENT ON COLUMN CATALOG_BARCODE_RECORD.CREATE_BY IS '创建人';
COMMENT ON COLUMN CATALOG_BARCODE_RECORD.CREATE_DATE IS '创建时间';
COMMENT ON COLUMN CATALOG_BARCODE_RECORD.UPDATE_BY IS '更新人';
COMMENT ON COLUMN CATALOG_BARCODE_RECORD.UPDATE_DATE IS '更新时间';
COMMENT ON COLUMN CATALOG_BARCODE_RECORD.REMARKS IS '备注';
COMMENT ON COLUMN CATALOG_BARCODE_RECORD.DEL_FLAG IS '删除标示 0未删除 1已删除';
CREATE TABLE PERI_BINDING
(
    ID VARCHAR2(64) PRIMARY KEY NOT NULL,
    ORG_ID VARCHAR2(64),
    PERI_DIRECTORY_ID VARCHAR2(64),
    COLLECTION_SITE_ID VARCHAR2(64),
    PUBLISHING_YEAR VARCHAR2(32),
    TITLE VARCHAR2(128),
    BINDING_NUM VARCHAR2(32),
    PRICE NUMBER(*),
    BAR_CODE VARCHAR2(50),
    LIBRARSORT_CODE VARCHAR2(64),
    BOOK_TIME_NO VARCHAR2(64),
    ASS_NO VARCHAR2(64),
    SOM_NO VARCHAR2(64),
    CHECK_STATUS CHAR(1),
    STATUS CHAR(1),
    CREATE_BY VARCHAR2(64),
    CREATE_DATE TIMESTAMP(6),
    UPDATE_BY VARCHAR2(64),
    UPDATE_DATE TIMESTAMP(6),
    REMARKS NVARCHAR2(255),
    DEL_FLAG CHAR(1)
);
COMMENT ON COLUMN PERI_BINDING.ID IS '主键编号';
COMMENT ON COLUMN PERI_BINDING.ORG_ID IS '机构id';
COMMENT ON COLUMN PERI_BINDING.PERI_DIRECTORY_ID IS '期刊目录id';
COMMENT ON COLUMN PERI_BINDING.COLLECTION_SITE_ID IS '馆藏地点id';
COMMENT ON COLUMN PERI_BINDING.PUBLISHING_YEAR IS '出版年份';
COMMENT ON COLUMN PERI_BINDING.TITLE IS '刊名';
COMMENT ON COLUMN PERI_BINDING.BINDING_NUM IS '装订卷期';
COMMENT ON COLUMN PERI_BINDING.PRICE IS '装订价格';
COMMENT ON COLUMN PERI_BINDING.BAR_CODE IS '条形码';
COMMENT ON COLUMN PERI_BINDING.LIBRARSORT_CODE IS '分类号';
COMMENT ON COLUMN PERI_BINDING.BOOK_TIME_NO IS '书次号';
COMMENT ON COLUMN PERI_BINDING.ASS_NO IS '辅助区分号';
COMMENT ON COLUMN PERI_BINDING.SOM_NO IS '索刊号';
COMMENT ON COLUMN PERI_BINDING.CHECK_STATUS IS '登记状态(0用户登记和新增的合订  1通过记到步骤的合订)';
COMMENT ON COLUMN PERI_BINDING.STATUS IS '馆藏状态';
COMMENT ON COLUMN PERI_BINDING.CREATE_BY IS '创建人';
COMMENT ON COLUMN PERI_BINDING.CREATE_DATE IS '创建时间';
COMMENT ON COLUMN PERI_BINDING.UPDATE_BY IS '更新人';
COMMENT ON COLUMN PERI_BINDING.UPDATE_DATE IS '更新时间';
COMMENT ON COLUMN PERI_BINDING.REMARKS IS '备注';
COMMENT ON COLUMN PERI_BINDING.DEL_FLAG IS '删除标示 0未删除 1已删除';
CREATE TABLE PERI_BINDING_DETAIL
(
    ORDER_DETAIL_ID VARCHAR2(64) NOT NULL,
    BINDING_ID VARCHAR2(64) NOT NULL
);
COMMENT ON COLUMN PERI_BINDING_DETAIL.ORDER_DETAIL_ID IS '订购明细ID';
COMMENT ON COLUMN PERI_BINDING_DETAIL.BINDING_ID IS '过刊装订ID';
CREATE UNIQUE INDEX UN_BINDING_DETAIL ON PERI_BINDING_DETAIL (ORDER_DETAIL_ID, BINDING_ID);
CREATE TABLE PERI_DIRECTORY
(
    ID VARCHAR2(64) PRIMARY KEY NOT NULL,
    ORG_ID VARCHAR2(64),
    ISSN VARCHAR2(64),
    PERI_NUM VARCHAR2(64),
    EMAIL_NUM VARCHAR2(64),
    TITLE VARCHAR2(256),
    SUB_TITLE VARCHAR2(128),
    AUTHOR VARCHAR2(32),
    PRESS_ID VARCHAR2(64),
    LIBRARSORT_ID VARCHAR2(64),
    LIBRARSORT_CODE VARCHAR2(50),
    PRICE NUMBER(6,2),
    BOOK_SIZE VARCHAR2(32),
    PUBLISHING_FRE CHAR(1),
    LANGUAGE CHAR(1),
    LEV CHAR(1),
    PERI_TYPE CHAR(1),
    MARC64 CLOB,
    PUBLISHING_NAME VARCHAR2(512),
    CONTENT CLOB,
    CREATE_BY VARCHAR2(64),
    CREATE_DATE TIMESTAMP(6),
    UPDATE_BY VARCHAR2(64),
    UPDATE_DATE TIMESTAMP(6),
    REMARKS NVARCHAR2(255),
    DEL_FLAG CHAR(1)
);
COMMENT ON COLUMN PERI_DIRECTORY.ID IS '主键编号';
COMMENT ON COLUMN PERI_DIRECTORY.ORG_ID IS '机构id';
COMMENT ON COLUMN PERI_DIRECTORY.ISSN IS 'ISSN';
COMMENT ON COLUMN PERI_DIRECTORY.PERI_NUM IS '统一刊号';
COMMENT ON COLUMN PERI_DIRECTORY.EMAIL_NUM IS '邮发代号';
COMMENT ON COLUMN PERI_DIRECTORY.TITLE IS '刊名';
COMMENT ON COLUMN PERI_DIRECTORY.SUB_TITLE IS '副刊名';
COMMENT ON COLUMN PERI_DIRECTORY.AUTHOR IS '著者';
COMMENT ON COLUMN PERI_DIRECTORY.PRESS_ID IS '出版社id';
COMMENT ON COLUMN PERI_DIRECTORY.LIBRARSORT_ID IS '中图分类id';
COMMENT ON COLUMN PERI_DIRECTORY.LIBRARSORT_CODE IS '中图分类号';
COMMENT ON COLUMN PERI_DIRECTORY.PRICE IS '定价';
COMMENT ON COLUMN PERI_DIRECTORY.BOOK_SIZE IS '开本';
COMMENT ON COLUMN PERI_DIRECTORY.PUBLISHING_FRE IS '出版频率(枚举类型)';
COMMENT ON COLUMN PERI_DIRECTORY.LANGUAGE IS '语种(枚举类型)';
COMMENT ON COLUMN PERI_DIRECTORY.LEV IS '级别(枚举类型)';
COMMENT ON COLUMN PERI_DIRECTORY.PERI_TYPE IS '刊期类型(枚举类型)';
COMMENT ON COLUMN PERI_DIRECTORY.MARC64 IS '马克字段';
COMMENT ON COLUMN PERI_DIRECTORY.PUBLISHING_NAME IS '出版社名称';
COMMENT ON COLUMN PERI_DIRECTORY.CONTENT IS '内容简介';
COMMENT ON COLUMN PERI_DIRECTORY.CREATE_BY IS '创建人';
COMMENT ON COLUMN PERI_DIRECTORY.CREATE_DATE IS '创建时间';
COMMENT ON COLUMN PERI_DIRECTORY.UPDATE_BY IS '更新人';
COMMENT ON COLUMN PERI_DIRECTORY.UPDATE_DATE IS '更新时间';
COMMENT ON COLUMN PERI_DIRECTORY.REMARKS IS '备注';
COMMENT ON COLUMN PERI_DIRECTORY.DEL_FLAG IS '删除标示 0未删除 1已删除';
CREATE TABLE PERI_ORDER
(
    ID VARCHAR2(64) PRIMARY KEY NOT NULL,
    ORG_ID VARCHAR2(64),
    PERI_DIRECTORY_ID VARCHAR2(64),
    PUBLISHING_YEAR VARCHAR2(32),
    BATCH_NO VARCHAR2(20),
    START_TIME DATE,
    END_TIME DATE,
    ORDER_PERI NUMBER(*),
    ORDER_AMOUNT NUMBER(*),
    TOTAL_PRICE NUMBER(*),
    CREATE_BY VARCHAR2(64),
    CREATE_DATE TIMESTAMP(6),
    UPDATE_BY VARCHAR2(64),
    UPDATE_DATE TIMESTAMP(6),
    REMARKS NVARCHAR2(255),
    DEL_FLAG CHAR(1)
);
COMMENT ON COLUMN PERI_ORDER.ID IS '主键编号';
COMMENT ON COLUMN PERI_ORDER.ORG_ID IS '机构id';
COMMENT ON COLUMN PERI_ORDER.PERI_DIRECTORY_ID IS '期刊目录id';
COMMENT ON COLUMN PERI_ORDER.PUBLISHING_YEAR IS '出版年份';
COMMENT ON COLUMN PERI_ORDER.BATCH_NO IS '批次号';
COMMENT ON COLUMN PERI_ORDER.START_TIME IS '起订日期';
COMMENT ON COLUMN PERI_ORDER.END_TIME IS '止订日期';
COMMENT ON COLUMN PERI_ORDER.ORDER_PERI IS '订购期数';
COMMENT ON COLUMN PERI_ORDER.ORDER_AMOUNT IS '订购数量';
COMMENT ON COLUMN PERI_ORDER.TOTAL_PRICE IS '订购总价';
COMMENT ON COLUMN PERI_ORDER.CREATE_BY IS '创建人';
COMMENT ON COLUMN PERI_ORDER.CREATE_DATE IS '创建时间';
COMMENT ON COLUMN PERI_ORDER.UPDATE_BY IS '更新人';
COMMENT ON COLUMN PERI_ORDER.UPDATE_DATE IS '更新时间';
COMMENT ON COLUMN PERI_ORDER.REMARKS IS '备注';
COMMENT ON COLUMN PERI_ORDER.DEL_FLAG IS '删除标示 0未删除 1已删除';
CREATE TABLE PERI_ORDER_DETAIL
(
    ID VARCHAR2(64) PRIMARY KEY NOT NULL,
    ORDER_ID VARCHAR2(64),
    PERI_NUM VARCHAR2(64),
    ORDER_AMOUNT NUMBER(*),
    ARRIVE_AMOUNT NUMBER(*),
    AMOUNT NUMBER(*),
    COLLECTION_SITE_ID VARCHAR2(64),
    IS_BOUND CHAR(1),
    CREATE_BY VARCHAR2(64),
    CREATE_DATE TIMESTAMP(6),
    UPDATE_BY VARCHAR2(64),
    UPDATE_DATE TIMESTAMP(6),
    REMARKS NVARCHAR2(255),
    DEL_FLAG CHAR(1)
);
COMMENT ON COLUMN PERI_ORDER_DETAIL.ID IS '主键编号';
COMMENT ON COLUMN PERI_ORDER_DETAIL.ORDER_ID IS '期刊订购ID';
COMMENT ON COLUMN PERI_ORDER_DETAIL.PERI_NUM IS '订购期号';
COMMENT ON COLUMN PERI_ORDER_DETAIL.ORDER_AMOUNT IS '订购册数';
COMMENT ON COLUMN PERI_ORDER_DETAIL.ARRIVE_AMOUNT IS '实到册数';
COMMENT ON COLUMN PERI_ORDER_DETAIL.AMOUNT IS '库存册数(默认=实到册数)';
COMMENT ON COLUMN PERI_ORDER_DETAIL.COLLECTION_SITE_ID IS '馆藏地id';
COMMENT ON COLUMN PERI_ORDER_DETAIL.IS_BOUND IS '是否合订过 0否 1是';
COMMENT ON COLUMN PERI_ORDER_DETAIL.CREATE_BY IS '创建人';
COMMENT ON COLUMN PERI_ORDER_DETAIL.CREATE_DATE IS '创建时间';
COMMENT ON COLUMN PERI_ORDER_DETAIL.UPDATE_BY IS '更新人';
COMMENT ON COLUMN PERI_ORDER_DETAIL.UPDATE_DATE IS '更新时间';
COMMENT ON COLUMN PERI_ORDER_DETAIL.REMARKS IS '备注';
COMMENT ON COLUMN PERI_ORDER_DETAIL.DEL_FLAG IS '删除标示 0未删除 1已删除';

ALTER TABLE CATALOG_BOOK_DIRECTORY ADD BOOK_NO VARCHAR2(64);
COMMENT ON COLUMN CATALOG_BOOK_DIRECTORY.BOOK_NO IS '著作号';

ALTER TABLE CIRCULATE_RULE ADD ORG_ID VARCHAR2(64);
COMMENT ON COLUMN CIRCULATE_RULE.ORG_ID IS '机构id';

ALTER TABLE CATALOG_BATCH ADD TYPE CHAR(1);
COMMENT ON COLUMN CATALOG_BATCH.TYPE IS '类型 0书目批次 1期刊批次';

--初始化数据
insert into SYS_CATE_CONFIG (ID, ORG_ID, CNF_TYPE, CNF_METHOD, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('1', null, '0', '0', sysdate, null, null, null, '索书号=分类号+种次号+辅助区分号', '0');
commit;


alter table CATALOG_BOOK_DIRECTORY add OLD_INDEXNUM VARCHAR2(512);
comment on column CATALOG_BOOK_DIRECTORY.OLD_INDEXNUM
  is '旧索书号';

--rollback DROP TABLE SYS_CATE_CONFIG;
--rollback DROP TABLE CATALOG_BARCODE_RECORD;
--rollback DROP TABLE PERI_BINDING;
--rollback DROP TABLE PERI_BINDING_DETAIL;
--rollback DROP TABLE PERI_DIRECTORY;
--rollback DROP TABLE PERI_ORDER;
--rollback DROP TABLE PERI_ORDER_DETAIL;
--rollback ALTER TABLE CATALOG_BOOK_DIRECTORY DROP COLUMN BOOK_NO;
--rollback ALTER TABLE CIRCULATE_RULE DROP COLUMN ORG_ID;
--rollback ALTER TABLE CATALOG_BATCH DROP COLUMN TYPE;
--rollback ALTER TABLE CATALOG_BOOK_DIRECTORY DROP COLUMN OLD_INDEXNUM;