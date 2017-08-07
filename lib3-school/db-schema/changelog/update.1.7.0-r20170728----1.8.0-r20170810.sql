--liquibase formatted sql
--changeset lianyitech:9

comment on column CIRCULATE_READER.READER_TYPE is '读者类型  0.学生 1.老师 2.其他 3.集体';
insert into CIRCULATE_RULE (ID, READER_TYPE, RULE_NAME, BORROW_DAYS, BORROW_NUMBER, RENEW_DAYS, RENEW_NUMBER, BESPEAKING_DAYS, BESPEAKING_NUMBER, BESPOKE_DAYS, BESPOKE_NUMBER, EXCEED_LIMIT, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG, ORG_ID, SHORT_BORROW_DAYS, EXCEED_FINE, EXCEED_FINE_DAY_AMOUNT, EXCEED_FINE_MAX_AMOUNT, EXCEED_AUTO_STOP_BORROW, EXCEED_AUTO_STOP_DAYS, LOSS_FINE, LOSS_FINE_MULTI, STAIN_FINE, STAIN_FINE_MULTI)
values ('3', '3', '集体借阅规则', 30, 200, 30, 1, 7, 5, 7, 5, '1', null, '28-7月 -17 06.52.43.000000 上午', null, null, null, '0', null, 0, '0', 0, 0, '0', 0, '0', 0, '0', 0);
alter table CATALOG_COPY modify BATCH_NO VARCHAR2(100);
alter table CATALOG_BATCH modify BATCH_NO VARCHAR2(100);
alter table PERI_ORDER modify BATCH_NO VARCHAR2(100);
alter table SERVER_CIRCULATE_LOG add dir_type char(1) default '0';
comment on column SERVER_CIRCULATE_LOG.dir_type  is '流通类型';
update server_circulate_log t set t.dir_type = '0' where t.dir_type is null;

-- Create table
create table SYS_CARD_PRINT_CONFIG
(
  id             VARCHAR2(64) not null,
  org_id         VARCHAR2(64),
  comp_name      VARCHAR2(200),
  card_name      VARCHAR2(100),
  comp_font      VARCHAR2(20),
  comp_font_size NUMBER(5),
  card_font      VARCHAR2(20),
  card_font_size NUMBER(5),
  school_badge   VARCHAR2(100),
  print_image    CHAR(1),
  CREATE_BY VARCHAR2(64),
  CREATE_DATE TIMESTAMP(6),
  UPDATE_BY VARCHAR2(64),
  UPDATE_DATE TIMESTAMP(6)
);
-- Add comments to the table
comment on table SYS_CARD_PRINT_CONFIG
  is '读者证打印设置';
-- Add comments to the columns
comment on column SYS_CARD_PRINT_CONFIG.comp_name
  is '单位名称';
comment on column SYS_CARD_PRINT_CONFIG.card_name
  is '证件名称';
comment on column SYS_CARD_PRINT_CONFIG.comp_font
  is '单位名称-字体';
comment on column SYS_CARD_PRINT_CONFIG.comp_font_size
  is '单位名称-字体大小';
comment on column SYS_CARD_PRINT_CONFIG.card_font
  is '读者证-字体';
comment on column SYS_CARD_PRINT_CONFIG.card_font_size
  is '读者证-字体大小';
comment on column SYS_CARD_PRINT_CONFIG.school_badge
  is '校徽图片url';
comment on column SYS_CARD_PRINT_CONFIG.print_image
  is '是否打印读者证照片';
-- Create/Recreate primary, unique and foreign key constraints
alter table SYS_CARD_PRINT_CONFIG
  add constraint PK_SYS_CARD_PRINT_CONFIG primary key (ID);
create table catalog_supply_card
(
  id          varchar2(64),
  org_id      varchar2(64),
  card        VARCHAR2(64),
  NAME        VARCHAR2(64),
  group_id    varchar2(64),
  group_name  varchar2(64),
  sex         char(1),
  status      char(1),
  CREATE_BY   VARCHAR2(64),
  CREATE_DATE TIMESTAMP(6),
  UPDATE_BY   VARCHAR2(64),
  UPDATE_DATE TIMESTAMP(6),
  REMARKS     NVARCHAR2(255),
  DEL_FLAG    CHAR(1)
)
;
comment on table catalog_supply_card
  is '读者证补缺';
comment on column catalog_supply_card.id
  is '主键id';
comment on column catalog_supply_card.org_id
  is '机构id';
comment on column catalog_supply_card.card
  is '读者证';
comment on column catalog_supply_card.NAME
  is '读者姓名';
comment on column catalog_supply_card.group_id
  is '读者组织id';
comment on column catalog_supply_card.group_name
  is '读者组织名称';
comment on column catalog_supply_card.sex
  is '读者性别';
comment on column catalog_supply_card.status
  is '读者证状态';
comment on column catalog_supply_card.CREATE_BY
  is '创建人';
comment on column catalog_supply_card.CREATE_DATE
  is '创建时间';
comment on column catalog_supply_card.UPDATE_BY
  is '更新人';
comment on column catalog_supply_card.UPDATE_DATE
  is '更新时间';
comment on column catalog_supply_card.REMARKS
  is '备注';
comment on column catalog_supply_card.DEL_FLAG
  is '删除标示 0未删除 1已删除';

--读者证新增二维码
alter table circulate_card add card_img varchar2(200) ;

--rollback delete from circulate_rule where id = '3';
--rollback ALTER table SERVER_CIRCULATE_LOG drop column dir_type;
