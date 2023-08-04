/* 数据表初始化脚本, 默认明文密码123456 */
insert into `sys_user` (`id`, `username`, `nickname`, `user_type`, `user_rank`, `password`, `sex`) values
(101, 'admin', 'Admin', 'manager', '0', '$2a$10$EMNrFn9zjcJZQf6z8WxG6OhdC5tQbk5a39hPG61aHkTGOVWacAWFi', '男'),
(102, 'system', 'System', 'manager', '0', '$2a$10$EMNrFn9zjcJZQf6z8WxG6OhdC5tQbk5a39hPG61aHkTGOVWacAWFi', '男'),
(103, 'actuator', 'Actuator', 'manager', '0', '$2a$10$EMNrFn9zjcJZQf6z8WxG6OhdC5tQbk5a39hPG61aHkTGOVWacAWFi', '女'),
(104, 'user', '木雨之舟', 'general', '1', '$2a$10$EMNrFn9zjcJZQf6z8WxG6OhdC5tQbk5a39hPG61aHkTGOVWacAWFi', '男'),
(105, '李礼', 'LiLi', 'general', '1', '$2a$10$EMNrFn9zjcJZQf6z8WxG6OhdC5tQbk5a39hPG61aHkTGOVWacAWFi', '女'),
(106, '介知礼', '知书达理', 'customer', '1', '$2a$10$EMNrFn9zjcJZQf6z8WxG6OhdC5tQbk5a39hPG61aHkTGOVWacAWFi', '女');

insert into `sys_role` (`id`, `role_name`, `role_code`, `description`) values
(101, 'admin', 'ADMIN' ,'顶级管理员，有权查看本平台所有页面。'),
(102, 'system', 'SYSTEM', '系统管理员，有权查看系统管理相关页面。'),
(103, 'actuator', 'ACTUATOR' ,'监控管理员，有权查看系统监控相关页面。'),
(104, 'fangen', 'USER', '一般用户，有权查看一般用户特定授权页面。'),
(105, 'general', 'GENERAL', '普通用户，有权查看特定授权页面。'),
(106, 'customer', 'CUSTOMER', '访客，只能查看无权限管控页面。');

insert into `sys_user_role` (`user_id`, `role_id`) values
(101, 101),
(102, 102),
(103, 103),
(104, 104),
(105, 105),
(106, 106);

insert into `sys_menu` (`id`, `parent_id`, `menu_path`, `redirect`, `menu_name`, `title`, `icon`, `component`, `hidden`, `sort`) values
(101, 0, '/system', '/system/user', 'SystemManage', 'systemManage', 'system', 'layout', 0, 0),
(102, 101, 'user', null, 'UserManage', 'userManage', 'peoples', 'system_user', 0, 0),
(103, 101, 'role', null, 'RoleManage', 'roleManage', 'role', 'system_role', 0, 1),
(104, 101, 'menu', null, 'MenuManage', 'menuManage', 'menu', 'system_menu', 0, 2),
(105, 101, 'permission', null, 'PermissionManage', 'permissionManage', 'permissions', 'system_permission', 0, 3),
(106, 0, '/monitor', '/monitor/log', 'SystemMonitor', 'systemMonitor', 'monitor', 'layout', 0, 1),
(107, 106, 'online', null, 'OnlineUser', 'onlineUser', 'online', 'monitor_online', 0, 0),
(108, 106, 'log', null, 'OperationLog', 'operationLog', 'log', 'monitor_log', 0, 1),
(109, 106, 'api', null, 'ApiDoc', 'apiDoc', 'api', 'monitor_api', 0, 2),
(110, 106, 'druid', null, 'DruidMonitor', 'druidMonitor', 'druid', 'monitor_druid', 0, 3),
(111, 0, '/settings', '/settings/parameter', 'SystemSettings', 'systemSettings', 'settings', 'layout', 0, 2),
(112, 111, 'parameter', null, 'ParamSettings', 'paramSettings', 'parameter', 'settings_parameter', 0, 0),
(113, 111, 'dictionary', null, 'DictSettings', 'dictSettings', 'dictionary', 'settings_dictionary', 0, 1);

insert into `sys_role_menu` (`role_id`, `menu_id`) values
(101, 101),(101, 102),(101, 103),(101, 104),(101, 105),(101, 106),(101, 107),(101, 108),(101, 109),(101, 110),
(101, 111),(101, 112),(101, 113),
(102, 101),(102, 102),(102, 103),(102, 104),(102, 105),(102, 111),(102, 112),(102, 113),
(103, 106),(103, 107),(103, 108),(103, 109),(103, 110),
(104, 101),(104, 102);

/* 系统参数初始化 */
insert into `sys_parameter` (`id`, `param_name`, `param_key`, `param_value`, `param_type`, `app_range`, `remark`, `creator`) values
(101, 'admin系统-生成token签名密钥key', 'admin:app:tokenKey', 'FBQ8x8pMuvX' ,'internal', 'admin', '随机生成字符串，可定期更换', 'systemInit'),
(102, 'admin系统-token失效时间间隔', 'admin:app:tokenInterval', '3600' ,'internal', 'admin', '时间间隔，单位秒', 'systemInit'),
(103, 'admin系统-权限加载url通配符', 'admin:app:authUrlPatterns', '/system/**,/monitor/**,/settings/**' ,'internal', 'admin', '匹配模式数组格式存储', 'systemInit'),
(104, 'admin系统-同时在线用户限制', 'admin:user:onlineLimit', '3' ,'internal', 'admin', '统计限制在线和离线用户', 'systemInit'),
(105, '用户管理-账号初始化密码', 'admin:user:initPassword', '123456' ,'internal', 'admin', '默认用户密码', 'systemInit');

/* 系统字典初始化 */
insert into `sys_dictionary` (`id`, `dict_name`, `dict_key`, `dict_type`, `app_range`, `remark`, `creator`) values
(101, '用户类型', 'sys_user_type', 'userType' ,'admin', '用户类型列表', 'systemInit'),
(102, '用户级别', 'sys_user_rank', 'userRank' ,'admin', '用户级别列表', 'systemInit'),
(103, '系统角色', 'sys_role_code', 'roleCode' ,'admin', '系统角色列表', 'systemInit'),
(104, '系统api状态', 'sys_api_status', 'apiStatus' ,'admin', '接口请求返回状态枚举', 'systemInit'),
(105, '日志操作类型', 'sys_operator_type', 'operatorType' ,'admin', '日志操作类型枚举', 'systemInit'),
(106, '数据标签', 'sys_echo_class', 'echoClass' ,'admin', '数据标签回显样式', 'systemInit'),
(107, '通用文案（中文）', 'common_language_zh', 'i18n' ,'admin', 'admin管理后台通用文案（中文）', 'systemInit'),
(108, '通用文案（英文）', 'common_language_en', 'i18n' ,'admin', 'admin管理后台通用文案（英文）', 'systemInit');

/* 系统字典初始化 */
insert into `sys_dictionary_data` (`id`, `dict_id`, `dict_label`, `dict_value`, `is_default`, `remark`, `sort`, `creator`) values
(101, 101, '管理员' ,'manager', 'Y', '系统用户top类型', 0, 'systemInit'),
(102, 101, '普通用户' ,'general', 'N', '系统一般用户类型', 1, 'systemInit'),
(103, 101, '访客/游客' ,'customer', 'N', '系统默认用户类型', 2, 'systemInit'),

(104, 102, '等级' ,'0', 'Y', '管理员专用级别', 0, 'systemInit'),
(105, 102, '等级1' ,'1', 'N', '1～n-其它类型级别', 1, 'systemInit'),
(106, 102, '等级2' ,'2', 'N', '1～n-其它类型级别', 2, 'systemInit'),
(107, 102, '等级3' ,'3', 'N', '1～n-其它类型级别', 3, 'systemInit'),

(108, 103, '顶级管理员' ,'ADMIN', 'Y', '有权查看本平台所有页面', 0, 'systemInit'),
(109, 103, '系统管理员' ,'SYSTEM', 'N', '有权查看系统管理相关页面', 1, 'systemInit'),
(110, 103, '监控管理员' ,'ACTUATOR', 'N', '有权查看系统监控相关页面', 2, 'systemInit'),
(111, 103, '一般用户' ,'USER', 'N', '有权查看一般用户特定授权页面', 3, 'systemInit'),
(112, 103, '普通用户' ,'GENERAL', 'N', '有权查看特定授权页面', 4, 'systemInit'),
(113, 103, '访客' ,'CUSTOMER', 'N', '只能查看无权限管控页面', 5, 'systemInit'),

(114, 104, '成功' ,'2000', 'Y', '正常返回', 0, 'systemInit'),
(115, 104, '失败' ,'5000', 'N', '执行失败', 1, 'systemInit'),
(116, 104, '未知错误' ,'9999', 'N', '系统运行异常', 2, 'systemInit'),
(117, 104, '认证失败' ,'4010', 'N', '认证鉴权异常', 3, 'systemInit'),
(118, 104, '授权失败' ,'4030', 'N', '认证鉴权异常', 4, 'systemInit'),
(119, 104, 'token验证失败' ,'4014', 'N', 'token校验失败拦截', 5, 'systemInit'),
(120, 104, 'token失效' ,'4018', 'N', 'token校验失败拦截', 6, 'systemInit'),
(121, 104, '业务异常' ,'5100', 'N', '通用业务异常', 7, 'systemInit'),
(122, 104, '参数异常' ,'5110', 'N', '接口传参异常拦截', 8, 'systemInit'),
(123, 104, '必传参数异常' ,'5111', 'N', '接口必传参数异常拦截', 9, 'systemInit'),

(124, 105, '新增' ,'add', 'Y', '新增操作', 0, 'systemInit'),
(125, 105, '删除' ,'del', 'N', '删除操作', 1, 'systemInit'),
(126, 105, '查询' ,'query', 'N', '查询操作', 2, 'systemInit'),
(127, 105, '更新' ,'update', 'N', '更新操作', 3, 'systemInit'),
(128, 105, '登录' ,'login', 'N', '登录操作', 4, 'systemInit'),
(129, 105, '登出' ,'quit', 'N', '登出操作', 5, 'systemInit'),
(130, 105, '授权' ,'auth', 'N', '授权擦操作', 6, 'systemInit'),
(131, 105, '导出' ,'export', 'N', '导出操作', 7, 'systemInit'),
(132, 105, '导入' ,'import', 'N', '导入操作', 8, 'systemInit'),
(133, 105, '清空' ,'empty', 'N', '清空操作', 9, 'systemInit'),
(134, 105, '其他' ,'other', 'N', '其他操作', 10, 'systemInit'),

(135, 106, '默认' ,'default', 'Y', '标签默认回显样式', 0, 'systemInit'),
(136, 106, '主要' ,'primary', 'N', '标签主要回显样式', 1, 'systemInit'),
(137, 106, '成功' ,'success', 'N', '标签成功回显样式', 2, 'systemInit'),
(138, 106, '信息' ,'info', 'N', '标签信息回显样式', 3, 'systemInit'),
(139, 106, '警告' ,'warning', 'N', '标签警告回显样式', 4, 'systemInit'),
(140, 106, '危险' ,'danger', 'N', '标签危险回显样式', 5, 'systemInit'),

(141, 107, '用户类型' ,'sys_user_type', 'Y', '国际化标签', 0, 'systemInit'),
(142, 107, '用户级别' ,'sys_user_rank', 'N', '国际化标签', 1, 'systemInit'),
(143, 107, '系统角色' ,'sys_role_code', 'N', '国际化标签', 2, 'systemInit'),
(144, 107, '系统api状态' ,'sys_api_status', 'N', '国际化标签', 3, 'systemInit'),
(145, 107, '日志操作类型' ,'sys_operator_type', 'N', '国际化标签', 4, 'systemInit'),
(146, 107, '数据标签' ,'sys_echo_class', 'N', '国际化标签', 5, 'systemInit'),
(147, 107, '通用文案（中文）' ,'common_language_zh', 'N', '国际化标签', 6, 'systemInit'),
(148, 107, '通用文案（英文）' ,'common_language_en', 'N', '国际化标签', 7, 'systemInit'),

(149, 108, 'UserType' ,'sys_user_type', 'Y', '国际化标签', 0, 'systemInit'),
(150, 108, 'UserRank' ,'sys_user_rank', 'N', '国际化标签', 1, 'systemInit'),
(151, 108, 'RoleCode' ,'sys_role_code', 'N', '国际化标签', 2, 'systemInit'),
(152, 108, 'ApiStatus' ,'sys_api_status', 'N', '国际化标签', 3, 'systemInit'),
(153, 108, 'OperatorType' ,'sys_operator_type', 'N', '国际化标签', 4, 'systemInit'),
(154, 108, 'EchoClass' ,'sys_echo_class', 'N', '国际化标签', 5, 'systemInit'),
(155, 108, 'General(Chinese)' ,'common_language_zh', 'N', '国际化标签', 6, 'systemInit'),
(156, 108, 'General(English)' ,'common_language_en', 'N', '国际化标签', 7, 'systemInit');
