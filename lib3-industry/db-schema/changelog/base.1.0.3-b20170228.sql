--liquibase formatted sql
--changeset lianyitech:1

create table DIM_BOOK_CATEGORY
(
  id                 VARCHAR2(64) not null,
  small_classes_code CHAR(1),
  small_classes_name VARCHAR2(128),
  major_classes_code CHAR(1),
  major_classes_name VARCHAR2(128),
  create_date        TIMESTAMP(6)
);
comment on table DIM_BOOK_CATEGORY is '书目分类表';
comment on column DIM_BOOK_CATEGORY.id is '编号';
comment on column DIM_BOOK_CATEGORY.small_classes_code is '22个基本类编号  A-Z';
comment on column DIM_BOOK_CATEGORY.small_classes_name is '22个基本类名称';
comment on column DIM_BOOK_CATEGORY.major_classes_code is '五大部类编号1马列主义、毛泽东思想 2哲学 3社会科学 4自然科学 5综合性图书';
comment on column DIM_BOOK_CATEGORY.major_classes_name is '五大部类名称';
comment on column DIM_BOOK_CATEGORY.create_date is '创建时间';
alter table DIM_BOOK_CATEGORY  add constraint PK_DIM_BOOK_CATEGORY primary key (ID);


create table DIM_BOOK_DIRECTORY
(
  id              VARCHAR2(64) not null,
  isbn            VARCHAR2(64),
  author          VARCHAR2(512),
  title           VARCHAR2(512),
  publishing_name VARCHAR2(512),
  publishing_time VARCHAR2(512),
  price           NUMBER default 0,
  assortnum       VARCHAR2(512)
);
comment on table DIM_BOOK_DIRECTORY is '书目维度表';
comment on column DIM_BOOK_DIRECTORY.id is '编号';
comment on column DIM_BOOK_DIRECTORY.isbn is 'ISBN';
comment on column DIM_BOOK_DIRECTORY.author is '著者';
comment on column DIM_BOOK_DIRECTORY.title is '题名';
comment on column DIM_BOOK_DIRECTORY.publishing_name is '出版社名';
comment on column DIM_BOOK_DIRECTORY.publishing_time is '出版时间';
comment on column DIM_BOOK_DIRECTORY.price is '定价';
comment on column DIM_BOOK_DIRECTORY.assortnum is '分类号';
create index IDX_DIM_DERCTORY_ID on DIM_BOOK_DIRECTORY (ID);
create index IDX_DIM_DERCTORY_UNI on DIM_BOOK_DIRECTORY (ISBN, AUTHOR, TITLE, PUBLISHING_NAME, PUBLISHING_TIME, PRICE);


create table DIM_CUSTOMERS
(
  customer_id       INTEGER not null,
  customer_name     VARCHAR2(34),
  adress            VARCHAR2(42),
  zip_code          VARCHAR2(120),
  city              VARCHAR2(14),
  state             VARCHAR2(13),
  country           VARCHAR2(13),
  continent         VARCHAR2(5),
  contact_lastname  VARCHAR2(11),
  contact_firstname VARCHAR2(11),
  phone_number      VARCHAR2(17)
);

create table DIM_DATE
(
  id          VARCHAR2(64) not null,
  c_date      DATE,
  c_year      VARCHAR2(4),
  c_month     VARCHAR2(2),
  c_quarter   VARCHAR2(2),
  c_week      VARCHAR2(10),
  create_date TIMESTAMP(6)
);
comment on table DIM_DATE is '日期表';
comment on column DIM_DATE.id is '编号';
comment on column DIM_DATE.c_date is '日期';
comment on column DIM_DATE.c_year is '年';
comment on column DIM_DATE.c_month is '月';
comment on column DIM_DATE.c_quarter is '季度';
comment on column DIM_DATE.c_week is '星期';
comment on column DIM_DATE.create_date is '创建时间';
create index INDX_DATE_YEAR on DIM_DATE (C_YEAR);
alter table DIM_DATE  add constraint PK_DATE_ID primary key (ID);


create table DIM_GROUP
(
  id           VARCHAR2(64),
  org_id       VARCHAR2(64),
  group_name   VARCHAR2(64),
  group_type   CHAR(1),
  create_date  DATE,
  invalid_date DATE,
  valid        CHAR(1),
  o_id         VARCHAR2(64)
);
comment on column DIM_GROUP.org_id is '机构id';
comment on column DIM_GROUP.group_name is '组织机构名称';
comment on column DIM_GROUP.group_type is '组织类型';
comment on column DIM_GROUP.create_date is '创建日期';
comment on column DIM_GROUP.invalid_date is '失效日期';
comment on column DIM_GROUP.valid is '状态';
alter table DIM_GROUP  add constraint PK_DIM_GROUP primary key (ID);


create table DIM_OP_TYPE
(
  id          VARCHAR2(64) not null,
  name        VARCHAR2(20),
  create_date DATE,
  log_type    VARCHAR2(64)
);
comment on table DIM_OP_TYPE is '操作类型表';
comment on column DIM_OP_TYPE.id is '编号';
comment on column DIM_OP_TYPE.name is '日期';
comment on column DIM_OP_TYPE.create_date is '创建时间';
alter table DIM_OP_TYPE  add constraint PK_DIM_OP_TYPE primary key (ID);


create table DIM_ORG
(
  id            VARCHAR2(64) not null,
  unit_code     VARCHAR2(64),
  unit_name     NVARCHAR2(100),
  province_code VARCHAR2(64),
  province_name NVARCHAR2(100),
  city_code     NVARCHAR2(64),
  city_name     NVARCHAR2(100),
  county_code   NVARCHAR2(64),
  county_name   NVARCHAR2(100),
  school_type   VARCHAR2(64),
  invalid_date  TIMESTAMP(6),
  valid         CHAR(1),
  type          VARCHAR2(20)
);
comment on table DIM_ORG is '机构表';
comment on column DIM_ORG.id is '编号';
comment on column DIM_ORG.unit_code is '机构编码';
comment on column DIM_ORG.unit_name is '机构名称';
comment on column DIM_ORG.province_code is '省机构编码';
comment on column DIM_ORG.province_name is '省机构名称';
comment on column DIM_ORG.city_code is '市机构编码';
comment on column DIM_ORG.city_name is '市机构名称';
comment on column DIM_ORG.county_code is '区县机构编码';
comment on column DIM_ORG.county_name is '区县机构名称';
comment on column DIM_ORG.school_type is '学校类别';
comment on column DIM_ORG.invalid_date is '失效时间';
comment on column DIM_ORG.valid is '状态  0有效 1失效';
comment on column DIM_ORG.type is '机构类型    0-省级机构，1-市级机构，2-区县级机构，3-学区，4-学校';
create index IDX_ORG_UNIT_CODE on DIM_ORG (UNIT_CODE);
create index IND_ORG_ORG_ID on DIM_ORG (ID);

create table DIM_PRODUCTS
(
  product_code         VARCHAR2(9) not null,
  product_type         VARCHAR2(16),
  prix_vente_conseille INTEGER
);

create table DIM_READER
(
  id           VARCHAR2(64),
  org_id       VARCHAR2(64),
  group_id     VARCHAR2(64),
  o_reader_id  VARCHAR2(64),
  name         VARCHAR2(50),
  email        VARCHAR2(50),
  phone        VARCHAR2(50),
  cert_type    VARCHAR2(64),
  cert_num     VARCHAR2(50),
  reader_type  CHAR(1) not null,
  create_date  DATE,
  invalid_date DATE,
  valid        CHAR(1)
);
comment on table DIM_READER is '读者维度表';
comment on column DIM_READER.id is '编号';
comment on column DIM_READER.org_id is '机构维度id';
comment on column DIM_READER.group_id is '组织 维度表id';
comment on column DIM_READER.o_reader_id is '读者原始ID';
comment on column DIM_READER.name is '读者姓名';
comment on column DIM_READER.email is '读者邮箱';
comment on column DIM_READER.phone is '读者手机号码';
comment on column DIM_READER.cert_type is '读者证件类型 1.学生证 2.身份证  3.学籍证  ';
comment on column DIM_READER.cert_num is '读者证件号码';
comment on column DIM_READER.reader_type is '读者类型';
comment on column DIM_READER.invalid_date is '日期id';
comment on column DIM_READER.valid is '状态（0：有效，1：失效）';
create index INX_DIM_READER_OID on DIM_READER (O_READER_ID);
create index INX_DIM_READER_ORG on DIM_READER (ORG_ID);
create index INX_DIM_READER_TYPE on DIM_READER (READER_TYPE);
alter table DIM_READER  add constraint PK_DIM_READER primary key (ID);


create table FACT_CIRCULATE
(
  id            VARCHAR2(64) not null,
  org_id        VARCHAR2(64),
  group_id      VARCHAR2(64),
  date_id       VARCHAR2(64),
  dim_reader_id VARCHAR2(64),
  op_type_id    VARCHAR2(64),
  bar_code      VARCHAR2(50),
  directory_id  VARCHAR2(64),
  op_log_id     VARCHAR2(64)
);
comment on table FACT_CIRCULATE is '流通表';
comment on column FACT_CIRCULATE.id is '编号';
comment on column FACT_CIRCULATE.org_id is '机构id(dim_org表id)';
comment on column FACT_CIRCULATE.group_id is '组织id';
comment on column FACT_CIRCULATE.date_id is '日期id(dim_date表id)';
comment on column FACT_CIRCULATE.dim_reader_id is '读者维度id';
comment on column FACT_CIRCULATE.op_type_id is '操作类型id(dim_op_type表id)';
comment on column FACT_CIRCULATE.bar_code is '条形码';
comment on column FACT_CIRCULATE.directory_id is '原表书目id';
alter table FACT_CIRCULATE add constraint PK_FACT_CIRCULATE primary key (ID);


create table FACT_COPY
(
  id           VARCHAR2(64) not null,
  org_id       VARCHAR2(64),
  category_id  VARCHAR2(64),
  directory_id VARCHAR2(64),
  in_date_id   VARCHAR2(64),
  out_date_id  VARCHAR2(64),
  bar_code     VARCHAR2(64),
  valid        CHAR(1),
  o_id         VARCHAR2(64)
);
comment on table FACT_COPY is '馆藏复本';
comment on column FACT_COPY.id is '编号';
comment on column FACT_COPY.org_id is '机构id(dim_org表id)';
comment on column FACT_COPY.category_id is '分类id(dim_book_category表id)';
comment on column FACT_COPY.directory_id is '书目ID';
comment on column FACT_COPY.in_date_id is '日期id(dim_date表id)';
comment on column FACT_COPY.out_date_id is '报废时间';
comment on column FACT_COPY.bar_code is '条形码';
comment on column FACT_COPY.valid is '状态';
create index IDX_COPY_OID on FACT_COPY (O_ID);
create index IDX_COPY_ORG_DEREC on FACT_COPY (ORG_ID, DIRECTORY_ID);
create index IDX_COPY_VALIDID on FACT_COPY (VALID);
create index INX_COPY_DATE on FACT_COPY (IN_DATE_ID, OUT_DATE_ID);
alter table FACT_COPY  add constraint PK_FACT_COPY primary key (ID);


create table FACT_SALES
(
  date_id      INTEGER,
  customer_id  INTEGER,
  product_code VARCHAR2(9),
  status       VARCHAR2(10),
  unit_price   BINARY_DOUBLE,
  retail_price NUMBER(20),
  quantity     INTEGER,
  amount       BINARY_DOUBLE
);

create table INFO_BOOK_DIRECTORY
(
  id                 VARCHAR2(64) not null,
  org_id             VARCHAR2(64),
  isbn               VARCHAR2(64),
  title              VARCHAR2(512),
  sub_title          VARCHAR2(512),
  tied_title         VARCHAR2(512),
  part_name          VARCHAR2(512),
  part_num           VARCHAR2(512),
  series_name        VARCHAR2(512),
  author             VARCHAR2(512),
  sub_author         VARCHAR2(512),
  series_editor      VARCHAR2(512),
  translator         VARCHAR2(512),
  publishing_name    VARCHAR2(512),
  publishing_address VARCHAR2(512),
  publishing_time    VARCHAR2(512),
  librarsort_id      VARCHAR2(512),
  librarsort_code    VARCHAR2(512),
  price              NUMBER,
  edition            VARCHAR2(512),
  language           VARCHAR2(512),
  measure            VARCHAR2(512),
  page_no            VARCHAR2(512),
  binding_form       VARCHAR2(512),
  best_age           VARCHAR2(512),
  attachment_note    VARCHAR2(512),
  subject            VARCHAR2(512),
  content            CLOB,
  create_by          VARCHAR2(64),
  create_date        TIMESTAMP(6),
  update_by          VARCHAR2(64),
  update_date        TIMESTAMP(6),
  remarks            NVARCHAR2(256),
  del_flag           CHAR(1),
  taneji_no          VARCHAR2(64),
  ass_no             VARCHAR2(64),
  purpose            VARCHAR2(64),
  marc64             CLOB
);
alter table INFO_BOOK_DIRECTORY  add constraint PK_INFO_BOOK_DIRECTORY primary key (ID);

create table INFO_COPY
(
  id                 VARCHAR2(64) not null,
  org_id             VARCHAR2(64),
  book_directory_id  VARCHAR2(64),
  collection_site_id VARCHAR2(64),
  bar_code           VARCHAR2(50),
  batch_no           VARCHAR2(20),
  status             CHAR(1),
  is_renew           CHAR(1),
  is_stained         CHAR(1),
  place              VARCHAR2(50),
  is_order           CHAR(1),
  create_date        TIMESTAMP(6),
  update_date        TIMESTAMP(6)
);
comment on table INFO_COPY is '馆藏复本表';
comment on column INFO_COPY.id is '主键id';
comment on column INFO_COPY.org_id is '机构id';
comment on column INFO_COPY.book_directory_id is '书目id';
comment on column INFO_COPY.collection_site_id is '馆藏地点id';
comment on column INFO_COPY.bar_code is '条形码';
comment on column INFO_COPY.batch_no is '批次号';
comment on column INFO_COPY.status is '状态   0 在馆 1借出 2剔旧 3报废 4丢失  5预借';
comment on column INFO_COPY.is_renew is '是否续借 0否 1是';
comment on column INFO_COPY.is_stained is '是否污损 0否 1是';
comment on column INFO_COPY.place is '存放位置';
comment on column INFO_COPY.is_order is '是否预约 0否 1是';
comment on column INFO_COPY.create_date is '创建时间';
comment on column INFO_COPY.update_date is '更新时间';
create index INDX_INFO_COPY on INFO_COPY (ORG_ID);
alter table INFO_COPY  add constraint PK_INFO_COPY primary key (ID);


create table LIB_WAITING_HANDLE
(
  id                   VARCHAR2(64),
  name                 VARCHAR2(100),
  theme_describe       VARCHAR2(200),
  other_theme_describe VARCHAR2(100),
  waiting_type         CHAR(1),
  user_type            CHAR(1),
  deal_data            CLOB,
  create_by            VARCHAR2(64),
  create_date          TIMESTAMP(6),
  update_by            VARCHAR2(64),
  update_date          TIMESTAMP(6),
  remarks              NVARCHAR2(255),
  del_flag             CHAR(1)
);
comment on table LIB_WAITING_HANDLE is '待办表';
comment on column LIB_WAITING_HANDLE.id is '编号';
comment on column LIB_WAITING_HANDLE.name is '待办名称';
comment on column LIB_WAITING_HANDLE.theme_describe is '主题描述';
comment on column LIB_WAITING_HANDLE.other_theme_describe is '主题副描述';
comment on column LIB_WAITING_HANDLE.waiting_type is '类型  0公告通知   1待办事项';
comment on column LIB_WAITING_HANDLE.user_type is '适用应用对象   0单管   1读者';
comment on column LIB_WAITING_HANDLE.deal_data is '待办数据';
comment on column LIB_WAITING_HANDLE.create_by is '创建人';
comment on column LIB_WAITING_HANDLE.create_date is '创建时间';
comment on column LIB_WAITING_HANDLE.update_by is '更新人';
comment on column LIB_WAITING_HANDLE.update_date is '更新时间';
comment on column LIB_WAITING_HANDLE.remarks is '备注';
comment on column LIB_WAITING_HANDLE.del_flag is '删除标示 0未删除 1已删除';
alter table LIB_WAITING_HANDLE  add constraint PK_LIB_WAITING_HANDLE primary key (ID);

--rollback DROP TABLE DIM_BOOK_CATEGORY;
--rollback DROP TABLE DIM_BOOK_DIRECTORY;
--rollback DROP TABLE DIM_CUSTOMERS;
--rollback DROP TABLE DIM_DATE;
--rollback DROP TABLE DIM_GROUP;
--rollback DROP TABLE DIM_OP_TYPE;
--rollback DROP TABLE DIM_ORG;
--rollback DROP TABLE DIM_PRODUCTS;
--rollback DROP TABLE DIM_READER;
--rollback DROP TABLE FACT_CIRCULATE;
--rollback DROP TABLE FACT_COPY;
--rollback DROP TABLE FACT_SALES;
--rollback DROP TABLE INFO_BOOK_DIRECTORY;
--rollback DROP TABLE INFO_COPY;
--rollback DROP TABLE LIB_WAITING_HANDLE;
