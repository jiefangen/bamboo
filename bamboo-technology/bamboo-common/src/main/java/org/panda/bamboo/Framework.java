package org.panda.bamboo;

import org.springframework.context.annotation.ComponentScan;

/**
 * 技术框架信息
 * 
 * @author fangen
 */
@ComponentScan(basePackageClasses = Framework.class)
public class Framework {
    /**
     * 框架名称
     */
    public static final String NAME = "bamboo";
    /**
     * 框架所有者
     */
    public static final String OWNER = "jiefangen";
    /**
     * 框架交流邮箱
     */
    public static final String EMAIL = "bamboo@gmail.com";
}
