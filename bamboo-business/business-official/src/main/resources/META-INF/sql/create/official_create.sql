/* 官网系统库创建脚本 */
DROP DATABASE IF EXISTS `official_system`;
CREATE DATABASE  IF NOT EXISTS `official_system` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `official_system`;

SET NAMES utf8mb4;
SET GLOBAL FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
                            `id` INT unsigned AUTO_INCREMENT NOT NULL COMMENT '主键ID',
                            `username` VARCHAR(100) NOT NULL COMMENT '用户名',
                            `password` VARCHAR(500) NOT NULL COMMENT '密码',
                            `user_type` VARCHAR(80) NOT NULL COMMENT '用户类型：manager-管理员；general-普通用户；customer-访客/游客',
                            `user_rank` VARCHAR(10) DEFAULT '1' NOT NULL COMMENT '用户级别：0-管理员专用级别；1～n-其它类型级别',
                            `phone` VARCHAR(20) COMMENT '手机号',
                            `nickname` VARCHAR(200) COMMENT '昵称',
                            `email` VARCHAR(50) COMMENT '邮箱',
                            `sex` CHAR(1) COMMENT '性别',
                            `enabled` BIT(1) DEFAULT 1 NOT NULL COMMENT '启用',
                            `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                            `update_time` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                            PRIMARY KEY (`id`) USING BTREE,
                            UNIQUE KEY `UQ_USERNAME` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=1000 CHARACTER SET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='系统用户' ROW_FORMAT=Compact;


DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
                                 `user_id` INT unsigned NOT NULL COMMENT '用户表关联ID',
                                 `role_id` INT unsigned NOT NULL COMMENT '角色表关联ID',
                                 PRIMARY KEY (`user_id`, `role_id`)
) ENGINE=InnoDB CHARACTER SET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='系统用户角色关系' ROW_FORMAT=Compact;


DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
                            `id` INT unsigned AUTO_INCREMENT NOT NULL COMMENT '主键ID',
                            `role_name` VARCHAR(100) NOT NULL COMMENT '角色名',
                            `role_code` VARCHAR(20) NOT NULL COMMENT '角色编码',
                            `description` VARCHAR(200) COMMENT '描述',
                            `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                            `update_time` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                            PRIMARY KEY (`id`) USING BTREE,
                            UNIQUE KEY `UQ_ROLE_CODE` (`role_code`)
) ENGINE=InnoDB AUTO_INCREMENT=1000 CHARACTER SET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='系统角色' ROW_FORMAT=Compact;
