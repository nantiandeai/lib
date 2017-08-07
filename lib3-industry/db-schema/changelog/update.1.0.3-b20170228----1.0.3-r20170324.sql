--liquibase formatted sql
--changeset lianyitech:2

alter table DIM_ORG add (SCHOOL_AREA_CODE NVARCHAR2(64), SCHOOL_AREA_NAME NVARCHAR2(100) );
comment on column DIM_ORG.SCHOOL_AREA_CODE is '学区机构编码';
comment on column DIM_ORG.SCHOOL_AREA_NAME is '学区机构名称';

create materialized view log on fact_copy
with rowid, sequence (org_id,in_date_id,out_date_id,directory_id,category_id,valid)
including new values;

create materialized view log on dim_date
with rowid, sequence (id,c_year,c_month,c_quarter)
including new values;

create materialized view log on dim_book_directory
with rowid, sequence (id,assortnum,price)
including new values;

create materialized view log on dim_book_category
with rowid, sequence (id) including new values;

create materialized view mv_fact_copy_assort refresh force on commit as
  select cop.org_id,
    cop.valid,
    ind.c_year indy,
    ind.c_month indm,
    ind.c_quarter indq,
    outd.c_year outdy,
    outd.c_month outdm,
    outd.c_quarter outdq,
    count(*) copys,
    count(book.price) pcount,
    sum(book.price) price,
    book.assortnum,
    cat.id catid
  from fact_copy cop, dim_date ind, dim_date outd, dim_book_directory book, dim_book_category cat
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

--rollback ALTER TABLE DIM_ORG DROP COLUMN SCHOOL_AREA_CODE;
--rollback ALTER TABLE DIM_ORG DROP COLUMN SCHOOL_AREA_NAME;
--rollback drop materialized view log on fact_copy;
--rollback drop materialized view log on dim_date;
--rollback drop materialized view log on dim_book_directory;
--rollback drop materialized view log on dim_book_category;
--rollback drop materialized view mv_fact_copy_assort;