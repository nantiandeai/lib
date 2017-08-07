--liquibase formatted sql
--changeset lib3data:5
--IPROTAL菜单修改

--图书单管增加菜单

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('102010400', '102010000', null, '回溯建库', 4, null, null, '1', 'flash-back', '1', '系统导入', '01-1月 -17 12.00.00.000000 上午', null, null, null, '0');

--rollback delete from sys_menu where id = '102010400';