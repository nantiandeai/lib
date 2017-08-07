--liquibase formatted sql
--changeset lianyitech:7
create table SERVER_APP
(
   id                VARCHAR2(64),
   user_name          VARCHAR2(64),
   org_id             VARCHAR2(64),
   cert_key           VARCHAR2(64),
   status             CHAR(1),
   create_date        TIMESTAMP(6),
   update_date        TIMESTAMP(6)
);
alter table SERVER_APP add constraint PK_SERVER_APP_id primary key (ID);
comment on table SERVER_APP is '认证申请表';
comment on column SERVER_APP.id is '编号';
comment on column SERVER_APP.user_name is '用户名';
comment on column SERVER_APP.org_id is '机构id';
comment on column SERVER_APP.cert_key is '服务端返回的认证key';
comment on column SERVER_APP.status is '状态 0可用 1不可用';
comment on column SERVER_APP.create_date is '创建时间';

create table SERVER_CIRCULATE_LOG
(
   id                 VARCHAR2(64),
   card               VARCHAR2(64),
   bar_code           VARCHAR2(64),
   org_id             VARCHAR2(64),
   op_type            CHAR(1),
   borrow_date        TIMESTAMP (6),
   return_date        TIMESTAMP (6),
   should_return_date TIMESTAMP (6),
   create_by          VARCHAR2(64),
   create_date        TIMESTAMP(6),
   sync_date          TIMESTAMP(6),
   status             CHAR(1),
   server_time_id     VARCHAR2(64),
   ERROR_INFO          VARCHAR2(2000),
   update_date        TIMESTAMP (6)
);
comment on table SERVER_CIRCULATE_LOG is '离线借阅同步记录表';
comment on column SERVER_CIRCULATE_LOG.id is '编号';
comment on column SERVER_CIRCULATE_LOG.card is '读者证号';
comment on column SERVER_CIRCULATE_LOG.bar_code is '条形码';
comment on column SERVER_CIRCULATE_LOG.op_type is '操作类型 0借书 1还书';
comment on column SERVER_CIRCULATE_LOG.borrow_date is '应还日期';
comment on column SERVER_CIRCULATE_LOG.return_date is '应还日期';
comment on column SERVER_CIRCULATE_LOG.should_return_date is '应还日期';
comment on column SERVER_CIRCULATE_LOG.create_by is '创建人';
comment on column SERVER_CIRCULATE_LOG.create_date is '创建时间';
comment on column SERVER_CIRCULATE_LOG.sync_date is '创建时间';
comment on column SERVER_CIRCULATE_LOG.STATUS  is '类型：0:上传成功 1:同步成功 2:同步失败';
comment on column SERVER_CIRCULATE_LOG.server_time_id is '上传时间id';
comment on column SERVER_CIRCULATE_LOG.ERROR_INFO is '失败原因';
comment on column SERVER_CIRCULATE_LOG.update_date is '修改时间'
alter table SERVER_CIRCULATE_LOG add constraint PK_SERVER_LOG_ID primary key (ID);
create table SERVER_TIME 
(
   id                 VARCHAR2(64),
   org_id             VARCHAR2(64),
   client_date        TIMESTAMP(6),
   create_date        TIMESTAMP(6)
);
alter table SERVER_TIME add constraint PK_SERVER_TIME_id primary key (ID);
comment on table SERVER_TIME is
'上传时间表';

comment on column SERVER_TIME.id is
'编号';

comment on column SERVER_TIME.org_id is
'机构id';

comment on column SERVER_TIME.create_date is
'创建时间';
alter table CIRCULATE_LOG add source char(1);
comment on column CIRCULATE_LOG.SOURCE is '操作来源(1:离线)';

alter table SERVER_CIRCULATE_LOG add type char(1) default '0';
comment on column SERVER_CIRCULATE_LOG.type is '给离线客户端生成好同步结果（成功与失败）1:准备生成  2:生成完成 其他：未处理';

create table SERVER_SYNC
(
   id                 VARCHAR2(64),
   org_id             VARCHAR2(64),
   start_date         TIMESTAMP(6),
   end_date           TIMESTAMP(6),
   status             char(1),
   type               varchar2(2),
   create_date        TIMESTAMP(6),
   update_date        TIMESTAMP(6)
);
comment on table SERVER_SYNC is
'同步记录表';

comment on column SERVER_SYNC.ID is
'编号';

comment on column SERVER_SYNC.ORG_ID is
'机构id';

comment on column SERVER_SYNC.START_DATE is
'上次同步时间';

comment on column SERVER_SYNC.END_DATE is
'本次同步时间';

comment on column SERVER_SYNC.STATUS is
'生成状态标示';
comment on column SERVER_SYNC.TYPE is
'类型';
--rollback DROP TABLE SERVER_APP;
--rollback DROP TABLE SERVER_CIRCULATE_LOG;
--rollback DROP TABLE SERVER_TIME;
--rollback DROP TABLE SERVER_TIME;
