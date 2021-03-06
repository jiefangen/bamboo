# bamboo后台管理系统表创建脚本 v1版本
CREATE DATABASE  IF NOT EXISTS `bamboo_admin_v1` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `bamboo_admin_v1`;

SET NAMES utf8mb4;
SET GLOBAL FOREIGN_KEY_CHECKS = 0;


DROP TABLE IF EXISTS `SYS_PERMISSION_ROLE`;
CREATE TABLE SYS_PERMISSION_ROLE (
                                   permission_id BIGINT unsigned NOT NULL,
                                   role_id BIGINT unsigned NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC;


DROP TABLE IF EXISTS `SYS_ROLE_MENU`;
CREATE TABLE SYS_ROLE_MENU (
                             menu_id BIGINT unsigned NOT NULL,
                             role_id BIGINT unsigned NOT NULL
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC;


DROP TABLE IF EXISTS `SYS_PERMISSION`;
CREATE TABLE SYS_PERMISSION (
                              id BIGINT unsigned AUTO_INCREMENT NOT NULL,
                              permission_name VARCHAR(64) NOT NULL,
                              permission_code VARCHAR(20) NOT NULL,
                              parent_id BIGINT unsigned,
                              description VARCHAR(200),
                              resources_type VARCHAR(20) NOT NULL,
                              create_time DATETIME NOT NULL,
                              PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统用户权限' ROW_FORMAT = Compact;


DROP TABLE IF EXISTS `SYS_USER_ROLE`;
CREATE TABLE SYS_USER_ROLE (
                             user_id BIGINT unsigned NOT NULL,
                             role_id BIGINT unsigned NOT NULL
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统用户角色关系' ROW_FORMAT = Compact;


DROP TABLE IF EXISTS `SYS_ROLE`;
CREATE TABLE SYS_ROLE (
                        id BIGINT unsigned AUTO_INCREMENT NOT NULL,
                        role_name VARCHAR(64) NOT NULL,
                        role_code VARCHAR(20) NOT NULL,
                        description VARCHAR(200),
                        create_time DATETIME NOT NULL,
                        update_time DATETIME,
                        PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统角色' ROW_FORMAT = Compact;


DROP TABLE IF EXISTS `SYS_MENU`;
CREATE TABLE `SYS_MENU` (
                          id BIGINT unsigned AUTO_INCREMENT NOT NULL,
                          menu_name VARCHAR(64) NOT NULL,
                          description VARCHAR(255),
                          path VARCHAR(255),
                          type INT,
                          sort INT,
                          icon VARCHAR(255),
                          permission VARCHAR(255),
                          hidden BIT(1),
                          create_time DATETIME NOT NULL,
                          update_time DATETIME,
                          PRIMARY KEY (`id`) USING BTREE
)ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '菜单栏' ROW_FORMAT = Compact;


DROP TABLE IF EXISTS `SYS_USER`;
CREATE TABLE SYS_USER (
                        id BIGINT unsigned AUTO_INCREMENT NOT NULL,
                        username VARCHAR(64) NOT NULL,
                        password VARCHAR(64) NOT NULL,
                        salt VARCHAR(64) NOT NULL,
                        phone VARCHAR(20),
                        nickname VARCHAR(64),
                        email VARCHAR(64),
                        sex CHAR(1),
                        disabled TINYINT DEFAULT 0 NOT NULL,
                        create_time DATETIME NOT NULL,
                        update_time DATETIME,
                        PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统用户' ROW_FORMAT = Compact;


DROP TABLE IF EXISTS `SYS_ACTION_LOG`;
CREATE TABLE SYS_ACTION_LOG (
                              id BIGINT unsigned AUTO_INCREMENT NOT NULL,
                              action_type INT NOT NULL,
                              content VARCHAR(225) NOT NULL,
                              ip_address VARCHAR(20),
                              operating_time DATETIME NOT NULL,
                              user_id BIGINT unsigned NOT NULL,
                              PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统操作日志' ROW_FORMAT = Compact;




ALTER TABLE SYS_PERMISSION ADD CONSTRAINT FK_SYS_PERMISSION_PARENT_ID_PERMISSION FOREIGN KEY (parent_id) REFERENCES SYS_PERMISSION (id);

ALTER TABLE SYS_USER_ROLE ADD CONSTRAINT FK_SYS_USER_ROLE_USER_ID_USER FOREIGN KEY (user_id) REFERENCES SYS_USER (id);
ALTER TABLE SYS_USER_ROLE ADD CONSTRAINT FK_SYS_USER_ROLE_ROLE_ID_ROLE FOREIGN KEY (role_id) REFERENCES SYS_ROLE (id);

ALTER TABLE SYS_ACTION_LOG ADD CONSTRAINT FK_SYS_ACTION_LOG_USER_ID_USER FOREIGN KEY (user_id) REFERENCES SYS_USER (id);

ALTER TABLE SYS_PERMISSION_ROLE ADD CONSTRAINT FK_SYS_PERMISSION_ROLE_0 FOREIGN KEY (permission_id) REFERENCES SYS_PERMISSION (id);
ALTER TABLE SYS_PERMISSION_ROLE ADD CONSTRAINT FK_SYS_PERMISSION_ROLE_1 FOREIGN KEY (role_id) REFERENCES SYS_ROLE (id);

ALTER TABLE SYS_ROLE_MENU ADD CONSTRAINT FK_SYS_ROLE_MENU_0 FOREIGN KEY (menu_id) REFERENCES SYS_MENU (id);
ALTER TABLE SYS_ROLE_MENU ADD CONSTRAINT FK_SYS_ROLE_MENU_1 FOREIGN KEY (role_id) REFERENCES SYS_ROLE (id);
