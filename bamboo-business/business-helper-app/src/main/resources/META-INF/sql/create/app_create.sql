/* APP系统库创建脚本 */
DROP DATABASE IF EXISTS `app_system`;
CREATE DATABASE  IF NOT EXISTS `app_system` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `app_system`;

SET NAMES utf8mb4;
SET GLOBAL FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS `app_user`;
CREATE TABLE `app_user` (
    `id` INT unsigned AUTO_INCREMENT NOT NULL COMMENT '主键ID',
    `username` VARCHAR(50) NOT NULL COMMENT '用户名',
    `password` VARCHAR(50) COMMENT '密码',
    `user_rank` INT default 1 NOT NULL COMMENT '用户级别：1～n',
    `salt` VARCHAR(50) COMMENT '随机盐',
    `nickname` VARCHAR(80) COMMENT '昵称',
    `phone` VARCHAR(20) COMMENT '手机号',
    `email` VARCHAR(50) COMMENT '邮箱',
    `gender` TINYINT(1) COMMENT '性别：0-女；1-男',
    `avatar` VARCHAR(500) COMMENT '头像',
    `openid` VARCHAR(255) COMMENT '微信openid',
    `status` TINYINT(1) NOT NULL COMMENT '状态：0-虚拟；1-正常；4-删除；9-锁定',
    `enabled` BIT(1) NOT NULL DEFAULT 1 COMMENT '启用',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `UQ_USERNAME` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=1000 CHARACTER SET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='APP用户' ROW_FORMAT=Compact;


DROP TABLE IF EXISTS `app_user_token`;
CREATE TABLE `app_user_token` (
    `id` BIGINT unsigned AUTO_INCREMENT NOT NULL COMMENT '主键ID',
    `user_id` INT unsigned NOT NULL COMMENT '关联用户ID',
    `identity` VARCHAR(500) COMMENT '身份标识',
    `token` VARCHAR(4000) NOT NULL COMMENT '交互凭证',
    `status` TINYINT(1) DEFAULT 1 NOT NULL COMMENT '状态：1-在线；2-离线；3-失效；4-登出',
    `expired_interval` INT COMMENT '失效时间间隔（单位秒）',
    `expiration_time` DATETIME COMMENT '失效时间',
    `logout_time` DATETIME COMMENT '登出时间',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建/登录时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 CHARACTER SET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='APP用户token' ROW_FORMAT=Compact;


DROP TABLE IF EXISTS `app_action_log`;
CREATE TABLE `app_action_log` (
    `id` BIGINT unsigned AUTO_INCREMENT NOT NULL COMMENT '主键ID',
    `identity` VARCHAR(500) NOT NULL COMMENT '身份标识',
    `action_type` VARCHAR(20) NOT NULL COMMENT '操作类型',
    `content` VARCHAR(500) COMMENT '操作内容',
    `host` VARCHAR(50) COMMENT '主机地址',
    `ip_address` VARCHAR(100) COMMENT 'IP归属地址',
    `operating_time` DATETIME COMMENT '操作时间',
    `elapsed_time` BIGINT COMMENT '耗时',
    `status_code` INT COMMENT '操作结果状态',
    `source_id` VARCHAR(100) COMMENT '来源ID',
    `terminal_device` VARCHAR(100) COMMENT '终端设备',
    `terminal_os` VARCHAR(100) COMMENT '终端操作系统',
    `exception_info` VARCHAR(4000) COMMENT '异常信息',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 CHARACTER SET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='APP操作日志' ROW_FORMAT=Compact;

