--liquibase formatted sql
--changeset lib3data:2

--新增菜单
insert into sys_menu (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('107000000', '100000000', null, '期刊管理', 3, null, null, '1', null, '1', null, sysdate, null, null, null, '0');

insert into sys_menu (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('107010000', '107000000', null, '期刊管理', 1, null, 'build_icon', '1', null, '1', null, sysdate, null, null, null, '0');

insert into sys_menu (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('107010100', '107010000', null, '期刊编目', 1, '../periodical/catalog.html', null, '1', null, '1', null, sysdate, null, null, null, '0');

insert into sys_menu (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('107010200', '107010000', null, '期刊记到', 2, '../periodical/record.html', null, '1', null, '1', null, sysdate, null, null, null, '0');

insert into sys_menu (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('107010300', '107010000', null, '期刊合订', 3, '../periodical/stapled.html', null, '1', null, '1', null, sysdate, null, null, null, '0');

insert into sys_menu (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('107010400', '107010000', null, '过刊清单', 4, '../periodical/inventory.html', null, '1', null, '1', null, sysdate, null, null, null, '0');

insert into sys_menu (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('107010500', '107010000', null, '书标打印', 5, '../periodical/label-print.html', null, '1', null, '1', null, sysdate, null, null, null, '0');

insert into sys_menu (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('107020000', '107000000', null, '批次管理', 2, '../periodical/batch.html', 'batch_icon', '1', null, '1', null, sysdate, null, null, null, '0');

insert into sys_menu (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('107030000', '107000000', null, '期刊分类统计', 3, '../periodical/classify.html', 'analysis_icon', '1', null, '1', null, sysdate, null, null, null, '0');

insert into sys_menu (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('103070000', '103000000', null, '库室调配', 3, '../collection/collectionsite-list.html', 'room_icon', '1', null, '1', null, sysdate, null, null, null, '0');

insert into sys_menu (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('103060000', '103000000', null, '条码置换', 2, '../collection/code-deploy.html', 'barcode_icon', '1', null, '1', null, sysdate, null, null, null, '0');

insert into sys_menu (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('105050000', '105000000', null, '编目设置', 2, '../lib/dictionary.html', 'collection_icon', '1', null, '1', null, sysdate, null, null, null, '0');

update sys_menu set sort=0 where id='102000000';
update sys_menu set sort=1 where id='103000000';
update sys_menu set sort=2 where id='104000000';
update sys_menu set sort=4 where id='105000000';

--新增角色菜单

insert into sys_role_menu (ROLE_ID, MENU_ID)
values ('1', '107000000');
insert into sys_role_menu (ROLE_ID, MENU_ID)
values ('1', '107010000');
insert into sys_role_menu (ROLE_ID, MENU_ID)
values ('1', '107010100');
insert into sys_role_menu (ROLE_ID, MENU_ID)
values ('1', '107010200');
insert into sys_role_menu (ROLE_ID, MENU_ID)
values ('1', '107010300');
insert into sys_role_menu (ROLE_ID, MENU_ID)
values ('1', '107010400');
insert into sys_role_menu (ROLE_ID, MENU_ID)
values ('1', '107010500');
insert into sys_role_menu (ROLE_ID, MENU_ID)
values ('1', '107020000');
insert into sys_role_menu (ROLE_ID, MENU_ID)
values ('1', '107030000');
insert into sys_role_menu (ROLE_ID, MENU_ID)
values ('1', '103070000');
insert into sys_role_menu (ROLE_ID, MENU_ID)
values ('1', '103060000');
insert into sys_role_menu (ROLE_ID, MENU_ID)
values ('1', '105050000');


insert into sys_role_menu (ROLE_ID, MENU_ID)
values ('222', '103060000');

insert into sys_role_menu (ROLE_ID, MENU_ID)
values ('222', '103070000');

insert into sys_role_menu (ROLE_ID, MENU_ID)
values ('222', '105050000');

insert into sys_role_menu (ROLE_ID, MENU_ID)
values ('222', '107000000');

insert into sys_role_menu (ROLE_ID, MENU_ID)
values ('222', '107010000');

insert into sys_role_menu (ROLE_ID, MENU_ID)
values ('222', '107030000');

insert into sys_role_menu (ROLE_ID, MENU_ID)
values ('222', '107010200');

insert into sys_role_menu (ROLE_ID, MENU_ID)
values ('222', '107010300');

insert into sys_role_menu (ROLE_ID, MENU_ID)
values ('222', '107010400');

insert into sys_role_menu (ROLE_ID, MENU_ID)
values ('222', '107010500');

insert into sys_role_menu (ROLE_ID, MENU_ID)
values ('222', '107020000');

insert into sys_role_menu (ROLE_ID, MENU_ID)
values ('222', '107010100');

insert into sys_role_menu (ROLE_ID, MENU_ID)
values ('224', '103070000');

insert into sys_role_menu (ROLE_ID, MENU_ID)
values ('224', '105050000');

insert into sys_role_menu (ROLE_ID, MENU_ID)
values ('224', '103060000');

insert into sys_role_menu (ROLE_ID, MENU_ID)
values ('225', '107000000');

insert into sys_role_menu (ROLE_ID, MENU_ID)
values ('225', '107010000');

insert into sys_role_menu (ROLE_ID, MENU_ID)
values ('225', '107010100');

insert into sys_role_menu (ROLE_ID, MENU_ID)
values ('225', '107010200');

insert into sys_role_menu (ROLE_ID, MENU_ID)
values ('225', '107010300');

insert into sys_role_menu (ROLE_ID, MENU_ID)
values ('225', '103060000');

insert into sys_role_menu (ROLE_ID, MENU_ID)
values ('225', '107010500');

insert into sys_role_menu (ROLE_ID, MENU_ID)
values ('225', '107020000');

insert into sys_role_menu (ROLE_ID, MENU_ID)
values ('225', '107030000');

insert into sys_role_menu (ROLE_ID, MENU_ID)
values ('225', '105050000');

insert into sys_role_menu (ROLE_ID, MENU_ID)
values ('225', '103070000');

insert into sys_role_menu (ROLE_ID, MENU_ID)
values ('225', '107010400');

--需求：把默认的系统角色名前面的‘默认’去掉
--update sys_role a set a.name = substr(a.name,3) where a.id in ('221','222','223','224','225','226','227');

update sys_menu a set a.permission = 'catalog' where a.id = '107010100';

update sys_menu a set a.permission = 'periodical' where a.id = '107000000';

update sys_menu a set a.permission = 'catalogue' where a.id = '107010100';

--rollback  update sys_role a set a.name = concat('默认'，a.name) where a.id in ('221','222','223','224','225','226','227')
--rollback update sys_menu a set a.permission = '' where a.id = '107010100'
--rollback update sys_menu a set a.permission = '' where a.id = '107000000'
--rollback update sys_menu a set a.permission = 'catalog' where a.id = '107010100'

--rollback DELETE FROM SYS_MENU WHERE ID IN('103060000','103070000','105050000','107000000','107010000','107010100','107010200','107010300','107010400','107010500','107020000','107030000');
--rollback DELETE FROM SYS_ROLE_MENU WHERE MENU_ID IN('103060000','103070000','105050000','107000000','107010000','107010100','107010200','107010300','107010400','107010500','107020000','107030000');

