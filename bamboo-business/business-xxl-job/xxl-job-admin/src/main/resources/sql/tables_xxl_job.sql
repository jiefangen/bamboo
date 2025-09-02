#
# XXL-JOB
# Copyright (c) 2015-present, xuxueli.

CREATE database if NOT EXISTS `xxl_job` default character set utf8mb4 collate utf8mb4_unicode_ci;
use `xxl_job`;

SET NAMES utf8mb4;

CREATE TABLE `xxl_job_info`
(
    `id`                        int(11)      NOT NULL AUTO_INCREMENT,
    `job_group`                 int(11)      NOT NULL COMMENT '执行器主键ID',
    `job_desc`                  varchar(255) NOT NULL,
    `add_time`                  datetime              DEFAULT NULL,
    `update_time`               datetime              DEFAULT NULL,
    `author`                    varchar(64)           DEFAULT NULL COMMENT '作者',
    `alarm_email`               varchar(255)          DEFAULT NULL COMMENT '报警邮件',
    `schedule_type`             varchar(50)  NOT NULL DEFAULT 'NONE' COMMENT '调度类型',
    `schedule_conf`             varchar(128)          DEFAULT NULL COMMENT '调度配置，值含义取决于调度类型',
    `misfire_strategy`          varchar(50)  NOT NULL DEFAULT 'DO_NOTHING' COMMENT '调度过期策略',
    `executor_route_strategy`   varchar(50)           DEFAULT NULL COMMENT '执行器路由策略',
    `executor_handler`          varchar(255)          DEFAULT NULL COMMENT '执行器任务handler',
    `executor_param`            varchar(512)          DEFAULT NULL COMMENT '执行器任务参数',
    `executor_block_strategy`   varchar(50)           DEFAULT NULL COMMENT '阻塞处理策略',
    `executor_timeout`          int(11)      NOT NULL DEFAULT '0' COMMENT '任务执行超时时间，单位秒',
    `executor_fail_retry_count` int(11)      NOT NULL DEFAULT '0' COMMENT '失败重试次数',
    `glue_type`                 varchar(50)  NOT NULL COMMENT 'GLUE类型',
    `glue_source`               mediumtext COMMENT 'GLUE源代码',
    `glue_remark`               varchar(128)          DEFAULT NULL COMMENT 'GLUE备注',
    `glue_updatetime`           datetime              DEFAULT NULL COMMENT 'GLUE更新时间',
    `child_jobid`               varchar(255)          DEFAULT NULL COMMENT '子任务ID，多个逗号分隔',
    `trigger_status`            tinyint(4)   NOT NULL DEFAULT '0' COMMENT '调度状态：0-停止，1-运行',
    `trigger_last_time`         bigint(13)   NOT NULL DEFAULT '0' COMMENT '上次调度时间',
    `trigger_next_time`         bigint(13)   NOT NULL DEFAULT '0' COMMENT '下次调度时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE `xxl_job_log`
(
    `id`                        bigint(20) NOT NULL AUTO_INCREMENT,
    `job_group`                 int(11)    NOT NULL COMMENT '执行器主键ID',
    `job_id`                    int(11)    NOT NULL COMMENT '任务，主键ID',
    `executor_address`          varchar(255)        DEFAULT NULL COMMENT '执行器地址，本次执行的地址',
    `executor_handler`          varchar(255)        DEFAULT NULL COMMENT '执行器任务handler',
    `executor_param`            varchar(512)        DEFAULT NULL COMMENT '执行器任务参数',
    `executor_sharding_param`   varchar(20)         DEFAULT NULL COMMENT '执行器任务分片参数，格式如 1/2',
    `executor_fail_retry_count` int(11)    NOT NULL DEFAULT '0' COMMENT '失败重试次数',
    `trigger_time`              datetime            DEFAULT NULL COMMENT '调度-时间',
    `trigger_code`              int(11)    NOT NULL COMMENT '调度-结果',
    `trigger_msg`               text COMMENT '调度-日志',
    `handle_time`               datetime            DEFAULT NULL COMMENT '执行-时间',
    `handle_code`               int(11)    NOT NULL COMMENT '执行-状态',
    `handle_msg`                text COMMENT '执行-日志',
    `alarm_status`              tinyint(4) NOT NULL DEFAULT '0' COMMENT '告警状态：0-默认、1-无需告警、2-告警成功、3-告警失败',
    PRIMARY KEY (`id`),
    KEY `I_trigger_time` (`trigger_time`),
    KEY `I_handle_code` (`handle_code`),
    KEY `I_jobid_jobgroup` (`job_id`,`job_group`),
    KEY `I_job_id` (`job_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE `xxl_job_log_report`
(
    `id`            int(11) NOT NULL AUTO_INCREMENT,
    `trigger_day`   datetime         DEFAULT NULL COMMENT '调度-时间',
    `running_count` int(11) NOT NULL DEFAULT '0' COMMENT '运行中-日志数量',
    `suc_count`     int(11) NOT NULL DEFAULT '0' COMMENT '执行成功-日志数量',
    `fail_count`    int(11) NOT NULL DEFAULT '0' COMMENT '执行失败-日志数量',
    `update_time`   datetime         DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `i_trigger_day` (`trigger_day`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE `xxl_job_logglue`
(
    `id`          int(11)      NOT NULL AUTO_INCREMENT,
    `job_id`      int(11)      NOT NULL COMMENT '任务，主键ID',
    `glue_type`   varchar(50) DEFAULT NULL COMMENT 'GLUE类型',
    `glue_source` mediumtext COMMENT 'GLUE源代码',
    `glue_remark` varchar(128) NOT NULL COMMENT 'GLUE备注',
    `add_time`    datetime    DEFAULT NULL,
    `update_time` datetime    DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE `xxl_job_registry`
(
    `id`             int(11)      NOT NULL AUTO_INCREMENT,
    `registry_group` varchar(50)  NOT NULL,
    `registry_key`   varchar(255) NOT NULL,
    `registry_value` varchar(255) NOT NULL,
    `update_time`    datetime DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `i_g_k_v` (`registry_group`, `registry_key`, `registry_value`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE `xxl_job_group`
(
    `id`           int(11)     NOT NULL AUTO_INCREMENT,
    `app_name`     varchar(64) NOT NULL COMMENT '执行器AppName',
    `title`        varchar(12) NOT NULL COMMENT '执行器名称',
    `address_type` tinyint(4)  NOT NULL DEFAULT '0' COMMENT '执行器地址类型：0=自动注册、1=手动录入',
    `address_list` text COMMENT '执行器地址列表，多地址逗号分隔',
    `update_time`  datetime             DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE `xxl_job_user`
(
    `id`         int(11)     NOT NULL AUTO_INCREMENT,
    `username`   varchar(50) NOT NULL COMMENT '账号',
    `password`   varchar(50) NOT NULL COMMENT '密码',
    `role`       tinyint(4)  NOT NULL COMMENT '角色：0-普通用户、1-管理员',
    `permission` varchar(255) DEFAULT NULL COMMENT '权限：执行器ID列表，多个逗号分割',
    PRIMARY KEY (`id`),
    UNIQUE KEY `i_username` (`username`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE `xxl_job_lock`
(
    `lock_name` varchar(50) NOT NULL COMMENT '锁名称',
    PRIMARY KEY (`lock_name`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;


-- ========================================
-- XXL-JOB 系统表初始化数据
-- ========================================

INSERT INTO `xxl_job_group`(`id`, `app_name`, `title`, `address_type`, `address_list`, `update_time`)
VALUES (1, 'xxl-job-executor', '默认执行器', 0, NULL, '2025-08-20 12:30:30');

INSERT INTO `xxl_job_user`(`id`, `username`, `password`, `role`, `permission`)
VALUES (1, 'admin', 'e10adc3949ba59abbe56e057f20f883e', 1, NULL);

INSERT INTO `xxl_job_lock` (`lock_name`)
VALUES ('schedule_lock');


-- ========================================
-- XXL-JOB 系统表索引优化 DDL
-- ========================================

-- 1. xxl_job_registry 表索引
CREATE INDEX idx_xxl_job_registry_update_time ON xxl_job_registry (update_time);
CREATE UNIQUE INDEX uk_xxl_job_registry_group_key_value ON xxl_job_registry (registry_group, registry_key, registry_value);

-- 2. xxl_job_user 表索引
CREATE UNIQUE INDEX uk_xxl_job_user_username ON xxl_job_user (username);
CREATE INDEX idx_xxl_job_user_role ON xxl_job_user (role);

-- 3. xxl_job_group 表索引
CREATE INDEX idx_xxl_job_group_address_type ON xxl_job_group (address_type);
CREATE INDEX idx_xxl_job_group_app_name ON xxl_job_group (app_name);
CREATE INDEX idx_xxl_job_group_title ON xxl_job_group (title);
CREATE INDEX idx_xxl_job_group_app_name_title_id ON xxl_job_group (app_name, title, id);

-- 4. xxl_job_info 表索引
CREATE INDEX idx_xxl_job_info_job_group ON xxl_job_info (job_group);
CREATE INDEX idx_xxl_job_info_trigger_status ON xxl_job_info (trigger_status);
CREATE INDEX idx_xxl_job_info_job_desc ON xxl_job_info (job_desc);
CREATE INDEX idx_xxl_job_info_executor_handler ON xxl_job_info (executor_handler);
CREATE INDEX idx_xxl_job_info_author ON xxl_job_info (author);
CREATE INDEX idx_xxl_job_info_trigger_status_next_time ON xxl_job_info (trigger_status, trigger_next_time);

-- 5. xxl_job_logglue 表索引
CREATE INDEX idx_xxl_job_logglue_job_id ON xxl_job_logglue (job_id);
CREATE INDEX idx_xxl_job_logglue_job_id_update_time ON xxl_job_logglue (job_id, update_time DESC);

-- 6. xxl_job_log 表索引（重要：日志表数据量大，索引尤为重要）
CREATE INDEX idx_xxl_job_log_job_group ON xxl_job_log (job_group);
CREATE INDEX idx_xxl_job_log_job_id ON xxl_job_log (job_id);
CREATE INDEX idx_xxl_job_log_trigger_time ON xxl_job_log (trigger_time);
CREATE INDEX idx_xxl_job_log_trigger_time_desc ON xxl_job_log (trigger_time DESC);
CREATE INDEX idx_xxl_job_log_trigger_code ON xxl_job_log (trigger_code);
CREATE INDEX idx_xxl_job_log_handle_code ON xxl_job_log (handle_code);
CREATE INDEX idx_xxl_job_log_alarm_status ON xxl_job_log (alarm_status);
CREATE INDEX idx_xxl_job_log_executor_address ON xxl_job_log (executor_address);
CREATE INDEX idx_xxl_job_log_trigger_handle_time ON xxl_job_log (trigger_code, handle_code, trigger_time);
CREATE INDEX idx_xxl_job_log_job_group_trigger_time ON xxl_job_log (job_group, trigger_time DESC);
CREATE INDEX idx_xxl_job_log_job_id_trigger_time ON xxl_job_log (job_id, trigger_time DESC);

-- 7. xxl_job_log_report 表索引
CREATE UNIQUE INDEX uk_xxl_job_log_report_trigger_day ON xxl_job_log_report (trigger_day);

commit;

-- CRON：0 0 0 * * ? *'*（每天的0点整（午夜 00:00:00）运行一次）
