/* service_notice通知服务表创建脚本 */
DROP DATABASE IF EXISTS `service_notice`;
CREATE DATABASE IF NOT EXISTS `service_notice` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `service_notice`;

SET NAMES utf8mb4;
SET GLOBAL FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS `notice_config_template`;
CREATE TABLE `notice_config_template` (
                                    `id` INT unsigned AUTO_INCREMENT NOT NULL COMMENT '主键ID',
                                    `template_name` varchar(500) NOT NULL COMMENT '模版名称',
                                    `template_content_title` varchar(1000) COMMENT '模版内容标题',
                                    `template_content` TEXT NOT NULL COMMENT '模版内容',
                                    `notice_mode` CHAR(1) NOT NULL COMMENT '通知方式',
                                    `notice_provider_type` varchar(180) NOT NULL COMMENT '通知内容提供者类型',
                                    `notice_sender_type` varchar(50) COMMENT '通知发送器类型',
                                    `category` varchar(50) DEFAULT NULL COMMENT '类别',
                                    `is_active` BIT(1) DEFAULT b'1' NOT NULL COMMENT '是否激活',
                                     PRIMARY KEY (`id`) USING BTREE,
                                     UNIQUE KEY `UQ_NOTICE_TYPE_MODE` (`notice_provider_type`, `notice_mode`)
) ENGINE=InnoDB DEFAULT CHARACTER SET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='通知配置模版' ROW_FORMAT=Compact;


DROP TABLE IF EXISTS `notice_send_record`;
CREATE TABLE `notice_send_record` (
                                    `id` BIGINT unsigned AUTO_INCREMENT NOT NULL COMMENT '主键ID',
                                    `notice_mode` CHAR(1) NOT NULL COMMENT '通知方式',
                                    `type` varchar(50) COMMENT '通知类型',
                                    `title` varchar(1000) COMMENT '标题',
                                    `content` TEXT NOT NULL COMMENT '内容',
                                    `notice_targets` varchar(4000) NOT NULL COMMENT '通知发送目标',
                                    `notice_status` INT DEFAULT NULL COMMENT '通知状态：1-成功；0-失败；',
                                    `notice_result` varchar(1000) DEFAULT NULL COMMENT '通知结果',
                                    `tags` varchar(300) DEFAULT NULL COMMENT '标签',
                                    `send_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发送时间',
                                    `receive_time` datetime COMMENT '接收时间',
                                     PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARACTER SET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='通知发送记录表' ROW_FORMAT=Compact;
