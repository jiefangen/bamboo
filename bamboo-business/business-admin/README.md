# bamboo-admin
BAMBOO后台管理系统--后端项目

> 这是BAMBOO管理系统的后端项目，它包含了各个模块的数据接口支撑以及权限管控的核心。

目前只有main分支为初始化版本，提供了后台管理系统的基本功能，即用户角色资源之间的权限分配与管控。

## 关联项目
- [admin-vue](https://github.com/jiefangen/frontend-vue/tree/main/admin-vue) 与其对接的前端项目

## 模块简介
    1.核心技术组件，使用自研独立设计bamboo-security模块包做整个系统的安全管控
    2.基础技术组件，使用springboot+mybatis-plus做整个系统的基础架构。
    3.系统管理功能，用户管理、角色管理、菜单管理、权限管理。
    4.系统监控管理功能，在线用户、操作日志、API外链文档、Druid监控外链。
    5.系统配置，参数设置、字典管理。

## 项目部署与启动
1. 初始化数据库，在resource -> sql文件夹中可以找到初始化数据库表的脚本，并在mysql数据库中初始化。
2. 启动项目，使用AdminApplication可一健启动。
3. 浏览器访问swagger验证接口 [Admin Api Guide](http://127.0.0.1:10001/biz-admin/doc.html)

## Demo
相关demo可看前端项目演示
[bamboo-vue](https://github.com/jiefangen/frontend-vue/blob/main/admin-vue/README.md)

## License

[Apache 2.0](https://github.com/jiefangen/bamboo/blob/master/LICENSE) license.

Copyright (c) 2023-present JieFangen