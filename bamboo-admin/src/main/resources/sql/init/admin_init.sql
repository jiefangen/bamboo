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
(2, 3),
(3, 3),
(4, 4);

insert into SYS_MENU (id, parent_id, menu_path, redirect, menu_name, meta, component, hidden, sort) values
(2, null, '/login', null, null, null, '/views/login/index', true, 0),
(3, null, '/', '/dashboard', null, null, 'layout/Layout', null, 1),
(4, 3, '/dashboard', null, 'dashboard', '{ title: "首页", icon: "dashboard" }', '/views/dashboard/index', null, 0),
(5, null, '/system', '/system/user', '系统管理', '{ title: "系统管理", icon: "system" }', 'layout/Layout', null, 2),
(6, 5, '/user', null, '用户管理', '{ title: "用户管理", icon: "peoples" }', '/views/system/user/index', null, 0),
(7, 5, '/role', null, '角色管理', '{ title: "角色管理", icon: "role" }', '/views/system/role/index', null, 1),
(8, 5, '/menu', null, '菜单管理', '{ title: "用户管理", icon: "menu" }', '/views/system/menu/index', null, 2);

insert into SYS_ROLE_MENU (role_id, menu_id) values
(2, 2),
(2, 3),
(2, 4),
(2, 5),
(2, 6),
(2, 7),
(2, 8),
(3, 2),
(3, 3),
(3, 4),
(3, 5),
(3, 6),
(3, 7),
(3, 8);
