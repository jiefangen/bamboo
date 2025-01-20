package org.panda.support.security.annotation;

import org.panda.support.security.config.AppSecurityAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 开启认证鉴权，可配置到应用启动项生效
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({AppSecurityAutoConfiguration.class})
public @interface EnableAuthServer {
}
