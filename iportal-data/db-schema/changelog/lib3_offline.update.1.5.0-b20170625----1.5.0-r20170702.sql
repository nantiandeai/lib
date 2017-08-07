--liquibase formatted sql
--changeset lib3data:6
--IPROTAL菜单修改

--离线客户端增加菜单

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('105060000', '105000000', null, '客户端管理', 4, null, 'client_icon', '1', 'client', '1', 'admin', sysdate, null, sysdate, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('105060001', '105060000', null, '认证', 1, null, null, '0', 'lib3school.api.offline.auth', '1', 'admin', sysdate, null, sysdate, null, '0');
insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('105060002', '105060000', null, '下载', 2, null, null, '0', 'lib3school.api.offline.download', '1', 'admin', sysdate, null, sysdate, null, '0');
insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('105060003', '105060000', null, '上传', 3, null, null, '0', 'lib3school.api.offline.upload', '1', 'admin', sysdate, null, sysdate, null, '0');
insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('105060004', '105060000', null, '解绑', 4, null, null, '0', 'lib3school.api.offline.unbind', '1', 'admin', sysdate, null, sysdate, null, '0');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '105060000');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '105060001');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '105060002');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '105060003');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '105060004');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '105060000');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '105060001');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '105060002');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '105060003');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '105060004');