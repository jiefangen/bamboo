package org.panda.tech.security;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 安全鉴权模块组件自动扫描机制
 *
 * @author fangen
 **/
@Configuration
@ComponentScan(basePackageClasses = SecurityModule.class)
public class SecurityModule {
}
