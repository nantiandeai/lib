--liquibase formatted sql
--changeset lib3data:3
--IPROTAL菜单修改
update sys_menu a set a.permission= substr(substr(a.href,
(SELECT instr(a.href,'/',5)+1 as firstchar FROM dual)),1,instr(substr(a.href,
(SELECT instr(a.href,'/',5)+1 as firstchar FROM dual)),'.html')-1) where a.is_show = '1' and a.app_id = '1' and a.permission is null;

--iportal菜单微调
update sys_menu a set a.permission = 'catalog' where a.id = '102000000';
update sys_menu a set a.permission = 'collection' where a.id = '103000000';
update sys_menu a set a.permission = 'circulate' where a.id = '104000000';
update sys_menu a set a.permission = 'lib' where a.id = '105000000';
update sys_menu a set a.permission = 'batch-man' where a.id = '107020000';

--需求：把默认的系统角色名前面的‘默认’去掉
update sys_role a set a.name = substr(a.name,3) where a.id in ('221','222','223','224','225','226','227');
update sys_menu a set a.permission = 'catalog' where a.id = '107010100';
update sys_menu a set a.permission = 'periodical' where a.id = '107000000';
update sys_menu a set a.permission = 'catalogue' where a.id = '107010100';

--rollback update sys_menu a set a.permission= '' where a.is_show = '1' and a.app_id = '1' and a.permission is not null;
--rollback  update sys_role a set a.name = concat('默认'，a.name) where a.id in ('221','222','223','224','225','226','227');
--rollback update sys_menu a set a.permission = '' where a.id = '107010100';
--rollback update sys_menu a set a.permission = '' where a.id = '107000000';
--rollback update sys_menu a set a.permission = 'catalog' where a.id = '107010100';