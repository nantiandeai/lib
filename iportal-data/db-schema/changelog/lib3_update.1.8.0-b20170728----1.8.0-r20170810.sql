--liquibase formatted sql
--changeset lib3data:8
--IPROTAL菜单修改

update sys_menu set href = '../collection/collectionSite-list.html', permission = 'collectionSite-list' where id = '103070000';

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('104020500', '104020000', null, '读者证打印', 7, '../circulate/card-print.html', null, '1', 'card-print', '1', null, null, null, null, null, '0');

--rollback update sys_menu set href = '../collection/collectionsite-list.html', permission = 'collectionsite-list' where id = '103070000';
--rollback delete from sys_menu where id = '104020500';