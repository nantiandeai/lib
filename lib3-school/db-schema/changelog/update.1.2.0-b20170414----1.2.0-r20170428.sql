--liquibase formatted sql
--changeset lianyitech:3

alter table circulate_card add (NEW_CARD varchar2(64)) ;

--rollback ALTER table circulate_card drop column NEW_CARD;