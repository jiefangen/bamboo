package org.panda.bamboo.common.constant;

/**
 * 运行环境常量集
 */
public class Profiles {

    private Profiles() {
    }

    /**
     * 运行环境：单元测试
     */
    public static final String JUNIT = "junit";
    /**
     * 运行环境：本地
     */
    public static final String LOCAL = "local";
    /**
     * 运行环境：开发
     */
    public static final String DEV = "dev";
    /**
     * 运行环境：测试
     */
    public static final String TEST = "test";
    /**
     * 运行环境：演示
     */
    public static final String DEMO = "demo";
    /**
     * 运行环境：生产
     */
    public static final String PRODUCTION = "prod";

}
