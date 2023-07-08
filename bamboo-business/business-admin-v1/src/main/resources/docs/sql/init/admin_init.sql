/* 数据表初始化脚本, 默认明文密码123456 */
insert into `sys_user` (`id`, `username`, `nickname`, `user_type`, `user_rank`, `password`, `sex`) values
(101, 'admin', 'Admin', 'manager', '0', '$2a$10$EMNrFn9zjcJZQf6z8WxG6OhdC5tQbk5a39hPG61aHkTGOVWacAWFi', '男'),
(102, 'system', 'System', 'manager', '0', '$2a$10$EMNrFn9zjcJZQf6z8WxG6OhdC5tQbk5a39hPG61aHkTGOVWacAWFi', '男'),
(103, 'actuator', 'Actuator', 'manager', '0', '$2a$10$EMNrFn9zjcJZQf6z8WxG6OhdC5tQbk5a39hPG61aHkTGOVWacAWFi', '女'),
(104, '介繁根', '木雨之舟', 'system', '1', '$2a$10$EMNrFn9zjcJZQf6z8WxG6OhdC5tQbk5a39hPG61aHkTGOVWacAWFi', '男'),
(105, '李礼', 'LiLi', 'general', '1', '$2a$10$EMNrFn9zjcJZQf6z8WxG6OhdC5tQbk5a39hPG61aHkTGOVWacAWFi', '女'),
(106, '介知礼', '知书达理', 'customer', '1', '$2a$10$EMNrFn9zjcJZQf6z8WxG6OhdC5tQbk5a39hPG61aHkTGOVWacAWFi', '女');

insert into `sys_role` (`id`, `role_name`, `role_code`, `description`) values
(101, 'admin', 'ADMIN' ,'顶级管理员，有权查看本平台所有页面。'),
(102, 'system', 'SYSTEM', '系统管理员，有权查看系统管理相关页面。'),
(103, 'actuator', 'ACTUATOR' ,'监控管理员，有权查看系统监控相关页面。'),
(104, 'user', 'USER', '用户管理员，有权查看用户模块以及特定授权页面。'),
(105, 'general', 'GENERAL', '普通用户，有权查看特定授权页面。'),
(106, 'customer', 'CUSTOMER', '访客，只能查看无权限管控页面。');

insert into `sys_user_role` (`user_id`, `role_id`) values
(101, 101),
(102, 102),
(103, 103),
(104, 104),
(105, 105),
(106, 106);

insert into `sys_menu` (`id`, `parent_id`, `menu_path`, `redirect`, `menu_name`, `title`, `icon`, `component`, `hidden`, `sort`) values
(102, 0, '/system', '/system/user', 'router-系统管理', 'systemManage', 'system', 'layout', 0, 0),
(103, 102, 'user', null, 'router-用户管理', 'userManage', 'peoples', 'sys_user', 0, 0),
(104, 102, 'role', null, 'router-角色管理', 'roleManage', 'role', 'sys_role', 0, 1),
(105, 102, 'menu', null, 'router-菜单管理', 'menuManage', 'menu', 'sys_menu', 0, 2),
(106, 0, '/monitor', '/monitor/log', 'router-系统监控', 'systemMonitor', 'monitor', 'layout', 0, 1),
(107, 106, 'log', null, 'router-操作日志', 'operationLog', 'log', 'monitor_log', 0, 0),
(108, 106, 'api', null, 'router-API文档', 'apiDoc', 'api', 'monitor_api', 0, 1),
(109, 106, 'druid', null, 'router-Druid监控', 'DruidMonitor', 'druid', 'monitor_druid', 0, 2);

insert into `sys_role_menu` (`role_id`, `menu_id`) values
(101, 102),(101, 103),(101, 104),(101, 105),(101, 106),(101, 107),(101, 108),(101, 109),
(102, 102),(102, 103),(102, 104),(102, 105),
(103, 106),(103, 107),(103, 108),(103, 109),
(104, 102),(104, 103);