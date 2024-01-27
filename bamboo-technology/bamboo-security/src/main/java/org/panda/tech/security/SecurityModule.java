package org.panda.tech.security;

import org.springframework.context.annotation.ComponentScan;

/**
 * 安全鉴权模块组件自动扫描机制
 *
 * @author fangen
 **/
@ComponentScan(basePackageClasses = SecurityModule.class)
public class SecurityModule {
}
