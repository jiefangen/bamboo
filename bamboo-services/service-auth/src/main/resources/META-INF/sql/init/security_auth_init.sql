/* 数据表初始化脚本, 默认明文密码123456 */
insert into `auth_account` (`id`, `username`, `password`, `secret_key`, `credentials`, `account_type`, `account_rank`) values
(101, 'bamboo_admin', '$2a$10$EMNrFn9zjcJZQf6z8WxG6OhdC5tQbk5a39hPG61aHkTGOVWacAWFi', 'a6UQAg6JpQhtrM8cGxPkp66twHg6yTvb', '8L4tmOJUrhzx5P8UG5I8mBJIEN25Uhj8qtJGVlRYFfbwzQHDKnjKzxQX9624UXJX', 'manager', '1'),
(102, 'bamboo_general', '$2a$10$EMNrFn9zjcJZQf6z8WxG6OhdC5tQbk5a39hPG61aHkTGOVWacAWFi', 'RG7ynuWGrAtW6tgQPRmwYUeKhgJtwUHQ', 'Z787jdhOAvQ8VXneN+ZX1Q5YwxGNZd70HH7ssT7LGG5uRxnsGULRysqhT/DUeH2aoZsG+jOJY2p792xQaSt6Rw==', 'general', '1'),
(103, 'bamboo_customer', '$2a$10$EMNrFn9zjcJZQf6z8WxG6OhdC5tQbk5a39hPG61aHkTGOVWacAWFi', 'mWPBQeuVBThTVF34rMYXjPGJ4tKrKNPj', '90/OSWYZ+ebDKFb79k1O+KLFGeIeIX45K23qLJeIdAKL7kwitONvfoBS+62s+4yoqX4Hl3X6SRW5eUAs8DPrgw==', 'customer', '1');

insert into `auth_role` (`id`, `role_name`, `role_code`, `description`) values
(101, 'admin', 'ADMIN' ,'顶级角色权限，有权访问所有资源。'),
(102, 'account', 'ACCOUNT', '账户角色权限，有权访问特定授权资源。'),
(103, 'customer', 'CUSTOMER', '访客，只能访问无权限管控的资源。');

insert into `auth_account_role` (`account_id`, `role_id`) values
(101, 101),(101, 102),(101, 103),
(102, 102),(102, 103),
(103, 103);
