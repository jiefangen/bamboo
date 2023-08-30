/* 权限认证库创建脚本 */
CREATE DATABASE  IF NOT EXISTS `authorization` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `authorization`;

SET NAMES utf8mb4;
SET GLOBAL FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS `auth_user`;
CREATE TABLE `auth_user` (
                          `id` INT unsigned AUTO_INCREMENT NOT NULL COMMENT '主键ID',
                          `username` VARCHAR(100) NOT NULL COMMENT '用户名',
                          `password` VARCHAR(500) NOT NULL COMMENT '密码',
                          `user_type` VARCHAR(80) DEFAULT 'customer' NOT NULL COMMENT '用户类型：manager-管理员；system-系统用户；general-普通用户；customer-访客/游客',
                          `user_rank` VARCHAR(10) DEFAULT '1' NOT NULL COMMENT '用户级别：0-管理员专用级别；1～n-其它类型级别',
                          `phone` VARCHAR(20) COMMENT '手机号',
                          `nickname` VARCHAR(200) COMMENT '昵称',
                          `email` VARCHAR(50) COMMENT '邮箱',
                          `sex` CHAR(1) COMMENT '性别',
                          `enabled` BIT(1) DEFAULT 1 NOT NULL COMMENT '启用',
                          `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                          `update_time` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                          PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1000 CHARACTER SET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='系统用户' ROW_FORMAT=Compact;


DROP TABLE IF EXISTS `auth_user_role`;
CREATE TABLE `auth_user_role` (
                               `user_id` INT unsigned NOT NULL COMMENT '用户表关联ID',
                               `role_id` INT unsigned NOT NULL COMMENT '角色表关联ID',
                               PRIMARY KEY (`user_id`, `role_id`)
) ENGINE=InnoDB CHARACTER SET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='系统用户角色关系' ROW_FORMAT=Compact;


DROP TABLE IF EXISTS `auth_role`;
CREATE TABLE `auth_role` (
                          `id` INT unsigned AUTO_INCREMENT NOT NULL COMMENT '主键ID',
                          `role_name` VARCHAR(100) NOT NULL COMMENT '角色名',
                          `role_code` VARCHAR(20) NOT NULL COMMENT '角色编码',
                          `description` VARCHAR(200) COMMENT '描述',
                          `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                          `update_time` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                          PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1000 CHARACTER SET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='系统角色' ROW_FORMAT=Compact;


DROP TABLE IF EXISTS `auth_role_permission`;
CREATE TABLE auth_role_permission (
                                     `role_id` INT unsigned NOT NULL COMMENT '角色表关联ID',
                                     `permission_id` INT unsigned NOT NULL COMMENT '权限表关联ID',
                                     PRIMARY KEY (`role_id`, `permission_id`)
) ENGINE=InnoDB CHARACTER SET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='系统角色权限关系' ROW_FORMAT=Compact;


DROP TABLE IF EXISTS `auth_permission`;
CREATE TABLE auth_permission (
                                `id` INT unsigned AUTO_INCREMENT NOT NULL COMMENT '主键ID',
                                `permission_name` VARCHAR(64) NOT NULL COMMENT '权限名称',
                                `permission_code` VARCHAR(20) NOT NULL COMMENT '权限编码',
                                `description` VARCHAR(200) COMMENT '描述',
                                `resources_id` INT unsigned NOT NULL COMMENT '资源ID',
                                `resources_type` VARCHAR(20) NOT NULL COMMENT '资源类型',
                                `operation_scope` VARCHAR(20) COMMENT '操作范围',
                                PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1000 CHARACTER SET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='系统权限' ROW_FORMAT=Compact;

ALTER TABLE `auth_user_role` ADD CONSTRAINT fk_auth_user_role_user_id_user FOREIGN KEY (`user_id`) REFERENCES auth_user (`id`);
ALTER TABLE `auth_user_role` ADD CONSTRAINT fk_auth_user_role_role_id_role FOREIGN KEY (`role_id`) REFERENCES auth_role (`id`);

ALTER TABLE `auth_role_permission` ADD CONSTRAINT fk_auth_role_permission_permission_id_permission FOREIGN KEY (`permission_id`) REFERENCES auth_permission (`id`);
ALTER TABLE `auth_role_permission` ADD CONSTRAINT fk_auth_role_permission_role_id_role FOREIGN KEY (`role_id`) REFERENCES auth_role (`id`);
