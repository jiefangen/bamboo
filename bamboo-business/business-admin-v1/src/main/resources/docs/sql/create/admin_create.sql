/* 后台管理系统库创建脚本 */
CREATE DATABASE  IF NOT EXISTS `admin_system` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `admin_system`;

SET NAMES utf8mb4;
SET GLOBAL FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
                          `id` INT unsigned AUTO_INCREMENT NOT NULL COMMENT '主键ID',
                          `username` VARCHAR(100) NOT NULL COMMENT '用户名',
                          `password` VARCHAR(500) NOT NULL COMMENT '密码',
                          `user_type` VARCHAR(80) DEFAULT 'customer' NOT NULL COMMENT '用户类型：manager-管理员；system-系统用户；general-普通用户；customer-访客/游客',
                          `user_rank` VARCHAR(10) DEFAULT '1' NOT NULL COMMENT '用户级别：0-管理员专用级别；1～n-其它类型级别',
                          `phone` VARCHAR(20) COMMENT '手机号',
                          `nickname` VARCHAR(200) COMMENT '昵称',
                          `email` VARCHAR(50) COMMENT '邮箱',
                          `sex` CHAR(1) COMMENT '性别',
                          `enabled` TINYINT DEFAULT 1 NOT NULL COMMENT '启用',
                          `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                          `update_time` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                          PRIMARY KEY (`id`) USING BTREE,
                          UNIQUE KEY `UQ_USERNAME` (`username`)
) ENGINE = InnoDB AUTO_INCREMENT=1000 CHARACTER SET=utf8 COLLATE=utf8_general_ci COMMENT='系统用户' ROW_FORMAT=Compact;


DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
                               `user_id` INT unsigned NOT NULL COMMENT '用户表关联ID',
                               `role_id` INT unsigned NOT NULL COMMENT '角色表关联ID',
                               PRIMARY KEY (`user_id`, `role_id`)
) ENGINE = InnoDB CHARACTER SET=utf8 COLLATE=utf8_general_ci COMMENT='系统用户角色关系' ROW_FORMAT=Compact;


DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
                          `id` INT unsigned AUTO_INCREMENT NOT NULL COMMENT '主键ID',
                          `role_name` VARCHAR(100) NOT NULL COMMENT '角色名',
                          `role_code` VARCHAR(20) NOT NULL COMMENT '角色编码',
                          `description` VARCHAR(200) COMMENT '描述',
                          `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                          `update_time` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                          PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT=1000 CHARACTER SET=utf8 COLLATE=utf8_general_ci COMMENT='系统角色' ROW_FORMAT=Compact;


DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu` (
                               `role_id` INT unsigned NOT NULL COMMENT '角色表关联ID',
                               `menu_id` INT unsigned NOT NULL COMMENT '菜单表关联ID',
                               PRIMARY KEY (`role_id`, `menu_id`)
) ENGINE=InnoDB CHARACTER SET=utf8 COLLATE=utf8_general_ci COMMENT='系统角色菜单关系' ROW_FORMAT=Compact;


DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
                            `id` INT unsigned AUTO_INCREMENT NOT NULL COMMENT '主键ID',
                            `parent_id` INT unsigned DEFAULT 0 NOT NULL COMMENT '父级ID（0代表根目录）',
                            `menu_path` VARCHAR(255) NOT NULL COMMENT '菜单路径',
                            `component` VARCHAR(50) NOT NULL COMMENT '菜单组件',
                            `menu_name` VARCHAR(100) COMMENT '菜单名',
                            `redirect` VARCHAR(255) COMMENT '跳转路径',
                            `title` VARCHAR(100) COMMENT '标题',
                            `icon` VARCHAR(100) COMMENT '图标',
                            `hidden` TINYINT(1) default(0) COMMENT '隐藏',
                            `sort` INT default(0) COMMENT '排序',
                            PRIMARY KEY (`id`) USING BTREE,
                            KEY `IDX_PARENT_ID` (`parent_id`)
) ENGINE = InnoDB AUTO_INCREMENT=1000 CHARACTER SET=utf8 COLLATE=utf8_general_ci COMMENT='系统菜单' ROW_FORMAT=Compact;


DROP TABLE IF EXISTS `sys_role_permission`;
CREATE TABLE sys_role_permission (
                                     `id` INT unsigned AUTO_INCREMENT NOT NULL COMMENT '主键ID',
                                     `role_id` INT unsigned NOT NULL COMMENT '角色表关联ID',
                                     `permission_id` INT unsigned NOT NULL COMMENT '权限表关联ID',
                                     `association` VARCHAR(50) COMMENT '关联方式',
                                     PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1000 CHARACTER SET=utf8 COLLATE=utf8_general_ci COMMENT='系统角色权限关系' ROW_FORMAT=Compact;


DROP TABLE IF EXISTS `sys_permission`;
CREATE TABLE `sys_permission` (
                                `id` INT unsigned AUTO_INCREMENT NOT NULL COMMENT '主键ID',
                                `permission_name` VARCHAR(200) NOT NULL COMMENT '权限名称',
                                `permission_code` VARCHAR(100) NOT NULL COMMENT '权限编码',
                                `description` VARCHAR(1000) COMMENT '描述',
                                `resources_id` VARCHAR(1000) COMMENT '资源ID',
                                `resources_type` VARCHAR(50) NOT NULL COMMENT '资源类型',
                                `source` VARCHAR(50) COMMENT '权限来源',
                                `operation_scope` VARCHAR(400) COMMENT '操作范围',
                                PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT=1000 CHARACTER SET=utf8 COLLATE=utf8_general_ci COMMENT='系统权限' ROW_FORMAT=Compact;


ALTER TABLE `sys_user_role` ADD CONSTRAINT FK_USER_ROLE_USER_ID_USER FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`id`);
ALTER TABLE `sys_user_role` ADD CONSTRAINT FK_USER_ROLE_ROLE_ID_ROLE FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`id`);

ALTER TABLE `sys_role_menu` ADD CONSTRAINT FK_ROLE_MENU_ROLE_ID_ROLE FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`id`);
ALTER TABLE `sys_role_menu` ADD CONSTRAINT FK_ROLE_MENU_MENU_ID_MENU FOREIGN KEY (`menu_id`) REFERENCES `sys_menu` (`id`);


DROP TABLE IF EXISTS `sys_action_log`;
CREATE TABLE `sys_action_log` (
                                `id` BIGINT unsigned AUTO_INCREMENT NOT NULL COMMENT '主键ID',
                                `action_type` INT NOT NULL COMMENT '操作类型',
                                `content` VARCHAR(200) COMMENT '操作内容',
                                `ip_address` VARCHAR(20) COMMENT 'IP地址',
                                `username` VARCHAR(100) COMMENT '用户名',
                                `operating_time` DATETIME COMMENT '操作时间',
                                `elapsed_time` INT COMMENT '耗时',
                                `status_code` INT COMMENT '操作结果状态',
                                `exception_info` VARCHAR(4000) COMMENT '异常信息',
                                PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT=1 CHARACTER SET=utf8 COLLATE=utf8_general_ci COMMENT='系统操作日志' ROW_FORMAT=Compact;
