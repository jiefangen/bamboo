/* Document文档服务表创建脚本 */
DROP DATABASE IF EXISTS `service_document`;
CREATE DATABASE IF NOT EXISTS `service_document` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `service_document`;

SET NAMES utf8mb4;
SET GLOBAL FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS `doc_file`;
CREATE TABLE `doc_file` (
                        `id` BIGINT unsigned AUTO_INCREMENT NOT NULL COMMENT '主键ID',
                        `filename` varchar(300) NOT NULL COMMENT '文件名称',
                        `file_type` varchar(50) NOT NULL COMMENT '文件类型',
                        `file_md5` varchar(180) NOT NULL COMMENT '文件md5值',
                        `file_size` BIGINT unsigned NULL COMMENT '文件大小',
                        `content` longtext DEFAULT NULL COMMENT '文本内容',
                        `accessibility` BIT(1) default(1) COMMENT '可访问性',
                        `category` varchar(50) DEFAULT NULL COMMENT '类别',
                        `tags` varchar(300) DEFAULT NULL COMMENT '标签',
                        `biz_attributes` varchar(100) DEFAULT NULL COMMENT '业务属性',
                        `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                        `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                        PRIMARY KEY (`id`) USING BTREE,
                        KEY `IDX_FILE_MD5` (`file_md5`)
) ENGINE=InnoDB DEFAULT CHARACTER SET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='文档文件' ROW_FORMAT=Compact;


DROP TABLE IF EXISTS `doc_file_storage`;
CREATE TABLE `doc_file_storage` (
                                `id` BIGINT unsigned AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
                                `file_id` BIGINT unsigned NOT NULL COMMENT '文档文件ID',
                                `file_binary` LONGBLOB NOT NULL COMMENT '二进制文件', /* TINYBLOB(255B)；BLOB(64KB)；MEDIUMBLOB(16MB)；LONGBLOB(4GB) */
                                `status` INT DEFAULT 1 NOT NULL COMMENT '0-异常/损坏；1-正常；2-删除；',
                                `storage_location` VARCHAR(500) DEFAULT 'db' COMMENT '文件存储位置：db-数据库；其他-第三方链接',
                                UNIQUE KEY `UQ_FILE_ID` (`file_id`)
) ENGINE=InnoDB DEFAULT CHARACTER SET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='文档文件存储' ROW_FORMAT=Compact;


DROP TABLE IF EXISTS `doc_excel_data`;
CREATE TABLE `doc_excel_data` (
                              `id` BIGINT unsigned AUTO_INCREMENT NOT NULL COMMENT '主键ID',
                              `file_id` BIGINT unsigned NOT NULL COMMENT '文档文件ID',
                              `sheet_name` VARCHAR(300) NOT NULL COMMENT '工作表名称',
                              `row_index` INT(11) NOT NULL COMMENT '单元格行索引',
                              `column_index` INT(11) NOT NULL COMMENT '单元格列索引',
                              `cell_value` VARCHAR(1000) COMMENT '单元格值',
                              PRIMARY KEY (`id`) USING BTREE,
                              KEY `IDX_FILE_ID` (`file_id`)
) ENGINE=InnoDB DEFAULT CHARACTER SET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='文档EXCEL数据' ROW_FORMAT=Compact;
