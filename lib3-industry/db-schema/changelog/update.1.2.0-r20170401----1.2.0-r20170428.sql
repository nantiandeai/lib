--liquibase formatted sql
--changeset lianyitech:3

alter table DIM_ORG add (MASTER NVARCHAR2(100), PHONE NVARCHAR2(100) );
comment on column DIM_ORG.MASTER is '联系人';
comment on column DIM_ORG.PHONE is '联系人电话';

create table DIM_PERI_DIRECTORY
(
  id              VARCHAR2(64) primary key not null,
  issn            VARCHAR2(64),
  author          VARCHAR2(512),
  title           VARCHAR2(512),
  publishing_name VARCHAR2(512),
  publishing_fre  CHAR(1),
  price           NUMBER default 0,
  assortnum       VARCHAR2(512)
);
comment on table DIM_PERI_DIRECTORY is '期刊书目维度表';
comment on column DIM_PERI_DIRECTORY.id is '编号';
comment on column DIM_PERI_DIRECTORY.issn is 'issn';
comment on column DIM_PERI_DIRECTORY.author is '著者';
comment on column DIM_PERI_DIRECTORY.title is '刊名';
comment on column DIM_PERI_DIRECTORY.publishing_name is '出版社名';
comment on column DIM_PERI_DIRECTORY.publishing_fre  is '出版频率(0:日刊;1:周刊;2:旬刊;3:半月刊;4:月刊;5:双月刊;6:季刊;7:半年刊;8:年刊;)';
comment on column DIM_PERI_DIRECTORY.price is '定价';
comment on column DIM_PERI_DIRECTORY.assortnum is '分类号';
create index IDX_DIM_PERI_DERCTORY_UNI on DIM_PERI_DIRECTORY (issn, author, title, publishing_name, publishing_fre, price, assortnum);

create table FACT_PERI_COPY
(
  id           VARCHAR2(64) primary key not null,
  org_id       VARCHAR2(64),
  category_id  VARCHAR2(64),
  directory_id VARCHAR2(64),
  in_date_id   VARCHAR2(64),
  out_date_id  VARCHAR2(64),
  bar_code     VARCHAR2(64),
  price        NUMBER default 0,
  valid        CHAR(1),
  o_id         VARCHAR2(64)
);
comment on table FACT_PERI_COPY is '期刊表';
comment on column FACT_PERI_COPY.id is '编号';
comment on column FACT_PERI_COPY.org_id is '机构id(dim_org表id)';
comment on column FACT_PERI_COPY.category_id is '分类id(dim_book_category表id)';
comment on column FACT_PERI_COPY.directory_id is '期刊书目id(dim_peri_directory表id)';
comment on column FACT_PERI_COPY.in_date_id is '入库日期id(dim_date表id)';
comment on column FACT_PERI_COPY.out_date_id is '报废时间id(dim_date表id)';
comment on column FACT_PERI_COPY.bar_code is '条形码';
comment on column FACT_PERI_COPY.price is '定价';
comment on column FACT_PERI_COPY.valid is '状态';
comment on column FACT_PERI_COPY.o_id is '原期刊ID';
create index IDX_PERI_COPY_OID on FACT_PERI_COPY (o_id);
create index IDX_PERI_COPY_ORG_DEREC on FACT_PERI_COPY (org_id, directory_id);
create index IDX_PERI_COPY_VALIDID on FACT_PERI_COPY (valid);
create index INX_PERI_COPY_DATE on FACT_PERI_COPY (in_date_id, out_date_id);

create materialized view log on fact_peri_copy
with rowid, sequence (org_id,in_date_id,out_date_id,directory_id,category_id,valid,price)
including new values;

create materialized view log on dim_peri_directory
with rowid, sequence (id,assortnum)
including new values;

create materialized view MV_FACT_PERI_COPY_ASSORT
refresh force on commit
as
  select cop.org_id,
    cop.valid,
    ind.c_year indy,
    ind.c_month indm,
    ind.c_quarter indq,
    outd.c_year outdy,
    outd.c_month outdm,
    outd.c_quarter outdq,
    count(*) copys,
    count(cop.price) pcount,
    sum(cop.price) price,
    book.assortnum,
    cat.id catid
  from fact_peri_copy cop, dim_date ind, dim_date outd, dim_peri_directory book, dim_book_category cat
  where cop.in_date_id = ind.id
        and cop.out_date_id = outd.id(+)
        and book.id = cop.directory_id
        and cop.category_id = cat.id
  group by cop.org_id,
    cop.valid,
    ind.c_year,
    ind.c_month,
    ind.c_quarter,
    outd.c_year,
    outd.c_month,
    outd.c_quarter,
    book.assortnum,
    cat.id;


--rollback ALTER TABLE DIM_ORG DROP COLUMN MASTER;
--rollback ALTER TABLE DIM_ORG DROP COLUMN PHONE;
--rollback drop materialized view log on fact_peri_copy;
--rollback drop materialized view log on dim_peri_directory;
--rollback DROP TABLE FACT_PERI_COPY;
--rollback DROP TABLE DIM_PERI_DIRECTORY;
--rollback drop materialized view MV_FACT_PERI_COPY_ASSORT;