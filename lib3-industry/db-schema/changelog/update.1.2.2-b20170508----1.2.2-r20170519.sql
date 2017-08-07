--liquibase formatted sql
--changeset lianyitech:4

drop materialized view mv_fact_copy_assort;

create materialized view mv_fact_copy_assort
refresh force on demand
start with SYSDATE
next trunc(SYSDATE+1)+4/24
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

drop materialized view MV_FACT_PERI_COPY_ASSORT;

create materialized view MV_FACT_PERI_COPY_ASSORT
refresh force on demand
start with SYSDATE
next trunc(SYSDATE+1)+4/24
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

--rollback alter table CIRCULATE_BILL modify(title VARCHAR2(100));
--rollback drop materialized view mv_fact_copy_assort;
--rollback drop materialized view MV_FACT_PERI_COPY_ASSORT;