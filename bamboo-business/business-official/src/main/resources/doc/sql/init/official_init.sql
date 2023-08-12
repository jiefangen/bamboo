/* 数据表初始化脚本, 默认明文密码123456 */
insert into `sys_user` (`id`, `username`, `nickname`, `user_type`, `user_rank`, `password`, `sex`) values
(101, 'admin', 'Admin', 'manager', '0', '$2a$10$EMNrFn9zjcJZQf6z8WxG6OhdC5tQbk5a39hPG61aHkTGOVWacAWFi', '男'),
(102, 'system', 'System', 'manager', '0', '$2a$10$EMNrFn9zjcJZQf6z8WxG6OhdC5tQbk5a39hPG61aHkTGOVWacAWFi', '男'),
(103, 'actuator', 'Actuator', 'manager', '0', '$2a$10$EMNrFn9zjcJZQf6z8WxG6OhdC5tQbk5a39hPG61aHkTGOVWacAWFi', '女'),
(104, 'user', '木雨之舟', 'system', '1', '$2a$10$EMNrFn9zjcJZQf6z8WxG6OhdC5tQbk5a39hPG61aHkTGOVWacAWFi', '男'),
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
(101, 101),(101, 102),(101, 103),
(102, 102),(102, 103),
(103, 103),
(104, 104),
(105, 105),
(106, 106);
