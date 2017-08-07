--liquibase formatted sql
--changeset lianyitech:6

create table SYS_ETL_DATE
(
        id         VARCHAR2(64) primary key not null,
        begin_date TIMESTAMP(6),
        end_date   TIMESTAMP(6),
        remarks    NVARCHAR2(255),
        del_flag   CHAR(1) default '0'
);

--rollback drop table SYS_ETL_DATE;