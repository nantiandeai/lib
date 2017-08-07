--liquibase formatted sql
--changeset lianyitech:7

alter table CIRCULATE_READER add IMG VARCHAR2(128);
comment on column CIRCULATE_READER.IMG
is '头像';

alter table CIRCULATE_READER add SEX CHAR(1);
comment on column CIRCULATE_READER.SEX
is '性别 0男 1女';

alter table CIRCULATE_BILL add SUB_TITLE VARCHAR2(512);
comment on column CIRCULATE_BILL.SUB_TITLE
is '副题名';

alter table CIRCULATE_BILL add DIR_TYPE CHAR(1);
comment on column CIRCULATE_BILL.DIR_TYPE
is '书目类型 0图书 1期刊';

alter table PERI_BINDING add IS_RENEW CHAR(1);
comment on column PERI_BINDING.IS_RENEW
is '是否续借 0否 1是';

alter table PERI_BINDING add IS_STAINED CHAR(1);
comment on column PERI_BINDING.IS_STAINED
is '是否污损 0否 1是';

alter table PERI_BINDING add IS_ORDER CHAR(1);
comment on column PERI_BINDING.IS_ORDER
is '是否预约 0否 1是';

alter table PERI_ORDER_DETAIL add SEQ NUMBER;
comment on column PERI_ORDER_DETAIL.SEQ
is '序号';

update PERI_BINDING set STATUS='0' where STATUS is null;
commit;

update CIRCULATE_BILL set DIR_TYPE='0' where DIR_TYPE is null;
commit;

--rollback ALTER table CIRCULATE_READER drop column IMG;
--rollback ALTER table CIRCULATE_READER drop column SEX;
--rollback ALTER table CIRCULATE_BILL drop column DIR_TYPE;
--rollback ALTER table CIRCULATE_BILL drop column SUB_TITLE;
--rollback ALTER table PERI_BINDING drop column IS_RENEW;
--rollback ALTER table PERI_BINDING drop column IS_STAINED;
--rollback ALTER table PERI_BINDING drop column IS_ORDER;
--rollback ALTER table PERI_ORDER_DETAIL drop column SEQ;