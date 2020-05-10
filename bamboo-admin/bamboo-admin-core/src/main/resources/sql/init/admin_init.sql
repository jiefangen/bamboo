# 数据表初始化脚本
insert into SYS_USER (username, password, sex, disabled, create_time, update_time) values
('admin', '123456', '男', 0, now(), now()),
('jiefangen', '123456', '男', 0, now(), now()),
('xiaoming', '123456', '女', 0, now(), now()),
('xiaohong', '123456', '男', 0, now(), now()),
('xiaolan', '123456', '女', 0, now(), now()),
('xiaolv', '123456', '女', 0, now(), now());

insert into SYS_ROLE (role_name, description, create_time, update_time) values
('admin', '顶级管理员', now(), now()),
('system', '系统管理员', now(), now()),
('user', '用户管理员', now(), now()),
('general', '普通管理员', now(), now()),
('customer', '访客', now(), now());

insert into SYS_USER_ROLE (user_id, role_id) values
(1, 1),
(2, 2),
(3, 2),
(2, 3);
