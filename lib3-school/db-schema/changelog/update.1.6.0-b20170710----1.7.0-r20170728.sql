--liquibase formatted sql
--changeset lianyitech:8

CREATE TABLE CIRCULATE_DEPOSIT_RECORD
(
  ID VARCHAR2(64) PRIMARY KEY NOT NULL,
  READER_ID VARCHAR2(64),
  READER_NAME VARCHAR2(64),
  READER_CARD VARCHAR2(64),
  AMOUNT  NUMBER,
  OP_TYPE CHAR(1),
  ORG_ID VARCHAR2(64),
  CREATE_BY VARCHAR2(64),
  CREATE_DATE TIMESTAMP(6)
);
COMMENT ON TABLE CIRCULATE_DEPOSIT_RECORD IS '读者押金记录表';
COMMENT ON COLUMN CIRCULATE_DEPOSIT_RECORD.ID IS '主键id';
COMMENT ON COLUMN CIRCULATE_DEPOSIT_RECORD.READER_ID IS '读者id';
COMMENT ON COLUMN CIRCULATE_DEPOSIT_RECORD.READER_NAME IS '读者姓名';
COMMENT ON COLUMN CIRCULATE_DEPOSIT_RECORD.READER_CARD IS '读者证号';
COMMENT ON COLUMN CIRCULATE_DEPOSIT_RECORD.AMOUNT IS '金额';
COMMENT ON COLUMN CIRCULATE_DEPOSIT_RECORD.OP_TYPE IS '操作类型 0交押金 1退押金 2旧证转移';
COMMENT ON COLUMN CIRCULATE_DEPOSIT_RECORD.ORG_ID IS '机构id';
COMMENT ON COLUMN CIRCULATE_DEPOSIT_RECORD.CREATE_BY IS '创建人';
COMMENT ON COLUMN CIRCULATE_DEPOSIT_RECORD.CREATE_DATE IS '创建时间';


CREATE TABLE CIRCULATE_COMPEN_RECORD
(
  ID VARCHAR2(64) PRIMARY KEY NOT NULL,
  READER_ID VARCHAR2(64),
  READER_NAME VARCHAR2(64),
  READER_CARD VARCHAR2(64),
  READER_GROUP VARCHAR2(64),
  BAR_CODE VARCHAR2(64),
  TITLE VARCHAR2(512),
  OP_TYPE CHAR(1),
  COMPEN_TYPE CHAR(1),
  AMOUNT  NUMBER,
  NEW_BAR_CODE  VARCHAR2(64),
  ORG_ID VARCHAR2(64),
  CREATE_BY VARCHAR2(64),
  CREATE_DATE TIMESTAMP(6),
  UPDATE_BY VARCHAR2(64),
  UPDATE_DATE TIMESTAMP(6),
  remarks VARCHAR2(512)
);
COMMENT ON TABLE CIRCULATE_COMPEN_RECORD IS '赔罚记录表';
COMMENT ON COLUMN CIRCULATE_COMPEN_RECORD.ID IS '主键id';
COMMENT ON COLUMN CIRCULATE_COMPEN_RECORD.READER_ID IS '读者id';
COMMENT ON COLUMN CIRCULATE_COMPEN_RECORD.READER_NAME IS '读者姓名';
COMMENT ON COLUMN CIRCULATE_COMPEN_RECORD.READER_CARD IS '读者证号';
COMMENT ON COLUMN CIRCULATE_COMPEN_RECORD.READER_GROUP IS '读者组织';
COMMENT ON COLUMN CIRCULATE_COMPEN_RECORD.BAR_CODE IS '条形码';
COMMENT ON COLUMN CIRCULATE_COMPEN_RECORD.TITLE IS '题名';
COMMENT ON COLUMN CIRCULATE_COMPEN_RECORD.OP_TYPE IS '操作类型  0丢失 1污损 2超期';
COMMENT ON COLUMN CIRCULATE_COMPEN_RECORD.COMPEN_TYPE IS '赔偿类型 0赔书 1 罚款';
COMMENT ON COLUMN CIRCULATE_COMPEN_RECORD.AMOUNT IS '罚款金额';
COMMENT ON COLUMN CIRCULATE_COMPEN_RECORD.NEW_BAR_CODE IS '新条形码';
COMMENT ON COLUMN CIRCULATE_COMPEN_RECORD.ORG_ID IS '机构id';
COMMENT ON COLUMN CIRCULATE_COMPEN_RECORD.CREATE_BY IS '创建人';
COMMENT ON COLUMN CIRCULATE_COMPEN_RECORD.CREATE_DATE IS '创建时间';
COMMENT ON COLUMN CIRCULATE_COMPEN_RECORD.UPDATE_BY IS '更新人';
COMMENT ON COLUMN CIRCULATE_COMPEN_RECORD.UPDATE_DATE IS '更新时间';


alter table CIRCULATE_CARD add DEPOSIT NUMBER DEFAULT 0;
comment on column CIRCULATE_CARD.DEPOSIT
is '押金';

alter table CATALOG_COPY add ASS_NO VARCHAR2(64);
comment on column CATALOG_COPY.ASS_NO
is '辅助区分号';

alter table CIRCULATE_RULE add SHORT_BORROW_DAYS NUMBER;
comment on column CIRCULATE_RULE.SHORT_BORROW_DAYS
is '最短借阅天数';
alter table CIRCULATE_RULE add EXCEED_FINE CHAR(1);
comment on column CIRCULATE_RULE.EXCEED_FINE
is '超期是否罚款 0否 1是';
alter table CIRCULATE_RULE add EXCEED_FINE_DAY_AMOUNT NUMBER;
comment on column CIRCULATE_RULE.EXCEED_FINE_DAY_AMOUNT
is '超期每天罚金';
alter table CIRCULATE_RULE add EXCEED_FINE_MAX_AMOUNT NUMBER;
comment on column CIRCULATE_RULE.EXCEED_FINE_MAX_AMOUNT
is '超期最大罚金';
alter table CIRCULATE_RULE add EXCEED_AUTO_STOP_BORROW CHAR(1);
comment on column CIRCULATE_RULE.EXCEED_AUTO_STOP_BORROW
is '超期是否自动停借 0否 1是';
alter table CIRCULATE_RULE add EXCEED_AUTO_STOP_DAYS NUMBER;
comment on column CIRCULATE_RULE.EXCEED_AUTO_STOP_DAYS
is '超期自动停借天数';
alter table CIRCULATE_RULE add LOSS_FINE CHAR(1);
comment on column CIRCULATE_RULE.LOSS_FINE
is '丢书是否罚款 0否 1是';
alter table CIRCULATE_RULE add LOSS_FINE_MULTI NUMBER;
comment on column CIRCULATE_RULE.LOSS_FINE_MULTI
is '丢书罚款倍率';
alter table CIRCULATE_RULE add STAIN_FINE CHAR(1);
comment on column CIRCULATE_RULE.STAIN_FINE
is '污损是否罚款 0否 1是';
alter table CIRCULATE_RULE add STAIN_FINE_MULTI NUMBER;
comment on column CIRCULATE_RULE.STAIN_FINE_MULTI
is '污损罚款倍率';


update circulate_rule set short_borrow_days=0 where short_borrow_days is null;
update circulate_rule set exceed_fine='0' where exceed_fine is null;
update circulate_rule set exceed_fine_day_amount=0 where exceed_fine_day_amount is null;
update circulate_rule set exceed_fine_max_amount=0 where exceed_fine_max_amount is null;
update circulate_rule set exceed_auto_stop_borrow='0' where exceed_auto_stop_borrow is null;
update circulate_rule set exceed_auto_stop_days=0 where exceed_auto_stop_days is null;
update circulate_rule set loss_fine='0' where loss_fine is null;
update circulate_rule set loss_fine_multi=0 where loss_fine_multi is null;
update circulate_rule set stain_fine='0' where stain_fine is null;
update circulate_rule set stain_fine_multi=0 where stain_fine_multi is null;

comment on column CATALOG_COPY.status is '0 在馆 1借出 2剔旧 3报废 4丢失  5预借 6污损';
comment on column CATALOG_COPY.is_stained is '是否污损 0否 1是  (污损变成了状态此标识暂时没用)';
comment on column CIRCULATE_BILL.status is '状态  10 归还成功  01 借阅成功  24 丢失成功  65 预借成功 32 剔旧成功 43 报废成功 51 预约成功 91 取消预约成功 71 续借成功 86 污损成功 100 取消预借成功15预约自动转预借成功';
comment on column CIRCULATE_CARD.status
is '状态 0有效 1无效2.挂失3.过期 4.旧证  5.停借（3.过期在数据库里面不会存在数据只是在程序里面需要用的，通过办证终止日期跟当前日期来判断，4.旧证目前系统不允许查询出来为以后预留）';


MERGE INTO CATALOG_COPY T1
USING (select b.id,b.ass_no,b.org_id
       from CATALOG_BOOK_DIRECTORY b
       where  b.del_flag = '0') T2
ON (T1.book_directory_id = T2.id and t1.org_id = t2.org_id and t1.del_flag='0')
WHEN MATCHED THEN
UPDATE SET T1.ass_no = T2.ass_no;

--rollback DROP table CIRCULATE_DEPOSIT_RECORD;
--rollback DROP table CIRCULATE_COMPEN_RECORD;
--rollback ALTER table CIRCULATE_CARD drop column DEPOSIT;
--rollback ALTER table CATALOG_COPY drop column ASS_NO;
--rollback ALTER table CIRCULATE_RULE drop column SHORT_BORROW_DAYS;
--rollback ALTER table CIRCULATE_RULE drop column EXCEED_FINE;
--rollback ALTER table CIRCULATE_RULE drop column EXCEED_FINE_DAY_AMOUNT;
--rollback ALTER table CIRCULATE_RULE drop column EXCEED_FINE_MAX_AMOUNT;
--rollback ALTER table CIRCULATE_RULE drop column EXCEED_AUTO_STOP_BORROW;
--rollback ALTER table CIRCULATE_RULE drop column EXCEED_AUTO_STOP_DAYS;
--rollback ALTER table CIRCULATE_RULE drop column LOSS_FINE;
--rollback ALTER table CIRCULATE_RULE drop column LOSS_FINE_MULTI;
--rollback ALTER table CIRCULATE_RULE drop column STAIN_FINE;
--rollback ALTER table CIRCULATE_RULE drop column STAIN_FINE_MULTI;
