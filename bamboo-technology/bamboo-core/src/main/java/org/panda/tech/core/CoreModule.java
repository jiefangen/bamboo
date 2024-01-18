package org.panda.tech.core;

import org.springframework.context.annotation.ComponentScan;

/**
 * 注册核心模块组件自动扫描机制
 *
 * @author fangen
 **/
@ComponentScan(basePackageClasses = CoreModule.class)
public class CoreModule {
}
