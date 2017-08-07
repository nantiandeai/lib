--liquibase formatted sql
--changeset lib3data:1

--初始化OAuth Client
insert into OAUTH_CLIENT (CLIENT_ID, RESOURCE_IDS, CLIENT_SECRET, SCOPE, AUTHORIZED_GRANT_TYPES, WEB_SERVER_REDIRECT_URI, AUTHORITIES, ACCESS_TOKEN_VALIDITY, REFRESH_TOKEN_VALIDITY, ADDITIONAL_INFORMATION, AUTOAPPROVE, APP_ID)
values ('lib3school_client', 'iportal', '123456', 'read,write', 'authorization_code,implicit,password', null, 'ROLE_IPORTAL', null, null, null, null, '0');

insert into OAUTH_CLIENT (CLIENT_ID, RESOURCE_IDS, CLIENT_SECRET, SCOPE, AUTHORIZED_GRANT_TYPES, WEB_SERVER_REDIRECT_URI, AUTHORITIES, ACCESS_TOKEN_VALIDITY, REFRESH_TOKEN_VALIDITY, ADDITIONAL_INFORMATION, AUTOAPPROVE, APP_ID)
values ('lib3industry_client', 'iportal', '123456', 'read,write', 'authorization_code,implicit,password', null, 'ROLE_IPORTAL', null, null, null, null, '0');

insert into OAUTH_CLIENT (CLIENT_ID, RESOURCE_IDS, CLIENT_SECRET, SCOPE, AUTHORIZED_GRANT_TYPES, WEB_SERVER_REDIRECT_URI, AUTHORITIES, ACCESS_TOKEN_VALIDITY, REFRESH_TOKEN_VALIDITY, ADDITIONAL_INFORMATION, AUTOAPPROVE, APP_ID)
values ('reader_client', 'reader', '123456', 'read,write', 'authorization_code,implicit,password', null, 'ROLE_IPORTAL', null, null, null, null, '0');


--初始化应用
insert into SYS_APP (ID, NAME, URL, USEABLE, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG, ICON, IMG, BIGIMG, HOMEIMG)
values ('1', '图书系统', 'http://hebei.zbglxt.com/lib/lib3school', '1', null, sysdate, null, null, null, '0', 'lib', null, null, null);

insert into SYS_APP (ID, NAME, URL, USEABLE, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG, ICON, IMG, BIGIMG, HOMEIMG)
values ('2', '图书决策支持系统', 'http://hebei.zbglxt.com/lib/lib3industry', '1', null, sysdate, null, null, null, '0', 'lib-industy', null, null, null);

insert into SYS_APP (ID, NAME, URL, USEABLE, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG, ICON, IMG, BIGIMG, HOMEIMG)
values ('4', '阅读平台行管', 'https://www.ly100.cn/hebei/reader/', '1', null, sysdate, null, null, null, '0', 'read', null, null, null);


--初始化菜单
insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('100000000', 'root', null, '图书系统', null, null, null, '1', null, '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('102000000', '100000000', null, '编目管理', 2, null, null, '1', null, '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('102010000', '102000000', null, '图书建库', 1, null, 'build_icon', '1', null, '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('102010100', '102010000', null, '新书编目', 1, '../catalog/search.html', null, '1', null, '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('102010200', '102010000', null, '馆藏导入', 2, '../catalog/collection-import.html', null, '1', null, '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('102010300', '102010000', null, '书标打印', 3, '../catalog/label-print.html', null, '1', null, '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('102020000', '102000000', null, '批次管理', 2, '../catalog/batch.html', 'batch_icon', '1', null, '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('103000000', '100000000', null, '典藏管理', 3, null, null, '1', null, '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('103010000', '103000000', null, '馆藏清单', 1, '../collection/collection-list.html', 'collection_icon', '1', null, '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('103020000', '103000000', null, '丢失清单', 2, '../collection/loss-list.html', 'loss_icon', '1', null, '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('103030000', '103000000', null, '剔旧清单', 3, '../collection/weeding-list.html', 'weeding_icon', '1', null, '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('103040000', '103000000', null, '报废清单', 4, '../collection/scrap-list.html', 'scrap_icon', '1', null, '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('103050000', '103000000', null, '统计分析', 5, null, 'analysis_icon', '1', null, '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('103050100', '103050000', null, '复本统计', 1, '../collection/book-report.html', null, '1', null, '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('103050200', '103050000', null, '藏书分类统计', 2, '../collection/class-report.html', null, '1', null, '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('103050300', '103050000', null, '藏书分布结构', 3, '../collection/distribute-report.html', null, '1', null, '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('104000000', '100000000', null, '流通管理', 4, null, null, '1', null, '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('104010000', '104000000', null, '借阅管理', 1, null, 'borrow_icon', '1', null, '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('104010100', '104010000', null, '借阅管理', 1, '../circulate/borrow.html', null, '1', null, '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('104010200', '104010000', null, '预约预借', 2, '../circulate/order.html', null, '1', null, '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('104010300', '104010000', null, '流通记录', 3, '../circulate/circulate-record.html', null, '1', null, '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('104010400', '104010000', null, '超期催还', 4, '../circulate/return.html', null, '1', null, '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('104020000', '104000000', null, '读者管理', 2, null, 'reader_icon', '1', null, '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('104020100', '104020000', null, '读者导入', 1, '../circulate/data-import.html', null, '1', null, '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('104020200', '104020000', null, '读者管理', 2, '../circulate/reader.html', null, '1', null, '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('104020300', '104020000', null, '组织管理', 3, '../circulate/organization.html', null, '1', null, '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('104030000', '104000000', null, '统计分析', 3, null, 'analysis_icon', '1', null, '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('104030100', '104030000', null, '文献流通统计', 1, '../circulate/doc-count.html', null, '1', null, '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('104030200', '104030000', null, '读者借阅统计', 2, '../circulate/reader-report.html', null, '1', null, '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('104030300', '104030000', null, '流通率统计', 3, '../circulate/circulate-report.html', null, '1', null, '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('104030400', '104030000', null, '文献借阅排行榜', 4, '../circulate/doc-rank.html', null, '1', null, '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('104030500', '104030000', null, '读者借阅排行榜', 5, '../circulate/reader-rank.html', null, '1', null, '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('104030600', '104030000', null, '组织借阅排行榜', 6, '../circulate/organization-rank.html', null, '1', null, '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('105000000', '100000000', null, '图书馆管理', 5, null, null, '1', null, '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('105020000', '105000000', null, '借阅规则', 2, '../lib/rule.html', 'rule_icon', '1', null, '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('105030000', '105000000', null, '馆藏地管理', 3, '../lib/site.html', 'site_icon', '1', null, '1', null, null, null, null, null, '0');
insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('200000000', 'root', null, '图书决策支持系统', null, null, null, '1', null, '2', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('201000000', '200000000', null, '主页', 1, '../index/index.html', null, '1', null, '2', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('202000000', '200000000', null, '报表统计', 2, null, null, '1', null, '2', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('202010000', '202000000', null, '藏书量统计', 3, '../statistics/book-num.html', 'book_icon', '1', null, '2', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('202020000', '202000000', null, '生均及增量统计', 4, '../statistics/spike.html', 'spike_icon', '1', null, '2', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('202030000', '202000000', null, '生均借阅统计', 5, '../statistics/borrow-rate.html', 'borrow_icon', '1', null, '2', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('202040000', '202000000', null, '读者借阅率统计', 6, '../statistics/reader-rate.html', 'reader_icon', '1', null, '2', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('202050000', '202000000', null, '图书流通率统计', 7, '../statistics/library-rate.html', 'library_icon', '1', null, '2', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('202060000', '202000000', null, '藏书分类统计', 8, '../statistics/library-class.html', 'class_icon', '1', null, '2', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('203000000', '200000000', null, '排行榜', 9, null, null, '1', null, '2', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('203010000', '203000000', null, '图书借阅排行榜', 10, '../rink/book-rink.html', 'library_icon', '1', null, '2', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('203020000', '203000000', null, '学校借阅排行榜', 11, '../rink/school-rink.html', 'school_icon', '1', null, '2', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('203030000', '203000000', null, '读者借阅排行榜', 12, '../rink/reader-rink.html', 'reader_icon', '1', null, '2', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('400000000', 'root', null, '阅读平台行管', null, null, null, '1', null, '4', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('401000000', '400000000', null, '阅读活动', 1, 'read.html', 'read', '1', null, '4', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('402000000', '400000000', null, '班级管理', 2, 'class.html', 'class', '1', null, '4', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('403000000', '400000000', null, '公告管理', 3, 'notice.html', 'notice', '1', null, '4', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('404000000', '400000000', null, '统计报表', 4, 'javascript:void(0)', 'participation', '1', null, '4', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('404010000', '404000000', null, '好书评选', 401, 'voteStatistics.html', null, '1', null, '4', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('404030000', '404000000', null, '阅读活动', 403, 'actParticipation.html', null, '1', null, '4', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('405000000', '400000000', null, '消息提醒', 5, 'news.html', 'news', '1', null, '4', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('101010001', '101010000', null, 'opac检索按钮', null, null, null, '0', null, '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('102010101', '102010100', null, '书目查询按钮', null, 'catalog/bookdirectory/listsolr', null, '0', 'lib3school.api.catalog.bookdirectory.listsolr.get', '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('102010102', '102010100', null, '书目删除按钮', null, 'catalog/bookdirectory/{id}', null, '0', 'lib3school.api.catalog.bookdirectory.delete', '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('102010103', '102010100', null, '馆藏查询', null, 'catalog/copy/{id}', null, '0', 'lib3school.api.catalog.copy.get', '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('102010104', '102010100', null, '种次号查缺', null, 'catalog/bookdirectory/createTanejiNo', null, '0', 'lib3school.api.catalog.bookdirectory.createTanejiNo.get', '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('102010105', '102010100', null, '种次号唯一验证', null, 'catalog/bookdirectory/uniqueTanejiNo', null, '0', 'lib3school.api.catalog.bookdirectory.uniqueTanejiNo.get', '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('102010106', '102010100', null, '馆藏添加/馆藏修改按钮', null, 'catalog/copy', null, '0', 'lib3school.api.catalog.copy.post', '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('102010107', '102010100', null, '馆藏批量添加按钮', null, 'catalog/copy/addCopyAll', null, '0', 'lib3school.api.catalog.copy.addCopyAll.post', '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('102010108', '102010100', null, '馆藏删除按钮', null, 'catalog/copy/{id}', null, '0', 'lib3school.api.catalog.copy.delete', '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('102010109', '102010100', null, '批次号获取', null, 'catalog/copy/getAllBatchNo', null, '0', 'lib3school.api.catalog.copy.getAllBatchNo.get', '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('102010110', '102010100', null, '馆藏地点获取', null, 'catalog/copy/getAllSiteName', null, '0', 'lib3school.api.catalog.copy.getAllSiteName.get', '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('102010111', '102010100', null, '语种获取', null, 'sys/dict/1/list', null, '0', 'lib3school.api.sys.dict.list.get', '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('102010112', '102010100', null, '装帧方式', null, 'sys/dict/2/list', null, '0', 'lib3school.api.sys.dict.list.get', '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('102010113', '102010100', null, '适读年龄', null, 'sys/dict/3/list', null, '0', 'lib3school.api.sys.dict.list.get', '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('102010114', '102010100', null, '图书用途', null, 'sys/dict/4/list', null, '0', 'lib3school.api.sys.dict.list.get', '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('102010115', '102010100', null, '出版社获取', null, 'sys/publishing', null, '0', 'lib3school.api.sys.publishing.get', '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('102010116', '102010100', null, '分类号获取', null, 'sys/librarsort', null, '0', 'lib3school.api.sys.librarsort.get', '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('102010117', '102010100', null, '简单书目验证', null, 'catalog/bookdirectory/checkBook', null, '0', 'lib3school.api.catalog.bookdirectory.checkBook.post', '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('102010118', '102010100', null, '简单书目保存', null, 'catalog/bookdirectory', null, '0', 'lib3school.api.catalog.bookdirectory.post', '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('102010119', '102010100', null, '简单书目查询', null, 'catalog/bookdirectory/{id}', null, '0', 'lib3school.api.catalog.batch.get', '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('102010120', '102010100', null, '简单书目复制查询', null, 'catalog/bookdirectory/copy/{id}', null, '0', 'lib3school.api.catalog.bookdirectory.copy.get', '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('102010121', '102010100', null, '根据isbn查询出版社', null, 'sys/publishing/listData', null, '0', 'lib3school.api.sys.publishing.listData.get', '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('102010122', '102010100', null, '初始marc数据', null, 'catalog/bookdirectory/marcinfo', null, '0', 'lib3school.api.catalog.bookdirectory.marcinfo.get', '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('102010123', '102010100', null, 'marc数据验证', null, 'catalog/bookdirectory/checkMarc', null, '0', 'lib3school.api.catalog.bookdirectory.checkMarc.post', '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('102010124', '102010100', null, '校验分类号是否修改', null, 'catalog/bookdirectory/checkSortCodeMarc', null, '0', 'lib3school.api.catalog.bookdirectory.checkSortCodeMarc.post', '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('102010125', '102010100', null, 'marc数据保存', null, 'catalog/bookdirectory/bookMarc', null, '0', 'lib3school.api.catalog.bookdirectory.bookMarc.post', '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('102010126', '102010100', null, '根据分类号加载主题词', null, 'sys/librarsort/getSubjects', null, '0', 'lib3school.api.sys.librarsort.getSubjects.get', '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('102010127', '102010100', null, 'marc字段搜索', null, 'catalog/bookdirectory/mainMarcinfo', null, '0', 'lib3school.api.catalog.bookdirectory.mainMarcinfo.get', '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('102010128', '102010100', null, '校验分类号', null, 'sys/librarsort/checkSortCode', null, '0', 'lib3school.api.catalog.bookdirectory.checkSortCode.post', '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('102010201', '102010200', null, '馆藏excel模板下载', null, 'lib3school.api.catalog.bookdirectory.downloadBookDirTemplate.get', null, '0', 'lib3school.api.catalog.bookdirectory.downloadBookDirTemplate.get', '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('102010202', '102010200', null, '馆藏数据导入', null, 'catalog/bookdirectory/importBookDir', null, '0', 'lib3school.api.catalog.bookdirectory.importBookDir.post', '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('102010301', '102010300', null, '书标提取', null, 'catalog/copy/bookLabel', null, '0', 'lib3school.api.catalog.copy.bookLabel.post', '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('102010302', '102010300', null, '导出txt', null, 'catalog/copy/exportBookLabelTxt', null, '0', 'lib3school.api.catalog.copy.exportBookLabelTxt.get', '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('102010303', '102010300', null, '导出excel', null, 'catalog/copy/exportBookLabelExcel', null, '0', 'lib3school.api.catalog.copy.exportBookLabelExcel.get', '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('102020001', '102020000', null, '查询按钮', null, 'catalog/batch', null, '0', 'lib3school.api.catalog.batch.get', '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('102020002', '102020000', null, '新增/修改按钮', null, 'catalog/batch', null, '0', 'lib3school.api.catalog.batch.post', '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('102020003', '102020000', null, '删除按钮', null, 'catalog/batch/{id}', null, '0', 'lib3school.api.catalog.batch.id.get', '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('102030001', '102030000', null, '查询按钮', null, 'catalog/newbooknotifiy', null, '0', 'lib3school.api.catalog.newbooknotifiy.get', '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('102030002', '102030000', null, '新增/修改按钮', null, 'catalog/newbooknotifiy', null, '0', 'lib3school.api.catalog.newbooknotifiy.post', '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('102030003', '102030000', null, '删除按钮', null, 'catalog/newbooknotifiy/{id}', null, '0', 'lib3school.api.catalog.newbooknotifiy.delete', '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('102030004', '102030000', null, '复本查询', null, 'catalog/copy/newbookReportList', null, '0', 'lib3school.api.catalog.copy.newbookReportList.get', '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('102030005', '102030000', null, '通报按钮', null, 'catalog/newbooknotifiy/addNewBooksToReports', null, '0', 'lib3school.api.catalog.newbooknotifiy.addNewBooksToReports.post', '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('102030006', '102030000', null, '入库批次获取', null, 'catalog/copy/getAllBatchNo', null, '0', 'lib3school.api.catalog.copy.getAllBatchNo.get', '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('102030007', '102030000', null, '通报详情列表', null, 'catalog/newbooknotifiy/findNewbookList', null, '0', 'lib3school.api.catalog.newbooknotifiy.findNewbookList.get', '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('102030008', '102030000', null, '通报详情列表导出', null, 'catalog/newbooknotifiy/exportNewBooksReports', null, '0', 'lib3school.api.catalog.newbooknotifiy.exportNewBooksReports.get', '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('103010001', '103010000', null, '查询按钮', null, 'catalog/copy', null, '0', 'lib3school.api.catalog.copy.get', '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('103010002', '103010000', null, '馆藏地点获取', null, 'catalog/copy/getAllSiteName', null, '0', 'lib3school.api.catalog.copy.getAllSiteName.get', '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('103010003', '103010000', null, '馆藏状态获取', null, null, null, '0', null, '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('103010004', '103010000', null, '剔旧按钮', null, 'circulate/multi', null, '0', 'lib3school.api.circulate.multi.post', '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('103010005', '103010000', null, '删除按钮', null, 'catalog/copy/{id}', null, '0', 'lib3school.api.catalog.copy.delete', '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('103010006', '103010000', null, '导出操作', null, 'catalog/copy/collectionBookReports', null, '0', 'lib3school.api.catalog.copy.collectionBookReports.get', '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('103020001', '103020000', null, '查询按钮', null, 'catalog/copy/findScrapList?type=24=4', null, '0', 'lib3school.api.catalog.copy.findScrapList.get', '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('103020002', '103020000', null, '导出操作', null, 'catalog/copy/exportLoseListReports?type=24=4', null, '0', 'lib3school.api.catalog.copy.exportLoseListReports.get', '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('103030001', '103030000', null, '查询按钮', null, 'catalog/copy/findScrapList?type=32=2', null, '0', 'lib3school.api.catalog.copy.findScrapList.get', '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('103030002', '103030000', null, '报废按钮', null, 'circulate/multi', null, '0', 'lib3school.api.circulate.multi.post', '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('103030003', '103030000', null, '导出操作', null, 'catalog/copy/exportWeedingListReports?type=32=2', null, '0', 'lib3school.api.catalog.copy.exportWeedingListReports.get', '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('103040001', '103040000', null, '查询按钮', null, 'catalog/copy/findScrapList?type=43=3', null, '0', 'lib3school.api.catalog.copy.findScrapList.get', '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('103040002', '103040000', null, '导出操作', null, 'catalog/copy/exportScrapListReports?type=43=3', null, '0', 'lib3school.api.catalog.copy.exportScrapListReports.get', '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('104010101', '104010100', null, '读者借阅信息', null, 'circulate/bill/circulateInfo', null, '0', 'lib3school.api.circulate.bill.circulateInfo.get', '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('104010102', '104010100', null, '图书信息', null, 'circulate/bill/findBookByBarCode', null, '0', 'lib3school.api.circulate.bill.findBookByBarCode.get', '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('104010103', '104010100', null, '流通操作', null, 'circulate/bill/circulateInfo', null, '0', 'lib3school.api.circulate.bill.circulateInfo.get', '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('104010301', '104010300', null, '查询按钮', null, 'circulate/bill/findAllCirculate', null, '0', 'lib3school.api.circulate.bill.findAllCirculate.get', '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('104010302', '104010300', null, '导出操作', null, 'circulate/bill/exportCirculateInfo', null, '0', 'lib3school.api.circulate.bill.exportCirculateInfo.get', '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('104010303', '104010300', null, '操作行为', null, null, null, '0', null, '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('104010401', '104010400', null, '查询按钮', null, 'circulate/bill/findAllCirculate', null, '0', 'lib3school.api.circulate.bill.findAllCirculate.get', '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('104010402', '104010400', null, '导出操作', null, 'circulate/bill/exportPastDayInfo', null, '0', 'lib3school.api.circulate.bill.exportPastDayInfo.get', '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('104020101', '104020100', null, '读者模板下载', null, 'circulate/reader/downloadReaderTemplate', null, '0', 'lib3school.api.circulate.reader.downloadReaderTemplate.get', '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('104020102', '104020100', null, '读者数据导入', null, 'circulate/reader/importReaderCard', null, '0', 'lib3school.api.circulate.reader.importReaderCard.post', '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('104020201', '104020200', null, '读者类型获取', null, null, null, '0', null, '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('104020202', '104020200', null, '读者证状态获取', null, null, null, '0', null, '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('104020203', '104020200', null, '读者组织获取', null, 'circulate/group', null, '0', 'lib3school.api.circulate.group.get', '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('104020204', '104020200', null, '查询按钮', null, 'circulate/reader', null, '0', 'lib3school.api.circulate.reader.get', '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('104020205', '104020200', null, '新增/修改按钮', null, 'circulate/reader', null, '0', 'lib3school.api.circulate.reader.post', '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('104020206', '104020200', null, '注销', null, 'circulate/reader/logOutReaderCard/{id}', null, '0', 'lib3school.api.circulate.reader.logOutReaderCard.delete', '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('104020207', '104020200', null, '恢复', null, 'circulate/reader/regainReaderCard/{id}', null, '0', 'lib3school.api.circulate.reader.regainReaderCard.delete', '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('104020208', '104020200', null, '删除按钮', null, 'circulate/reader/{id}', null, '0', 'lib3school.api.circulate.reader.delete', '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('104020209', '104020200', null, '挂失按钮', null, 'circulate/reader/changeStatusAction', null, '0', 'lib3school.api.circulate.reader.changeStatusAction.post', '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('104020210', '104020200', null, '解挂按钮', null, 'circulate/reader/changeStatusAction', null, '0', 'lib3school.api.circulate.reader.changeStatusAction.post', '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('104020211', '104020200', null, '换证按钮', null, 'circulate/reader/changeStatusAction', null, '0', 'lib3school.api.circulate.reader.changeStatusAction.post', '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('104020212', '104020200', null, '读者证类型获取', null, null, null, '0', null, '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('104020213', '104020200', null, '导出操作', null, 'circulate/reader/exportReader', null, '0', 'lib3school.api.circulate.reader.exportReader.get', '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('104020214', '104020200', null, '续期按钮', null, 'circulate/reader/changeStatusAction', null, '0', 'lib3school.api.circulate.reader.changeStatusAction.post', '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('104020215', '104020200', null, '批量修改', null, 'circulate/reader/updateReader', null, '0', 'lib3school.api.circulate.reader.updateReader.post', '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('104020301', '104020300', null, '查询按钮', null, 'circulate/group', null, '0', 'lib3school.api.circulate.group.get', '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('104020302', '104020300', null, '新增/修改按钮', null, 'circulate/group', null, '0', 'lib3school.api.circulate.group.post', '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('104020303', '104020300', null, '注销按钮', null, 'circulate/group/logOutGroup/{id}', null, '0', 'lib3school.api.circulate.group.logOutGroup.delete', '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('104020304', '104020300', null, '删除按钮', null, 'circulate/group/{id}', null, '0', 'lib3school.api.circulate.group.delete', '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('105010001', '105010000', null, '查询按钮', null, 'lib/noticeManage', null, '0', 'lib3school.api.lib.noticeManage.get', '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('105010002', '105010000', null, '删除按钮', null, 'lib/noticeManage/{id}', null, '0', 'lib3school.api.lib.noticeManage.delete', '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('105010003', '105010000', null, '置顶按钮', null, 'lib/noticeManage/UpSite', null, '0', 'lib3school.api.lib.noticeManage.UpSite.post', '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('105010004', '105010000', null, '保存', null, 'lib/noticeManage', null, '0', 'lib3school.api.lib.noticeManage.post', '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('105010005', '105010000', null, '文件上传', null, 'lib/noticeManage/moreUpload', null, '0', 'lib3school.api.lib.noticeManage.moreUpload.post', '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('105010006', '105010000', null, '信息详情', null, 'lib/noticeManage/form/{id}', null, '0', 'lib3school.api.lib.noticeManage.form.get', '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('105020001', '105020000', null, '查询', null, 'circulate/rule', null, '0', 'lib3school.api.circulate.rule.get', '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('105020002', '105020000', null, '保存', null, 'circulate/rule', null, '0', 'lib3school.api.circulate.rule.post', '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('105030001', '105030000', null, '查询', null, 'sys/collectionsite', null, '0', 'lib3school.api.sys.collectionsite.get', '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('105030002', '105030000', null, '新增/修改按钮', null, 'sys/collectionsite', null, '0', 'lib3school.api.sys.collectionsite.post', '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('105030003', '105030000', null, '删除按钮', null, 'sys/collectionsite/{id}', null, '0', 'lib3school.api.sys.collectionsite.delete', '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('105030004', '105030000', null, '馆藏库类型获取', null, 'sys/dict/5/list', null, '0', 'lib3school.api.sys.dict.list.get', '1', null, null, null, null, null, '0');

insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, SORT, HREF, ICON, IS_SHOW, PERMISSION, APP_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('105030005', '105030000', null, '批量编辑', null, 'sys/collectionsite/updatecollectionsite', null, '0', 'lib3school.api.sys.collectionsite.updatecollectionsite.post', '1', null, null, null, null, null, '0');


--初始化用户
insert into SYS_USER (ID, ORG_ID, LOGIN_NAME, PASSWORD, NAME, EMAIL, PHONE, MOBILE, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG, DEPARTMENT_ID, LOGIN_DATE, VALID, LOGIN_COUNT, ERR_LOGIN_DATE, SUCC_LOGIN_COUNT, LAST_LOGIN_DATE)
values ('230', null, 'reader_default', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', '阅读平台访问单馆用户', null, null, '13700000000', '1', sysdate, '1', sysdate, '阅读平台访问单馆用户', '0', null, sysdate, '1', 0, null, 0, null);


--初始化角色
insert into SYS_ROLE (ID, ORG_ID, NAME, ENNAME, DATA_SCOPE, USEABLE, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG, TYPE, DISPLAYTYPE)
values ('226', '000000', '默认阅读平台行管', 'readerM', null, '1', '1', null, '1', null, null, '0', '0', '1');

insert into SYS_ROLE (ID, ORG_ID, NAME, ENNAME, DATA_SCOPE, USEABLE, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG, TYPE, DISPLAYTYPE)
values ('227', '000000', '默认阅读平台校行管', 'readerC', null, '1', '1', null, '1', null, null, '0', '0', '1');


--初始化角色对应权限
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '105020001');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '105020002');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '105030000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '105030001');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '105030002');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '105030003');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '105030004');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '105030005');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '200000000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '201000000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '202000000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '202010000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '202020000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '202030000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '202040000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '202050000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '202060000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '203000000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '203010000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '203020000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '203030000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '103030001');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '103030002');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '103030003');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '103040000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '103040001');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '103040002');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '103050000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '103050100');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '103050200');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '103050300');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '104000000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '104010000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '104010100');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '104010101');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '104010102');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '104010103');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '104010200');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '104010300');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '104010301');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '104010302');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '104010303');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '104010400');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '104010401');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '104010402');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '104020000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '104020100');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '104020101');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '104020102');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '104020200');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '104020201');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '104020202');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '104020203');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '104020204');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '104020205');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '104020206');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '104020207');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '104020208');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '104020209');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '104020210');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '104020211');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '104020212');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '104020213');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '104020214');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '104020215');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '104020300');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '104020301');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '104020302');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '104020303');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '104020304');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '104030000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '104030100');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '104030200');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '104030300');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '104030400');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '104030500');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '104030600');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '105000000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '105010001');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '105010002');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '105010003');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '105010004');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '105010005');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '105010006');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '105020000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '100000000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '101010001');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '102000000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '102010000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '102010100');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '102010101');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '102010102');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '102010103');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '102010104');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '102010105');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '102010106');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '102010107');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '102010108');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '102010109');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '102010110');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '102010111');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '102010112');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '102010113');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '102010114');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '102010115');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '102010116');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '102010117');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '102010118');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '102010119');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '102010120');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '102010121');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '102010122');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '102010123');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '102010124');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '102010125');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '102010126');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '102010127');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '102010128');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '102010200');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '102010201');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '102010202');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '102010300');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '102010301');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '102010302');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '102010303');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '102020000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '102020001');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '102020002');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '102020003');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '102030001');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '102030002');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '102030003');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '102030004');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '102030005');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '102030006');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '102030007');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '102030008');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '103000000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '103010000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '103010001');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '103010002');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '103010003');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '103010004');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '103010005');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '103010006');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '103020000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '103020001');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '103020002');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('1', '103030000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('221', '200000000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('221', '405000000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('221', '202000000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('221', '202010000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('221', '202020000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('221', '202030000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('221', '202040000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('221', '202050000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('221', '202060000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('221', '203000000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('221', '203010000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('221', '203020000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('221', '203030000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('221', '400000000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('221', '401000000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('221', '403000000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('221', '404000000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('221', '404010000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('221', '404030000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('221', '201000000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('222', '200000000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('222', '201000000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('222', '202000000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('222', '202010000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('222', '202020000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('222', '202030000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('222', '202040000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('222', '202050000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('222', '202060000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('222', '203000000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('222', '203010000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('222', '203020000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('222', '203030000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('222', '400000000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('222', '401000000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('222', '403000000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('222', '404000000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('222', '404010000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('222', '404030000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('222', '405000000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('223', '200000000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('223', '201000000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('223', '202000000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('223', '202010000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('223', '202020000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('223', '202030000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('223', '202040000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('223', '202050000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('223', '202060000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('223', '203000000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('223', '203010000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('223', '203020000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('223', '203030000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('223', '400000000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('223', '401000000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('223', '403000000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('223', '404000000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('223', '404010000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('223', '404030000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('223', '405000000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('224', '200000000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('224', '201000000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('224', '202000000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('224', '202010000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('224', '202020000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('224', '202030000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('224', '202040000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('224', '202050000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('224', '202060000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('224', '203000000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('224', '203010000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('224', '203020000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('224', '203030000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('224', '400000000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('224', '401000000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('224', '403000000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('224', '404000000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('224', '404010000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('224', '404030000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('224', '405000000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '103050300');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '104000000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '104010000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '104010100');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '104010101');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '104010102');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '104010103');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '104010200');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '104010300');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '104010301');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '104010302');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '104010303');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '104010400');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '104010401');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '104010402');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '104020000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '104020100');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '104020101');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '104020102');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '104020200');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '104020201');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '104020202');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '104020203');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '104020204');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '104020205');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '104020206');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '104020207');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '104020208');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '104020209');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '104020210');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '104020211');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '104020212');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '104020213');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '104020214');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '104020215');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '104020300');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '104020301');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '104020302');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '104020303');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '104020304');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '104030000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '104030100');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '104030200');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '104030300');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '104030400');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '104030500');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '104030600');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '105000000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '105020000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '105020001');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '105020002');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '105030000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '105030001');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '105030002');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '105030003');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '105030004');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '105030005');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '100000000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '102000000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '102010000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '102010100');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '102010101');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '102010102');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '102010103');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '102010104');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '102010105');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '102010106');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '102010107');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '102010108');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '102010109');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '102010110');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '102010111');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '102010112');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '102010113');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '102010114');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '102010115');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '102010116');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '102010117');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '102010118');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '102010119');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '102010120');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '102010121');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '102010122');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '102010123');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '102010124');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '102010125');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '102010126');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '102010127');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '102010128');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '102010200');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '102010201');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '102010202');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '102010300');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '102010301');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '102010302');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '102010303');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '102020000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '102020001');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '102020002');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '102020003');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '103000000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '103010000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '103010001');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '103010002');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '103010003');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '103010004');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '103010005');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '103010006');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '103020000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '103020001');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '103020002');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '103030000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '103030001');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '103030002');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '103030003');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '103040000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '103040001');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '103040002');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '103050000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '103050100');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('225', '103050200');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('226', '400000000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('226', '401000000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('226', '403000000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('226', '404000000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('226', '404010000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('226', '404030000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('226', '405000000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('227', '400000000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('227', '401000000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('227', '403000000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('227', '404000000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('227', '404010000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('227', '404030000');

insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('227', '405000000');

--初始化用户对应角色
insert into SYS_USER_ROLE select id, '225' from sys_user where login_name = 'reader_default';
commit;

--rollback DELETE FROM SYS_ROLE_MENU WHERE MENU_ID IN(SELECT ID FROM SYS_MENU WHERE APP_ID IN('1','2','4'));
--rollback DELETE FROM SYS_USER_ROLE WHERE USER_ID select id from sys_user where login_name = 'reader_default';
--rollback DELETE FROM OAUTH_CLIENT WHERE CLIENT_ID IN('lib3school_client','lib3industry_client','reader_client');
--rollback DELETE FROM SYS_APP WHERE ID IN('1','2','4');
--rollback DELETE FROM SYS_MENU WHERE APP_ID IN('1','2','4');
--rollback DELETE FROM SYS_USER WHERE ID IN('230');
--rollback DELETE FROM SYS_ROLE WHERE ID IN('226','227');

