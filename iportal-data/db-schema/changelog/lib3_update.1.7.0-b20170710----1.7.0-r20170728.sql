--liquibase formatted sql
--changeset lib3data:7
--IPROTAL菜单修改

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('104020400', '104020000', null, '押金管理', 6, '../circulate/cash-pledge.html', null, '1', 'cash-pledge', '1', null, null, null, null, null, '0');

update SYS_MENU set name = '丢失/污损清单' where name = '丢失清单';

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('104040000', '104000000', null, '赔罚管理', 2, null, 'penalty_icon', '1', null, '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('104040100', '104040000', null, '赔罚清单', 1, '../circulate/penalty.html', null, '1', 'penalty', '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('104040200', '104040000', null, '赔书管理', 2, '../circulate/lost-book.html', null, '1', 'lost-book', '1', null, null, null, null, null, '0');

update SYS_MENU set sort = 3 where id = '104030000';

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID) values ('225', '104020400');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID) values ('1', '205000000');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID) values ('1', '205010000');

--rollback delete from sys_menu where id = '104020400';
--rollback delete from SYS_ROLE_MENU where ROLE_ID = '225' and MENU_ID = '104020400';
--rollback delete from SYS_ROLE_MENU where ROLE_ID = '1' and MENU_ID = '205000000';
--rollback delete from SYS_ROLE_MENU where ROLE_ID = '1' and MENU_ID = '205010000';
--rollback update SYS_MENU set name = '丢失清单' where name = '丢失/污损清单';
--rollback delete from sys_menu where id = '104040000';
--rollback delete from sys_menu where id = '104040100';
--rollback delete from sys_menu where id = '104040200';