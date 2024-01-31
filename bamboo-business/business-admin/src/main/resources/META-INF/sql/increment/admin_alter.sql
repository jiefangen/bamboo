/*
===================增量语句模板========================

1、新建xxx_add.sql文件的人，首先根据自身所写的增量语句所在的库，新建对应的sql文件
2、然后按相应格式将相应的增量语句写进库模块中。

===============这是模块格式如下：=================
*/
begin by fangen 01
ALTER TABLE sys_role ADD UNIQUE `UQ_ROLE_CODE` (role_code);

ALTER TABLE auth_permission MODIFY permission_code VARCHAR(100);
ALTER TABLE auth_account ADD `credentials` VARCHAR(150) COMMENT '账户凭证' AFTER `password`;
ALTER TABLE auth_account ADD `secret_key` VARCHAR(100) NOT NULL COMMENT '密钥' AFTER `password`;
ALTER TABLE auth_account MODIFY credentials VARCHAR(300) COMMENT '账户凭证';

ALTER TABLE auth_permission ADD UNIQUE `UQ_PERMISSION_CODE` (`permission_code`);
ALTER TABLE auth_permission ADD `scope` VARCHAR(100) COMMENT '权限范围' AFTER `resources`;
ALTER TABLE auth_permission ADD `description` VARCHAR(200) COMMENT '描述';
ALTER TABLE app_server ADD UNIQUE `UQ_APP_CODE` (`app_code`);
end by fangen 01

begin by fangen 02
SHOW INDEX FROM auth_permission;
DROP INDEX UQ_PERMISSION_CODE ON auth_permission;

ALTER TABLE auth_permission DROP COLUMN `description`;
end by fangen 02
