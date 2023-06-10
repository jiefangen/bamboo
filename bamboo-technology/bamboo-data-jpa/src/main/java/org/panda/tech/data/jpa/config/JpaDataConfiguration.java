package org.panda.tech.data.jpa.config;

import org.panda.bamboo.common.constant.basic.Strings;
import org.panda.bamboo.common.util.LogUtil;
import org.panda.tech.data.jpa.hibernate.JpaHibernatePersistenceProvider;
import org.panda.tech.data.jpa.hibernate.MetadataProvider;
import org.panda.tech.data.jpa.support.JpaAccessTemplate;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings;
import org.springframework.boot.autoconfigure.orm.jpa.JpaBaseConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.autoconfigure.sql.init.SqlInitializationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.AbstractJpaVendorAdapter;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.jta.JtaTransactionManager;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * JPA数据层配置
 *
 * @author fangen
 */
@Configuration
@EnableConfigurationProperties(HibernateProperties.class)
public class JpaDataConfiguration extends JpaBaseConfiguration {

    @Autowired
    private HibernateProperties hibernateProperties;
    private ApplicationContext context;

    /**
     * 在多数据源场景下，可创建子类，构造函数中指定DataSourceProperties和DataSource的beanName
     */
    public JpaDataConfiguration(ApplicationContext context, SqlInitializationProperties dataSourceProperties,
            DataSource dataSource, JpaProperties properties,
            ObjectProvider<JtaTransactionManager> jtaTransactionManager) {
        super(dataSource, properties, jtaTransactionManager);
        this.context = context;
        // 当前配置会在数据源对象构建完之后，在数据源初始化之前加载，接下来spring-data-jpa框架会检查数据库表结构，
        // 此时如果数据库表结构未初始化，则会报错退出，导致后续的DataSourceInitializerInvoker无法执行，
        // 悲剧的是，通过@AutoConfigureAfter指定加载顺序，仍然无法在DataSourceInitializerInvoker之后执行，
        // 所以需要在此时提前执行数据库初始化脚本，以确保数据库表结构检查通过
        initDataSource(dataSourceProperties);
    }

    private void initDataSource(SqlInitializationProperties properties) {
        List<String> locations = new ArrayList<>();
        List<String> schema = properties.getSchemaLocations();
        if (schema != null) {
            locations.addAll(schema);
            schema.clear(); // 清除以避免框架再次执行
        }
        List<String> data = properties.getDataLocations();
        if (data != null) {
            locations.addAll(data);
            data.clear(); // 清除以避免框架再次执行
        }
        if (locations.size() > 0) {
            DataSourceInitializer initializer = new DataSourceInitializer();
            initializer.setDataSource(getDataSource());
            ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
            locations.forEach(location -> {
                populator.addScript(this.context.getResource(location));
            });
            initializer.setDatabasePopulator(populator);
            initializer.afterPropertiesSet();
        }
    }

    protected String getSchema() {
        return null;
    }

    @Override
    protected AbstractJpaVendorAdapter createJpaVendorAdapter() {
        return new HibernateJpaVendorAdapter();
    }

    @Override
    protected Map<String, Object> getVendorProperties() {
        return this.hibernateProperties.determineHibernateProperties(getProperties().getProperties(),
                new HibernateSettings());
    }

    protected void addMappingResources(List<String> mappingResources) {
        List<String> adding = new ArrayList<>();
        Iterator<String> iterator = mappingResources.iterator();
        while (iterator.hasNext()) {
            String location = iterator.next();
            int wildcardIndex = location.indexOf(Strings.ASTERISK);
            if (wildcardIndex > 0) { // 不支持处理以*开头的路径
                try {
                    String pattern = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + location;
                    Resource[] resources = this.context.getResources(pattern);
                    for (Resource resource : resources) {
                        String path = resource.getURI().toString();
                        String dir = location.substring(0, wildcardIndex);
                        adding.add(path.substring(path.lastIndexOf(dir)));
                    }
                } catch (IOException e) {
                    LogUtil.error(getClass(), e);
                }
                iterator.remove();
            }
        }
        mappingResources.addAll(adding);
    }

    @Bean
    public JpaHibernatePersistenceProvider persistenceProvider() {
        return new JpaHibernatePersistenceProvider();
    }

    @Override
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder factoryBuilder) {
        addMappingResources(getProperties().getMappingResources());
        LocalContainerEntityManagerFactoryBean factoryBean = super.entityManagerFactory(factoryBuilder);
        factoryBean.setPersistenceProvider(persistenceProvider());
        return factoryBean;
    }

    @Bean
    public JpaAccessTemplate jpaAccessTemplate(EntityManagerFactory factory, MetadataProvider metadataProvider) {
        String schema = getSchema();
        if (schema == null) {
            return new JpaAccessTemplate(factory, metadataProvider);
        } else {
            return new JpaAccessTemplate(schema, factory, metadataProvider);
        }
    }

}
