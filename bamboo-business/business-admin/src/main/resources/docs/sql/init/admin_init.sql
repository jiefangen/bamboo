# 数据表初始化脚本, 默认明文密码123456
insert into SYS_USER (id, username, password, salt, sex, disabled, create_time, update_time) values
(1002, 'admin', '9e1c263393c88252f0a379071bb0eef0', '8fc70e1a13231c5bf28cc8da404bdcd6', '男', 0, now(), now()),
(1003, 'system', '9e1c263393c88252f0a379071bb0eef0', '8fc70e1a13231c5bf28cc8da404bdcd6', '男', 0, now(), now()),
(1004, 'actuator', '9e1c263393c88252f0a379071bb0eef0', '8fc70e1a13231c5bf28cc8da404bdcd6', '女', 0, now(), now()),
(1005, '介繁根', '9e1c263393c88252f0a379071bb0eef0', '8fc70e1a13231c5bf28cc8da404bdcd6', '男', 0, now(), now()),
(1006, '李礼', '9e1c263393c88252f0a379071bb0eef0', '8fc70e1a13231c5bf28cc8da404bdcd6', '男', 0, now(), now()),
(1007, '介知礼', '9e1c263393c88252f0a379071bb0eef0', '8fc70e1a13231c5bf28cc8da404bdcd6', '女', 0, now(), now());

insert into SYS_ROLE (id, role_name, role_code, description, create_time, update_time) values
(1002, 'admin', 'ADMIN' ,'顶级管理员，有权查看本平台所有页面。', now(), now()),
(1003, 'system', 'SYSTEM', '系统管理员，有权查看系统管理相关页面。', now(), now()),
(1004, 'actuator', 'ACTUATOR' ,'监控管理员，有权查看系统监控相关页面。', now(), now()),
(1005, 'user', 'USER', '用户管理员，有权查看用户模块以及特定授权页面。', now(), now()),
(1006, 'general', 'GENERAL', '普通用户，有权查看特定授权页面。', now(), now()),
(1007, 'customer', 'CUSTOMER', '访客，只能查看无权限管控页面。', now(), now());

insert into SYS_USER_ROLE (user_id, role_id) values
(1002, 1002),
(1003, 1003),
(1004, 1004),
(1005, 1005),
(1006, 1006),
(1007, 1007);

insert into SYS_MENU (id, parent_id, menu_path, redirect, menu_name, title, icon, component, hidden, sort) values
(1002, 0, '/system', '/system/user', 'router-系统管理', '系统管理', 'system', 'layout', 0, 0),
(1003, 1002, 'user', null, 'router-用户管理', '用户管理', 'peoples', 'sys_user', 0, 0),
(1004, 1002, 'role', null, 'router-角色管理', '角色管理', 'role', 'sys_role', 0, 1),
(1005, 1002, 'menu', null, 'router-菜单管理', '菜单管理', 'menu', 'sys_menu', 0, 2),
(1006, 0, '/monitor', '/monitor/log', 'router-系统监控', '系统监控', 'monitor', 'layout', 0, 1),
(1007, 1006, 'log', null, 'router-操作日志', '操作日志', 'log', 'monitor_log', 0, 0),
(1008, 1006, 'api', null, 'router-API文档', 'API文档', 'api', 'monitor_api', 0, 1),
(1009, 1006, 'druid', null, 'router-Druid监控', 'Druid监控', 'druid', 'monitor_druid', 0, 2),

(1010, 0, '/relax', '/relax/sweep', 'router-娱乐放松', '娱乐放松', 'relax', 'layout', 0, 2),
(1011, 1010, 'sweep', null, 'router-扫雷', '扫雷', 'sweep', 'relax_sweep', 0, 0),
(1012, 1010, 'card', null, 'router-卡牌', '卡牌', 'card', 'relax_card', 0, 1);

insert into SYS_ROLE_MENU (role_id, menu_id) values
(1002, 1002),(1002, 1003),(1002, 1004),(1002, 1005),(1002, 1006),(1002, 1007),(1002, 1008),(1002, 1009),
(1003, 1002),(1003, 1003),(1003, 1004),(1003, 1005),
(1004, 1006),(1004, 1007),(1004, 1008),(1004, 1009),
(1005, 1002),(1005, 1003),

(1006, 1010),(1006, 1011),(1006, 1012);
