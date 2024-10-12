package org.panda.business.example.common.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.panda.business.example.common.constant.Datasource;
import org.panda.tech.data.common.DataCommons;
import org.panda.tech.data.mybatis.support.DynamicDataSourceSupport;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.Map;

/**
 * 动态数据源配置
 *
 * @author fangen
 **/
//@Configuration
public class DynamicDataSourceConfig extends DynamicDataSourceSupport {

    @Value("${mybatis.mapper-locations}")
    private String mapperLocationPattern;

    @Bean(name = DataCommons.DATASOURCE_PRIMARY)
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource primaryDataSource() {
        return new DruidDataSource();
    }

    @Bean(name = Datasource.DATASOURCE_ADMIN)
    @ConfigurationProperties(prefix = "spring.datasource.admin")
    public DataSource adminDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Override
    protected String getMapperLocationPattern() {
        return mapperLocationPattern;
    }

    @Override
    protected Map<Object, Object> getTargetDataSource() {
        Map<Object, Object> targetDataSource = super.getTargetDataSource();
        targetDataSource.put(Datasource.DATASOURCE_ADMIN, adminDataSource());
        return targetDataSource;
    }
}
