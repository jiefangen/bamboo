package org.panda.business.official.common.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.panda.tech.data.mybatis.support.DynamicDataSourceSupport;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * 动态数据源配置
 *
 * @author fangen
 **/
@Configuration
public class DynamicDataSourceConfig extends DynamicDataSourceSupport {

    @Value("${mybatis.mapper-locations}")
    private String mapperLocationPattern;

    @Bean(name = DATASOURCE_PRIMARY_NAME)
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource primaryDataSource() {
        return new DruidDataSource();
    }

    @Bean(name = DATASOURCE_SECONDARY_NAME)
    @ConfigurationProperties(prefix = "spring.datasource.secondary")
    public DataSource secondaryDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Override
    protected String getMapperLocationPattern() {
        return mapperLocationPattern;
    }

}
