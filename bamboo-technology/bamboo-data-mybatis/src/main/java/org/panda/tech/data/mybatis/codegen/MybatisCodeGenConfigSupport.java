package org.panda.tech.data.mybatis.codegen;

import org.panda.tech.data.codegen.ClassBasePackage;
import org.panda.tech.data.datasource.DataSourceConnConfig;
import org.springframework.context.annotation.Bean;

/**
 * Mybatis代码生成配置支持
 *
 * @author fangen
 */
public abstract class MybatisCodeGenConfigSupport {

    /**
     * 获取数据库连接配置
     *
     * @return 数据源配置参量
     */
    protected abstract DataSourceConnConfig getDataSourceConfig();

    /**
     * 获取代码生成基础包名称集，用于定位扫描的代码类存放的位置
     *
     * @return 模型基础包名称
     */
    protected abstract ClassBasePackage getClassBasePackage();

    @Bean
    protected MybatisCodeGeneratorImpl generator() {
        return new MybatisCodeGeneratorImpl(getDataSourceConfig(), getClassBasePackage());
    }

}
