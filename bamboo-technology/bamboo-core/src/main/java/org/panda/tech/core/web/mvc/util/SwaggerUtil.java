package org.panda.tech.core.web.mvc.util;

import org.springframework.context.ApplicationContext;

/**
 * Swagger工具类
 */
public class SwaggerUtil {

    private SwaggerUtil() {
    }

    public static boolean isEnabled(ApplicationContext context) {
        ClassLoader classLoader = context.getClassLoader();
        try {
            if (classLoader != null) {
                Class<?> docketClass = classLoader.loadClass("springfox.documentation.spring.web.plugins.Docket");
                context.getBean(docketClass);
                return true;
            }
        } catch (Exception e) {
            // 忽略所有异常，返回false
        }
        return false;
    }

}
