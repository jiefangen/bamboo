## bamboo-business（具体业务架构应用层）

### 一、业务系统项目简介
#### 1、business-admin（后台权限管理系统）
    1、基础核心能力，使用shiro与springboot结合来做安全token校验。整体内嵌druid监控以及swagger 
    2、API文档能力，同时使用aop做日志切面操作日志管理。
    3、门户模块接口，提供系统代办事项功能的基础操作能力。
    4、系统管理模块，其氛围用户管理，角色管理以及菜单管理相关功能。
    5、监控模块接口，现主要有日志操作管理能力的提供。
#### 2、business-official（技术官网系统）
    1、技术内容管理
    2、技术文档库
    3、优秀案例和经验分享

### 二、业务系统项目目录结构
    `org.panda.business.{业务系统名}`: 业务系统项目的根包名。
        common - 公共模块
            annotation - 自定义注解
            config - 公共配置
            constant - 常量定义
            exception - 异常处理
            util - 工具类
        application - 应用层
            scheduler - 定时任务
            event - 事件
            task - 异步任务
            validation - 应用参数校验
        modules - 平台划分模块包
            module1
                api - 对外暴露的接口层
                    controller - 控制器层
                    param - 参数对象
                    vo - 视图对象
                service - 服务层
                    impl - 服务实现类
                    dto - 数据传输对象
                    entity - 实体类
                    repository - 数据访问层
            ...
        subsystems - 平台嵌套子系统
            subsystem1 - 参考module1的目录结构
            ...
        infrastructure - 基础设施层
            cache - 缓存相关
            message - 消息中间件
            security - 安全相关
            thirdparty - 第三方组件服务

### 三、业务系统项目设计指导