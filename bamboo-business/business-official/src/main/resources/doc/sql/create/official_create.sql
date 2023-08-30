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
                          PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1000 CHARACTER SET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='系统角色' ROW_FORMAT=Compact;


DROP TABLE IF EXISTS `sys_role_permission`;
CREATE TABLE sys_role_permission (
                                     `role_id` INT unsigned NOT NULL COMMENT '角色表关联ID',
                                     `permission_id` INT unsigned NOT NULL COMMENT '权限表关联ID',
                                     PRIMARY KEY (`role_id`, `permission_id`)
) ENGINE=InnoDB CHARACTER SET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='系统角色权限关系' ROW_FORMAT=Compact;


DROP TABLE IF EXISTS `sys_permission`;
CREATE TABLE sys_permission (
                                `id` INT unsigned AUTO_INCREMENT NOT NULL COMMENT '主键ID',
                                `permission_name` VARCHAR(100) NOT NULL COMMENT '权限名称',
                                `permission_code` VARCHAR(20) NOT NULL COMMENT '权限编码',
                                `description` VARCHAR(200) COMMENT '描述',
                                `resources_type` VARCHAR(20) NOT NULL COMMENT '资源类型',
                                `operation_scope` VARCHAR(20) COMMENT '操作范围',
                                PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1000 CHARACTER SET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='系统权限' ROW_FORMAT=Compact;


-- ALTER TABLE `sys_user_role` ADD CONSTRAINT fk_sys_user_role_user_id_user FOREIGN KEY (`user_id`) REFERENCES sys_user (`id`);
-- ALTER TABLE `sys_user_role` ADD CONSTRAINT fk_sys_user_role_role_id_role FOREIGN KEY (`role_id`) REFERENCES sys_role (`id`);

-- ALTER TABLE `sys_role_permission` ADD CONSTRAINT fk_sys_role_permission_permission_id_permission FOREIGN KEY (`permission_id`) REFERENCES sys_permission (`id`);
-- ALTER TABLE `sys_role_permission` ADD CONSTRAINT fk_sys_role_permission_role_id_role FOREIGN KEY (`role_id`) REFERENCES sys_role (`id`);


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
) ENGINE=InnoDB AUTO_INCREMENT=1 CHARACTER SET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='系统操作日志' ROW_FORMAT=Compact;
