# bamboo-admin
BAMBOO后台管理系统--后端项目

> 这是BAMBOO管理系统的后端项目，它包含了各个模块的数据接口支撑以及权限管控的核心。

目前只有main分支为初始化版本，提供了后台管理系统的基本功能，即用户角色资源之间的权限分配与管控。

## 关联项目
- [bamboo-vue](https://github.com/jiefangen/bamboo-vue) 与其对接的前端项目

## 模块简介
- 基础核心能力，使用shiro与springboot结合来做安全token校验。整体内嵌druid监控以及swagger 
API文档能力，同时使用aop做日志切面操作日志管理。
- 门户模块接口，提供系统代办事项功能的基础操作能力。
- 系统管理模块，其氛围用户管理，角色管理以及菜单管理相关功能。
- 监控模块接口，现主要有日志操作管理能力的提供。

## 项目部署与启动
1. 初始化数据库，在resource -> sql文件夹中可以找到初始化数据库表的脚本，并在mysql数据库中初始化。
2. 启动项目，使用AdminApplication可一健启动。
3. 浏览器访问swagger验证接口 [Bamboo Api Guide](http://127.0.0.1:8081/bamboo-admin/doc.html)

## Demo
相关demo可看前端项目演示
[bamboo-vue](https://github.com/jiefangen/bamboo-vue)

## License

[MIT](https://github.com/jiefangen/bamboo-vue/blob/main/LICENSE) license.

Copyright (c) 2022-present JieFangen