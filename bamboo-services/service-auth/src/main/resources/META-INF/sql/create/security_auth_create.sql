/* 安全认证授权库创建脚本 */
DROP DATABASE IF EXISTS `security_auth`;
CREATE DATABASE IF NOT EXISTS `security_auth` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `security_auth`;

SET NAMES utf8mb4;
SET GLOBAL FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS `auth_account`;
CREATE TABLE `auth_account` (
                          `id` INT unsigned AUTO_INCREMENT NOT NULL COMMENT '主键ID',
                          `username` VARCHAR(100) NOT NULL COMMENT '账户名',
                          `password` VARCHAR(500) NOT NULL COMMENT '密码',
                          `secret_key` VARCHAR(100) NOT NULL COMMENT '密钥',
                          `credentials` VARCHAR(300) COMMENT '账户凭证',
                          `merchant_num` VARCHAR(100) COMMENT '商户号',
                          `account_type` VARCHAR(80) COMMENT '账户类型',
                          `account_rank` VARCHAR(10) DEFAULT '1' NOT NULL COMMENT '账户等级：1～n',
                          `email` VARCHAR(50) COMMENT '邮箱',
                          `enabled` BIT(1) DEFAULT 1 NOT NULL COMMENT '启用',
                          `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                          `update_time` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                          PRIMARY KEY (`id`) USING BTREE,
                          UNIQUE KEY `UQ_USERNAME` (`username`),
                          UNIQUE KEY `UQ_MERCHANT_NUM` (`merchant_num`)
) ENGINE=InnoDB AUTO_INCREMENT=1000 CHARACTER SET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='应用认证账户' ROW_FORMAT=Compact;


DROP TABLE IF EXISTS `auth_account_role`;
CREATE TABLE `auth_account_role` (
                               `account_id` INT unsigned NOT NULL COMMENT '账户表关联ID',
                               `role_id` INT unsigned NOT NULL COMMENT '角色表关联ID',
                               PRIMARY KEY (`account_id`, `role_id`)
) ENGINE=InnoDB CHARACTER SET=utf8 COLLATE=utf8_general_ci COMMENT='账户角色关系' ROW_FORMAT=Compact;


DROP TABLE IF EXISTS `auth_role`;
CREATE TABLE `auth_role` (
                          `id` INT unsigned AUTO_INCREMENT NOT NULL COMMENT '主键ID',
                          `role_name` VARCHAR(100) NOT NULL COMMENT '角色名',
                          `role_code` VARCHAR(100) NOT NULL COMMENT '角色编码',
                          `description` VARCHAR(200) COMMENT '描述',
                          `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                          `update_time` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                          PRIMARY KEY (`id`) USING BTREE,
                          UNIQUE KEY `UQ_ROLE_CODE` (`role_code`)
) ENGINE=InnoDB AUTO_INCREMENT=1000 CHARACTER SET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='应用认证角色' ROW_FORMAT=Compact;


DROP TABLE IF EXISTS `auth_role_permission`;
CREATE TABLE `auth_role_permission` (
                                     `role_id` INT unsigned NOT NULL COMMENT '角色表关联ID',
                                     `permission_id` INT unsigned NOT NULL COMMENT '权限表关联ID',
                                     PRIMARY KEY (`role_id`, `permission_id`)
) ENGINE=InnoDB CHARACTER SET=utf8 COLLATE=utf8_general_ci COMMENT='角色权限关系' ROW_FORMAT=Compact;


DROP TABLE IF EXISTS `auth_permission`;
CREATE TABLE `auth_permission` (
                                `id` INT unsigned AUTO_INCREMENT NOT NULL COMMENT '主键ID',
                                `permission_name` VARCHAR(64) NOT NULL COMMENT '权限名称',
                                `permission_code` VARCHAR(100) NOT NULL COMMENT '权限编码',
                                `resources_id` INT unsigned NOT NULL COMMENT '资源ID',
                                `resources_type` VARCHAR(20) NOT NULL COMMENT '资源类型',
                                `source` VARCHAR(50) COMMENT '权限来源',
                                `resources` VARCHAR(100) COMMENT '资源',
                                `scope` VARCHAR(100) COMMENT '权限范围',
                                `description` VARCHAR(200) COMMENT '描述',
                                PRIMARY KEY (`id`) USING BTREE,
                                UNIQUE KEY `UQ_PERMISSION_CODE` (`permission_code`)
) ENGINE=InnoDB AUTO_INCREMENT=1000 CHARACTER SET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='应用资源权限' ROW_FORMAT=Compact;

ALTER TABLE `auth_account_role` ADD CONSTRAINT fk_auth_account_role_user_id_user FOREIGN KEY (`account_id`) REFERENCES auth_account (`id`);
ALTER TABLE `auth_account_role` ADD CONSTRAINT fk_auth_account_role_role_id_role FOREIGN KEY (`role_id`) REFERENCES auth_role (`id`);

ALTER TABLE `auth_role_permission` ADD CONSTRAINT fk_auth_role_permission_permission_id_permission FOREIGN KEY (`permission_id`) REFERENCES auth_permission (`id`);
ALTER TABLE `auth_role_permission` ADD CONSTRAINT fk_auth_role_permission_role_id_role FOREIGN KEY (`role_id`) REFERENCES auth_role (`id`);


DROP TABLE IF EXISTS `app_server`;
CREATE TABLE `app_server` (
                           `id` INT unsigned AUTO_INCREMENT NOT NULL COMMENT '主键ID',
                           `app_name` VARCHAR(60) NOT NULL COMMENT '应用服务名称',
                           `app_code` VARCHAR(60) NOT NULL COMMENT '应用服务编码',
                           `caption` VARCHAR(200) COMMENT '标题',
                           `business` VARCHAR(200) COMMENT '业务',
                           `status` INT NOT NULL COMMENT '状态：0-停用；1-正常；2-维护中；4-故障',
                           `scope` VARCHAR(100) COMMENT '应用服务范围',
                           PRIMARY KEY (`id`) USING BTREE,
                           UNIQUE KEY `UQ_APP_CODE` (`app_code`)
) ENGINE=InnoDB AUTO_INCREMENT=1000 CHARACTER SET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='应用服务' ROW_FORMAT=Compact;
