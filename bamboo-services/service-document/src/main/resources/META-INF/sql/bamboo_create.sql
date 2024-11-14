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
) ENGINE=InnoDB DEFAULT CHARACTER SET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='文档文件表' ROW_FORMAT=Compact;

/* **************************************** Document文档服务表库创建脚本 **************************************************** */
