# 数据表初始化脚本,默认明文密码123456
insert into SYS_USER (username, password, salt, sex, disabled, create_time) values
('admin', '9e1c263393c88252f0a379071bb0eef0', '8fc70e1a13231c5bf28cc8da404bdcd6', '男', 0, now()),
('jiefangen', '9e1c263393c88252f0a379071bb0eef0', '8fc70e1a13231c5bf28cc8da404bdcd6', '男', 0, now()),
('xiaoming', '9e1c263393c88252f0a379071bb0eef0', '8fc70e1a13231c5bf28cc8da404bdcd6', '女', 0, now()),
('xiaohong', '9e1c263393c88252f0a379071bb0eef0', '8fc70e1a13231c5bf28cc8da404bdcd6', '女', 0, now()),
('xiaolan', '9e1c263393c88252f0a379071bb0eef0', '8fc70e1a13231c5bf28cc8da404bdcd6', '女', 0, now()),
('xiaolv', '9e1c263393c88252f0a379071bb0eef0', '8fc70e1a13231c5bf28cc8da404bdcd6', '女', 0, now());

insert into SYS_ROLE (role_name, role_code, description, create_time, update_time) values
('admin', 'ADMIN' ,'顶级管理员', now(), now()),
('actuator', 'ACTUATOR' ,'监控管理员', now(), now()),
('system', 'SYSTEM', '系统管理员', now(), now()),
('user', 'USER', '用户管理员', now(), now()),
('general', 'GENERAL', '普通管理员', now(), now()),
('customer', 'CUSTOMER', '访客', now(), now());

insert into SYS_USER_ROLE (user_id, role_id) values
(2, 2),
(2, 3),
(3, 4),
(5, 6),
(6, 7);
