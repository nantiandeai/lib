--liquibase formatted sql
--changeset lianyitech:5

create or replace procedure p_reflush_mv(Num Out Number)
as
begin
        dbms_mview.refresh('MV_FACT_PERI_COPY_ASSORT');
        dbms_mview.refresh('MV_FACT_COPY_ASSORT');
        Num := 0;
end;

--rollback drop procedure p_reflush_mv;