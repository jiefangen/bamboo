/* service_payment支付服务表创建脚本 */
DROP DATABASE IF EXISTS `service_payment`;
CREATE DATABASE IF NOT EXISTS `service_payment` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `service_payment`;

SET NAMES utf8mb4;
SET GLOBAL FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS `pay_order`;
CREATE TABLE `pay_order` (
                          `id` BIGINT unsigned AUTO_INCREMENT NOT NULL COMMENT '主键ID',
                          `order_no` VARCHAR(100) NOT NULL COMMENT '订单号',
                          `amount` DECIMAL(11,2) NOT NULL COMMENT '交易金额',
                          `currency` CHAR(3) NOT NULL COMMENT '交易币种',
                          `channel` VARCHAR(10) NOT NULL COMMENT '支付渠道',
                          `channel_flow_no` VARCHAR(100) COMMENT '渠道流水号',
                          `status` VARCHAR(30) NOT NULL COMMENT '订单状态：1-pending；2-processing；3-paid；4-failed；5-settled；6-refunded',
                          `terminal` VARCHAR(50) COMMENT '支付终端',
                          `payer_ip` VARCHAR(50) COMMENT '支付者IP',
                          `target` VARCHAR(50) COMMENT '支付目标',
                          `completion_id` BIGINT COMMENT '订单完成ID：结算；退款；',
                          `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                          `update_time` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                           PRIMARY KEY (`id`) USING BTREE,
                           UNIQUE KEY `UQ_ORDER_NO` (`order_no`)
) ENGINE=InnoDB DEFAULT CHARACTER SET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='支付订单表' ROW_FORMAT=Compact;


DROP TABLE IF EXISTS `pay_request_records`;
CREATE TABLE `pay_request_records` (
                                    `id` BIGINT unsigned AUTO_INCREMENT NOT NULL COMMENT '主键ID',
                                    `order_id` VARCHAR(100) NOT NULL COMMENT '订单号',
                                    `request_mode` VARCHAR(100) NOT NULL COMMENT '请求方式',
                                    `pay_params` VARCHAR(1000) COMMENT '付款参数集，可组装付款所需参数',
                                    `pay_url` VARCHAR(50) COMMENT '付款资源链接',
                                    `request_data` VARCHAR(1000) COMMENT '支付请求数据',
                                    `response_data` VARCHAR(1000) COMMENT '支付响应数据',
                                    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                     PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARACTER SET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='支付请求记录表' ROW_FORMAT=Compact;


DROP TABLE IF EXISTS `pay_order_notify`;
CREATE TABLE `pay_order_notify` (
                                 `id` BIGINT unsigned AUTO_INCREMENT NOT NULL COMMENT '主键ID',
                                 `order_id` VARCHAR(100) NOT NULL COMMENT '订单号',
                                 `channel` VARCHAR(10) NOT NULL COMMENT '通知渠道',
                                 `channel_flow_no` VARCHAR(100) NOT NULL COMMENT '渠道流水号',
                                 `amount` DECIMAL(11,2) NOT NULL COMMENT '交易金额',
                                 `response_status` INT COMMENT '响应状态',
                                 `notify_data` VARCHAR(1000) COMMENT '回调通知数据',
                                 `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARACTER SET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='订单回调通知表' ROW_FORMAT=Compact;


DROP TABLE IF EXISTS `pay_order_settlement`;
CREATE TABLE `pay_order_settlement` (
                                     `id` BIGINT unsigned AUTO_INCREMENT NOT NULL COMMENT '主键ID',
                                     `settle_no` VARCHAR(100) NOT NULL COMMENT '结算单号',
                                     `settle_type` VARCHAR(50) NOT NULL COMMENT '结算类型：auto-自动回调结算；manual-人工结算',
                                     `channel` VARCHAR(10) NOT NULL COMMENT '支付渠道',
                                     `channel_flow_no` VARCHAR(100) COMMENT '渠道流水号',
                                     `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                     `operator_id` INT COMMENT '人工操作ID',
                                     `finish_time` DATETIME COMMENT '完成时间',
                                      PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARACTER SET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='支付订单结算表' ROW_FORMAT=Compact;


DROP TABLE IF EXISTS `pay_order_refund`;
CREATE TABLE `pay_order_refund` (
                                    `id` BIGINT unsigned AUTO_INCREMENT NOT NULL COMMENT '主键ID',
                                    `refund_no` VARCHAR(100) NOT NULL COMMENT '退款单号',
                                    `channel` VARCHAR(10) NOT NULL COMMENT '退款渠道',
                                    `channel_flow_no` VARCHAR(100) COMMENT '渠道流水号',
                                    `status` VARCHAR(30) NOT NULL COMMENT '退款状态：1-pending；2-processing；3-completed；4-failed；',
                                    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                    `operator_id` INT COMMENT '人工操作ID',
                                    `finish_time` DATETIME COMMENT '完成时间',
                                    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARACTER SET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='支付订单退款表' ROW_FORMAT=Compact;
