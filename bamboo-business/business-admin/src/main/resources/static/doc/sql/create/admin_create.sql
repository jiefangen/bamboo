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
                          PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1000 CHARACTER SET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='系统角色' ROW_FORMAT=Compact;


DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu` (
                               `role_id` INT unsigned NOT NULL COMMENT '角色表关联ID',
                               `menu_id` INT unsigned NOT NULL COMMENT '菜单表关联ID',
                               PRIMARY KEY (`role_id`, `menu_id`)
) ENGINE=InnoDB CHARACTER SET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='系统角色菜单关系' ROW_FORMAT=Compact;


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
                            `hidden` BIT(1) default(0) COMMENT '隐藏',
                            `sort` INT default(0) COMMENT '排序',
                            PRIMARY KEY (`id`) USING BTREE,
                            KEY `IDX_PARENT_ID` (`parent_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1000 CHARACTER SET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='系统菜单' ROW_FORMAT=Compact;


DROP TABLE IF EXISTS `sys_role_permission`;
CREATE TABLE sys_role_permission (
                                     `id` INT unsigned AUTO_INCREMENT NOT NULL COMMENT '主键ID',
                                     `role_id` INT unsigned NOT NULL COMMENT '角色表关联ID',
                                     `permission_id` INT unsigned NOT NULL COMMENT '权限表关联ID',
                                     `association` VARCHAR(50) COMMENT '关联方式',
                                     PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1000 CHARACTER SET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='系统角色权限关系' ROW_FORMAT=Compact;


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
) ENGINE=InnoDB AUTO_INCREMENT=1000 CHARACTER SET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='系统权限' ROW_FORMAT=Compact;


ALTER TABLE `sys_user_role` ADD CONSTRAINT FK_USER_ROLE_USER_ID_USER FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`id`);
ALTER TABLE `sys_user_role` ADD CONSTRAINT FK_USER_ROLE_ROLE_ID_ROLE FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`id`);

ALTER TABLE `sys_role_menu` ADD CONSTRAINT FK_ROLE_MENU_ROLE_ID_ROLE FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`id`);
ALTER TABLE `sys_role_menu` ADD CONSTRAINT FK_ROLE_MENU_MENU_ID_MENU FOREIGN KEY (`menu_id`) REFERENCES `sys_menu` (`id`);


DROP TABLE IF EXISTS `sys_user_token`;
CREATE TABLE `sys_user_token` (
                                  `id` BIGINT unsigned AUTO_INCREMENT NOT NULL COMMENT '主键ID',
                                  `user_id` INT unsigned NOT NULL COMMENT '关联用户ID',
                                  `identity` VARCHAR(100) COMMENT '身份标识',
                                  `token` VARCHAR(4000) NOT NULL COMMENT '交互凭证',
                                  `status` INT DEFAULT 1 NOT NULL COMMENT '状态：1-在线；2-离线；3-失效；4-登出',
                                  `expired_interval` INT COMMENT '失效时间间隔（单位秒）',
                                  `expiration_time` DATETIME COMMENT '失效时间',
                                  `logout_time` DATETIME COMMENT '登出时间',
                                  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建/登录时间',
                                  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 CHARACTER SET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='系统用户token' ROW_FORMAT=Compact;


DROP TABLE IF EXISTS `sys_action_log`;
CREATE TABLE `sys_action_log` (
                                `id` BIGINT unsigned AUTO_INCREMENT NOT NULL COMMENT '主键ID',
                                `identity` VARCHAR(100) NOT NULL COMMENT '身份标识',
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
) ENGINE=InnoDB AUTO_INCREMENT=1 CHARACTER SET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='系统操作日志' ROW_FORMAT=Compact;


DROP TABLE IF EXISTS `sys_parameter`;
CREATE TABLE `sys_parameter` (
                                  `id` INT unsigned AUTO_INCREMENT NOT NULL COMMENT '主键ID',
                                  `param_name` VARCHAR(500) NOT NULL COMMENT '参数名称',
                                  `param_key` VARCHAR(100) NOT NULL COMMENT '参数键名',
                                  `param_value` VARCHAR(4000) NOT NULL COMMENT '参数值',
                                  `param_type` VARCHAR(50) COMMENT '参数类型：internal-内置；external-外置；',
                                  `status` INT DEFAULT 1 NOT NULL COMMENT '状态：0-关闭；1-开启；',
                                  `app_range` VARCHAR(100) COMMENT '应用范围',
                                  `remark` VARCHAR(1000) COMMENT '备注',
                                  `creator` VARCHAR(100) COMMENT '创建者',
                                  `updater` VARCHAR(100) COMMENT '更新者',
                                  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                  PRIMARY KEY (`id`) USING BTREE,
                                  UNIQUE KEY `UQ_PARAM_KEY` (`param_key`)
) ENGINE=InnoDB AUTO_INCREMENT=1000 CHARACTER SET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='系统参数' ROW_FORMAT=Compact;


DROP TABLE IF EXISTS `sys_dictionary`;
CREATE TABLE `sys_dictionary` (
                                 `id` INT unsigned AUTO_INCREMENT NOT NULL COMMENT '主键ID',
                                 `dict_name` VARCHAR(500) NOT NULL COMMENT '字典名称',
                                 `dict_key` VARCHAR(100) NOT NULL COMMENT '字典键名',
                                 `dict_type` VARCHAR(50) COMMENT '字典类型',
                                 `status` INT DEFAULT 1 NOT NULL COMMENT '状态：0-停用；1-正常；',
                                 `app_range` VARCHAR(100) COMMENT '应用范围',
                                 `remark` VARCHAR(1000) COMMENT '备注',
                                 `creator` VARCHAR(100) COMMENT '创建者',
                                 `updater` VARCHAR(100) COMMENT '更新者',
                                 `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                 `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                 PRIMARY KEY (`id`) USING BTREE,
                                 KEY `IDX_DICT_KEY` (`dict_key`)
) ENGINE=InnoDB AUTO_INCREMENT=1000 CHARACTER SET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='系统字典' ROW_FORMAT=Compact;


DROP TABLE IF EXISTS `sys_dictionary_data`;
CREATE TABLE `sys_dictionary_data` (
                                  `id` INT unsigned AUTO_INCREMENT NOT NULL COMMENT '主键ID',
                                  `dict_id` INT unsigned NOT NULL COMMENT '关联字典ID',
                                  `dict_label` VARCHAR(100) NOT NULL COMMENT '字典数据标签',
                                  `dict_value` VARCHAR(500) NOT NULL COMMENT '字典数据值',
                                  `status` INT DEFAULT 1 NOT NULL COMMENT '状态：0-停用；1-正常；',
                                  `is_default` CHAR(1) COMMENT '是否默认：Y-是；N-否；',
                                  `remark` VARCHAR(1000) COMMENT '备注',
                                  `sort` INT default(0) COMMENT '排序',
                                  `echo_class` VARCHAR(50) COMMENT '数据回显样式',
                                  `style_attribute` VARCHAR(1000) COMMENT '数据样式属性',
                                  `creator` VARCHAR(100) COMMENT '创建者',
                                  `updater` VARCHAR(100) COMMENT '更新者',
                                  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                  PRIMARY KEY (`id`) USING BTREE,
                                  KEY `IDX_DICT_ID` (`dict_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1000 CHARACTER SET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='系统字典数据' ROW_FORMAT=Compact;
