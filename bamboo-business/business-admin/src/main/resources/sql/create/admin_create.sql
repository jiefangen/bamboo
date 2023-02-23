# bamboo后台管理系统表创建脚本
CREATE DATABASE  IF NOT EXISTS `bamboo_admin` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `bamboo_admin`;

SET NAMES utf8mb4;
SET GLOBAL FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS `SYS_USER`;
CREATE TABLE SYS_USER (
                          id BIGINT unsigned AUTO_INCREMENT NOT NULL,
                          username VARCHAR(100) NOT NULL,
                          password VARCHAR(100) NOT NULL,
                          salt VARCHAR(100) NOT NULL,
                          phone VARCHAR(20),
                          nickname VARCHAR(100),
                          email VARCHAR(50),
                          sex CHAR(1),
                          disabled TINYINT DEFAULT 0 NOT NULL,
                          create_time DATETIME NOT NULL,
                          update_time DATETIME,
                          PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10000 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统用户' ROW_FORMAT = Compact;


DROP TABLE IF EXISTS `SYS_USER_ROLE`;
CREATE TABLE SYS_USER_ROLE (
                               user_id BIGINT unsigned NOT NULL,
                               role_id BIGINT unsigned NOT NULL
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统用户角色关系' ROW_FORMAT = Compact;


DROP TABLE IF EXISTS `SYS_ROLE`;
CREATE TABLE SYS_ROLE (
                          id BIGINT unsigned AUTO_INCREMENT NOT NULL,
                          role_name VARCHAR(100) NOT NULL,
                          role_code VARCHAR(20) NOT NULL,
                          description VARCHAR(200),
                          create_time DATETIME NOT NULL,
                          update_time DATETIME,
                          PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10000 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统角色' ROW_FORMAT = Compact;


DROP TABLE IF EXISTS `SYS_ROLE_MENU`;
CREATE TABLE SYS_ROLE_MENU (
                               role_id BIGINT unsigned NOT NULL,
                               menu_id BIGINT unsigned NOT NULL
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 CHECKSUM=1 DELAY_KEY_WRITE=1 COMMENT = '系统角色菜单关系' ROW_FORMAT=DYNAMIC;


DROP TABLE IF EXISTS `SYS_MENU`;
CREATE TABLE `SYS_MENU` (
                            id BIGINT unsigned NOT NULL,
                            parent_id BIGINT unsigned NOT NULL,
                            menu_path VARCHAR(255) NOT NULL,
                            component VARCHAR(50) NOT NULL,
                            menu_name VARCHAR(100),
                            redirect VARCHAR(255),
                            title VARCHAR(100),
                            icon VARCHAR(100),
                            hidden TINYINT(1) default(0),
                            sort INT default(0),
                            PRIMARY KEY (`id`) USING BTREE
)ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统菜单' ROW_FORMAT = Compact;


ALTER TABLE SYS_USER_ROLE ADD CONSTRAINT FK_SYS_USER_ROLE_USER_ID_USER FOREIGN KEY (user_id) REFERENCES SYS_USER (id);
ALTER TABLE SYS_USER_ROLE ADD CONSTRAINT FK_SYS_USER_ROLE_ROLE_ID_ROLE FOREIGN KEY (role_id) REFERENCES SYS_ROLE (id);

ALTER TABLE SYS_ROLE_MENU ADD CONSTRAINT FK_SYS_ROLE_MENU_0 FOREIGN KEY (menu_id) REFERENCES SYS_MENU (id);
ALTER TABLE SYS_ROLE_MENU ADD CONSTRAINT FK_SYS_ROLE_MENU_1 FOREIGN KEY (role_id) REFERENCES SYS_ROLE (id);


DROP TABLE IF EXISTS `SYS_ACTION_LOG`;
CREATE TABLE SYS_ACTION_LOG (
                                id BIGINT unsigned AUTO_INCREMENT NOT NULL,
                                action_type INT NOT NULL,
                                content VARCHAR(200),
                                ip_address VARCHAR(20),
                                username VARCHAR(100),
                                operating_time DATETIME,
                                elapsed_time INT,
                                status_code INT,
                                exception_info VARCHAR(4000),
                                PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT=1 CHARACTER SET=utf8 COLLATE=utf8_general_ci COMMENT='系统操作日志' ROW_FORMAT=Compact;


DROP TABLE IF EXISTS `WORK_TO_DO`;
CREATE TABLE WORK_TO_DO (
                            id VARCHAR(64) NOT NULL,
                            work_status INT NOT NULL,
                            content VARCHAR(2000),
                            sort INT default(0),
                            user_id BIGINT unsigned NOT NULL,
                            create_time DATETIME,
                            update_time DATETIME,
                            PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET=utf8 COLLATE=utf8_general_ci COMMENT='待办事项' ROW_FORMAT=Compact;