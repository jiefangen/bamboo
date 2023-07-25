/* 数据表初始化脚本, 默认明文密码123456 */
insert into `sys_user` (`id`, `username`, `nickname`, `user_type`, `user_rank`, `password`, `sex`) values
(101, 'admin', 'Admin', 'manager', '0', '$2a$10$EMNrFn9zjcJZQf6z8WxG6OhdC5tQbk5a39hPG61aHkTGOVWacAWFi', '男'),
(102, 'system', 'System', 'manager', '0', '$2a$10$EMNrFn9zjcJZQf6z8WxG6OhdC5tQbk5a39hPG61aHkTGOVWacAWFi', '男'),
(103, 'actuator', 'Actuator', 'manager', '0', '$2a$10$EMNrFn9zjcJZQf6z8WxG6OhdC5tQbk5a39hPG61aHkTGOVWacAWFi', '女'),
(104, 'user', '木雨之舟', 'general', '1', '$2a$10$EMNrFn9zjcJZQf6z8WxG6OhdC5tQbk5a39hPG61aHkTGOVWacAWFi', '男'),
(105, '李礼', 'LiLi', 'general', '1', '$2a$10$EMNrFn9zjcJZQf6z8WxG6OhdC5tQbk5a39hPG61aHkTGOVWacAWFi', '女'),
(106, '介知礼', '知书达理', 'customer', '1', '$2a$10$EMNrFn9zjcJZQf6z8WxG6OhdC5tQbk5a39hPG61aHkTGOVWacAWFi', '女');

insert into `sys_role` (`id`, `role_name`, `role_code`, `description`) values
(101, 'admin', 'ADMIN' ,'顶级管理员，有权查看本平台所有页面。'),
(102, 'system', 'SYSTEM', '系统管理员，有权查看系统管理相关页面。'),
(103, 'actuator', 'ACTUATOR' ,'监控管理员，有权查看系统监控相关页面。'),
(104, 'fangen', 'USER', '一般用户，有权查看一般用户特定授权页面。'),
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
(101, 0, '/system', '/system/user', 'SystemManage', 'systemManage', 'system', 'layout', 0, 0),
(102, 101, 'user', null, 'UserManage', 'userManage', 'peoples', 'system_user', 0, 0),
(103, 101, 'role', null, 'RoleManage', 'roleManage', 'role', 'system_role', 0, 1),
(104, 101, 'menu', null, 'MenuManage', 'menuManage', 'menu', 'system_menu', 0, 2),
(105, 101, 'permission', null, 'PermissionManage', 'permissionManage', 'permissions', 'system_permission', 0, 3),
(106, 0, '/monitor', '/monitor/log', 'SystemMonitor', 'systemMonitor', 'monitor', 'layout', 0, 1),
(107, 106, 'online', null, 'OnlineUser', 'onlineUser', 'online', 'monitor_online', 0, 0),
(108, 106, 'log', null, 'OperationLog', 'operationLog', 'log', 'monitor_log', 0, 1),
(109, 106, 'api', null, 'ApiDoc', 'apiDoc', 'api', 'monitor_api', 0, 2),
(110, 106, 'druid', null, 'DruidMonitor', 'druidMonitor', 'druid', 'monitor_druid', 0, 3),

(111, 0, '/settings', '/settings/parameter', 'SystemSettings', 'systemSettings', 'settings', 'layout', 0, 2),
(112, 111, 'parameter', null, 'ParamSettings', 'paramSettings', 'parameter', 'settings_parameter', 0, 0),
(113, 111, 'dictionary', null, 'DicSettings', 'dicSettings', 'dictionary', 'settings_dictionary', 0, 1);

insert into `sys_role_menu` (`role_id`, `menu_id`) values
(101, 101),(101, 102),(101, 103),(101, 104),(101, 105),(101, 106),(101, 107),(101, 108),(101, 109),(101, 110),
(101, 111),(101, 112),(101, 113),
(102, 101),(102, 102),(102, 103),(102, 104),(102, 105),(102, 111),(102, 112),(102, 113),
(103, 106),(103, 107),(103, 108),(103, 109),(103, 110),
(104, 101),(104, 102);