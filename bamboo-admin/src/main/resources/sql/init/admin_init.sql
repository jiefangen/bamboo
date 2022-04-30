# 数据表初始化脚本, 默认明文密码123456
insert into SYS_USER (id, username, password, salt, sex, disabled, create_time) values
(2, 'admin', '9e1c263393c88252f0a379071bb0eef0', '8fc70e1a13231c5bf28cc8da404bdcd6', '男', 0, now()),
(3, 'system', '9e1c263393c88252f0a379071bb0eef0', '8fc70e1a13231c5bf28cc8da404bdcd6', '男', 0, now()),
(4, 'actuator', '9e1c263393c88252f0a379071bb0eef0', '8fc70e1a13231c5bf28cc8da404bdcd6', '女', 0, now()),
(5, '介繁根', '9e1c263393c88252f0a379071bb0eef0', '8fc70e1a13231c5bf28cc8da404bdcd6', '男', 0, now()),
(6, '李礼', '9e1c263393c88252f0a379071bb0eef0', '8fc70e1a13231c5bf28cc8da404bdcd6', '男', 0, now()),
(7, '介知礼', '9e1c263393c88252f0a379071bb0eef0', '8fc70e1a13231c5bf28cc8da404bdcd6', '女', 0, now());

insert into SYS_ROLE (id, role_name, role_code, description, create_time) values
(2, 'admin', 'ADMIN' ,'顶级管理员，有权查看本平台所有页面。', now()),
(3, 'system', 'SYSTEM', '系统管理员，有权查看系统管理相关页面。', now()),
(4, 'actuator', 'ACTUATOR' ,'监控管理员，有权查看系统监控相关页面。', now()),
(5, 'user', 'USER', '用户管理员，有权查看用户模块以及特定授权页面。', now()),
(6, 'general', 'GENERAL', '普通用户，有权查看特定授权页面。', now()),
(7, 'customer', 'CUSTOMER', '访客，只能查看无权限管控页面。', now());

insert into SYS_USER_ROLE (user_id, role_id) values
(2, 2),
(3, 3),
(4, 4),
(5, 5),
(6, 6),
(7, 7);

insert into SYS_MENU (id, parent_id, menu_path, redirect, menu_name, title, icon, component, hidden, sort) values
(2, 0, '/', '/dashboard', null, null, 'undefined', 'layout', 0, 0),
(3, 2, '/dashboard', null, 'router-dashboard', '首页', 'dashboard', 'dashboard', 0, 0),
(4, 0, '/system', '/system/user', 'router-系统管理', '系统管理', 'system', 'layout', 0, 1),
(5, 4, 'user', null, 'router-用户管理', '用户管理', 'peoples', 'sys_user', 0, 0),
(6, 4, 'role', null, 'router-角色管理', '角色管理', 'role', 'sys_role', 0, 1),
(7, 4, 'menu', null, 'router-菜单管理', '菜单管理', 'menu', 'sys_menu', 0, 2);

insert into SYS_ROLE_MENU (role_id, menu_id) values
(2, 2),(2, 3),(2, 4),(2, 5),(2, 6),(2, 7),
(3, 2),(3, 3),(3, 4),(3, 5),(3, 6),(3, 7),
(4, 2),(4, 3),
(5, 2),(5, 3),(5, 4),(5, 5),
(6, 2),(6, 3);

