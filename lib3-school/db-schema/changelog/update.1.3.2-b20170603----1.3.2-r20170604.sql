--liquibase formatted sql
--changeset lianyitech:6

alter table CIRCULATE_LOG add ORG_ID VARCHAR2(64);
comment on column CIRCULATE_LOG.ORG_ID is '机构id';

MERGE INTO circulate_log T1
USING (select t.id, t.org_id from circulate_bill t) T2
ON (T1.Bill_Id = T2.Id)
WHEN MATCHED THEN
UPDATE SET T1.Org_Id = T2.Org_Id;

alter table CIRCULATE_CARD add ORG_ID VARCHAR2(64);
comment on column CIRCULATE_CARD.ORG_ID is '机构id';

MERGE INTO circulate_card T1
USING (select t.id, t.org_id from circulate_reader t) T2
ON (T1.Reader_Id = T2.Id)
WHEN MATCHED THEN
UPDATE SET T1.Org_Id = T2.Org_Id;

--rollback ALTER table CIRCULATE_LOG drop column ORG_ID;
--rollback ALTER table CIRCULATE_CARD drop column ORG_ID;
