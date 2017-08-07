--liquibase formatted sql
--changeset lianyitech:4

alter table CIRCULATE_BILL modify(title VARCHAR2(512));

--rollback alter table CIRCULATE_BILL modify(title VARCHAR2(100));