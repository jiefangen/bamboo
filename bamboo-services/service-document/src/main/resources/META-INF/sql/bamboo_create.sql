/* BAMBOO系统全局数据库表创建脚本 */
/* **************************************** Document文档服务表库创建脚本 **************************************************** */
USE `bamboo_system`; /* 切换到bamboo_system库 */

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
                            `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                            `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                            PRIMARY KEY (`id`) USING BTREE,
                            KEY `IDX_FILE_MD5` (`file_md5`)
) ENGINE=InnoDB DEFAULT CHARACTER SET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='文档文件' ROW_FORMAT=Compact;


DROP TABLE IF EXISTS `doc_file_storage`;
CREATE TABLE `doc_file_storage` (
                                    `id` BIGINT unsigned AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
                                    `doc_file_id` BIGINT unsigned NOT NULL COMMENT '文档文件ID',
                                    `file_binary` LONGBLOB NOT NULL COMMENT '二进制文件', /* TINYBLOB(255B)；BLOB(64KB)；MEDIUMBLOB(16MB)；LONGBLOB(4GB) */
                                    `status` INT DEFAULT 1 NOT NULL COMMENT '0-异常/损坏；1-正常；2-删除；',
                                    `storage_path` VARCHAR(500) COMMENT '文件存储路径',
                                    UNIQUE KEY `UQ_DOC_FILE_ID` (`doc_file_id`)
) ENGINE=InnoDB DEFAULT CHARACTER SET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='文档文件存储' ROW_FORMAT=Compact;


DROP TABLE IF EXISTS `doc_excel_data`;
CREATE TABLE `doc_excel_data` (
                                  `id` BIGINT unsigned AUTO_INCREMENT NOT NULL COMMENT '主键ID',
                                  `doc_file_id` BIGINT unsigned NOT NULL COMMENT '文档文件ID',
                                  `sheet_name` VARCHAR(300) NOT NULL COMMENT '工作表名称',
                                  `row_index` INT(11) NOT NULL COMMENT '单元格行索引',
                                  `column_index` INT(11) NOT NULL COMMENT '单元格列索引',
                                  `cell_value` VARCHAR(1000) COMMENT '单元格值',
                                  PRIMARY KEY (`id`) USING BTREE,
                                  KEY `IDX_DOC_FILE_ID` (`doc_file_id`)
) ENGINE=InnoDB DEFAULT CHARACTER SET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='文档EXCEL数据' ROW_FORMAT=Compact;

/* **************************************** Document文档服务表库创建脚本 **************************************************** */
