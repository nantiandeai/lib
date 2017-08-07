--liquibase formatted sql
--changeset lib3data:4
--IPROTAL菜单修改

--图书行管增加菜单权限
update sys_menu a set a.permission = 'home' where a.id = '201000000';
update sys_menu a set a.permission = 'statistics' where a.id = '202000000';
update sys_menu a set a.permission = 'booknum' where a.id = '202010000';
update sys_menu a set a.permission = 'spike' where a.id = '202020000';
update sys_menu a set a.permission = 'borrowrate' where a.id = '202030000';
update sys_menu a set a.permission = 'readerrate' where a.id = '202040000';
update sys_menu a set a.permission = 'libraryrate' where a.id = '202050000';
update sys_menu a set a.permission = 'libraryclass' where a.id = '202060000';
update sys_menu a set a.permission = 'rank' where a.id = '203000000';
update sys_menu a set a.permission = 'bookrank' where a.id = '203010000';
update sys_menu a set a.permission = 'schoolrank' where a.id = '203020000';
update sys_menu a set a.permission = 'readerrank' where a.id = '203030000';
--图书行管增加菜单
insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('205000000', '200000000', null, '应用分析', 2, null, null, '1', 'analysis', '2', null, '01-1月 -17 12.00.00.000000 上午', null, null, null, '0');
insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('205010000', '205000000', null, '应用进度分析', 0, '../analysis/school-analysis.html', 'analysis_icon', '1', 'schoolanalysis', '2', null, '01-1月 -17 12.00.00.000000 上午', null, null, null, '0');
update sys_menu set sort=3 where id = '203000000';

--rollback delete from sys_menu where id = '205000000';
--rollback delete from sys_menu where id = '205010000';
--rollback update sys_menu set sort=2 where id = '203000000';
--rollback update sys_menu a set a.permission = null where a.id = '201000000';
--rollback update sys_menu a set a.permission = null where a.id = '202000000';
--rollback update sys_menu a set a.permission = null where a.id = '202010000';
--rollback update sys_menu a set a.permission = null where a.id = '202020000';
--rollback update sys_menu a set a.permission = null where a.id = '202030000';
--rollback update sys_menu a set a.permission = null where a.id = '202040000';
--rollback update sys_menu a set a.permission = null where a.id = '202050000';
--rollback update sys_menu a set a.permission = null where a.id = '202060000';
--rollback update sys_menu a set a.permission = null where a.id = '203000000';
--rollback update sys_menu a set a.permission = null where a.id = '203010000';
--rollback update sys_menu a set a.permission = null where a.id = '203020000';
--rollback update sys_menu a set a.permission = null where a.id = '203030000';