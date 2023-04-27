# 数据表默认初始化脚本, 默认明文密码123456
insert into SYS_USER (id, username, password, salt, sex, disabled, create_time, update_time) values
(102, 'admin', '9e1c263393c88252f0a379071bb0eef0', '8fc70e1a13231c5bf28cc8da404bdcd6', '男', 0, now(), now()),
(103, 'system', '9e1c263393c88252f0a379071bb0eef0', '8fc70e1a13231c5bf28cc8da404bdcd6', '男', 0, now(), now()),
(104, 'actuator', '9e1c263393c88252f0a379071bb0eef0', '8fc70e1a13231c5bf28cc8da404bdcd6', '女', 0, now(), now()),
(105, '介繁根', '9e1c263393c88252f0a379071bb0eef0', '8fc70e1a13231c5bf28cc8da404bdcd6', '男', 0, now(), now()),
(106, '李礼', '9e1c263393c88252f0a379071bb0eef0', '8fc70e1a13231c5bf28cc8da404bdcd6', '男', 0, now(), now()),
(107, '介知礼', '9e1c263393c88252f0a379071bb0eef0', '8fc70e1a13231c5bf28cc8da404bdcd6', '女', 0, now(), now());

insert into SYS_ROLE (id, role_name, role_code, description, create_time, update_time) values
(102, 'admin', 'ADMIN' ,'顶级管理员，有权查看本平台所有页面。', now(), now()),
(103, 'system', 'SYSTEM', '系统管理员，有权查看系统管理相关页面。', now(), now()),
(104, 'actuator', 'ACTUATOR' ,'监控管理员，有权查看系统监控相关页面。', now(), now()),
(105, 'user', 'USER', '用户管理员，有权查看用户模块以及特定授权页面。', now(), now()),
(106, 'general', 'GENERAL', '普通用户，有权查看特定授权页面。', now(), now()),
(107, 'customer', 'CUSTOMER', '访客，只能查看无权限管控页面。', now(), now());

insert into SYS_USER_ROLE (user_id, role_id) values
(102, 102),
(103, 103),
(104, 104),
(105, 105),
(106, 106),
(107, 107);

insert into SYS_MENU (id, parent_id, menu_path, redirect, menu_name, title, icon, component, hidden, sort) values
(102, 0, '/system', '/system/user', 'router-系统管理', '系统管理', 'system', 'layout', 0, 0),
(103, 102, 'user', null, 'router-用户管理', '用户管理', 'peoples', 'sys_user', 0, 0),
(104, 102, 'role', null, 'router-角色管理', '角色管理', 'role', 'sys_role', 0, 1),
(105, 102, 'menu', null, 'router-菜单管理', '菜单管理', 'menu', 'sys_menu', 0, 2),
(106, 0, '/monitor', '/monitor/log', 'router-系统监控', '系统监控', 'monitor', 'layout', 0, 1),
(107, 106, 'log', null, 'router-操作日志', '操作日志', 'log', 'monitor_log', 0, 0),
(108, 106, 'api', null, 'router-API文档', 'API文档', 'api', 'monitor_api', 0, 1),
(109, 106, 'druid', null, 'router-Druid监控', 'Druid监控', 'druid', 'monitor_druid', 0, 2);

insert into SYS_ROLE_MENU (role_id, menu_id) values
(102, 102),(102, 103),(102, 104),(102, 105),(102, 106),(102, 107),(102, 108),(102, 109),
(103, 102),(103, 103),(103, 104),(103, 105),
(104, 106),(104, 107),(104, 108),(104, 109),
(105, 102),(105, 103);
