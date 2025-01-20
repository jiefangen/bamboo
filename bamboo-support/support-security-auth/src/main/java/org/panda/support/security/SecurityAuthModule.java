package org.panda.support.security;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * SecurityAuth模块
 *
 * @author fangen
 */
@Configuration
@ComponentScan(basePackageClasses = SecurityAuthModule.class)
public class SecurityAuthModule {
}
