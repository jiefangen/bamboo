package org.panda.tech.core.config.app;

/**
 * 应用常量类
 */
public class AppConstants {

    private AppConstants() {
    }

    /**
     * 属性：Spring应用名称
     */
    public static final String PROPERTY_SPRING_APP_NAME = "spring.application.name";

    /**
     * 表达式：Spring应用名称
     */
    public static final String EL_SPRING_APP_NAME = "${spring.application.name}";

    /**
     * 表达式：服务启动环境
     */
    public static final String EL_SPRING_PROFILES_ACTIVE = "${spring.profiles.active}";

}
