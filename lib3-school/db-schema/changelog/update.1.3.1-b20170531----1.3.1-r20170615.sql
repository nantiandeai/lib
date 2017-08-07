--liquibase formatted sql
--changeset lianyitech:5

alter table SYS_COLLECTION_SITE add (STATUS CHAR(1)) ;

alter table SYS_COLLECTION_SITE modify STATUS default '1';

--rollback ALTER table SYS_COLLECTION_SITE drop column STATUS;