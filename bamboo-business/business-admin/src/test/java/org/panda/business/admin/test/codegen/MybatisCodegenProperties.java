package org.panda.business.admin.test.codegen;

import lombok.Setter;
import org.panda.tech.data.codegen.ClassBasePackage;
import org.panda.tech.data.datasource.DataSourceConnConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Setter
@Configuration
@ConfigurationProperties(prefix = "bamboo.mybatis.codegen")
public class MybatisCodegenProperties {
    /**
     * 父级路径
     */
    private String parentLocation;
    /**
     * 实体类路径
     */
    private String entityLocation;
    /**
     * 数据访问层路径
     */
    private String repositoryLocation;
    /**
     * 服务层路径
     */
    private String serviceLocation;

    /**
     * 数据库连接url
     */
    private String url;
    /**
     * 连接驱动名称
     */
    private String driverName;
    /**
     * 数据库用户名
     */
    private String username;
    /**
     * 数据库密码
     */
    private String password;

    public ClassBasePackage transformClassPackage() {
        ClassBasePackage classBasePackage = new ClassBasePackage();
        classBasePackage.setParentLocation(this.parentLocation);
        classBasePackage.setEntityLocation(this.entityLocation);
        classBasePackage.setRepositoryLocation(this.repositoryLocation);
        classBasePackage.setServiceLocation(this.serviceLocation);
        return classBasePackage;
    }

    public DataSourceConnConfig transformDataSource() {
        DataSourceConnConfig dataSourceConnConfig = new DataSourceConnConfig();
        dataSourceConnConfig.setUrl(this.url);
        dataSourceConnConfig.setDriverName(this.driverName);
        dataSourceConnConfig.setUsername(this.username);
        dataSourceConnConfig.setPassword(this.password);
        return dataSourceConnConfig;
    }
}
