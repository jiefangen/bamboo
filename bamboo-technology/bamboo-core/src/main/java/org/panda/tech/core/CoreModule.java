package org.panda.tech.core;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 注册核心模块组件自动扫描机制
 *
 * @author fangen
 **/
@Configuration
@ComponentScan(basePackageClasses = CoreModule.class)
public class CoreModule {
}
