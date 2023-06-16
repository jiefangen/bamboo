package org.panda.business.admin.test.codegen;

import org.panda.bamboo.core.context.ApplicationContextBean;
import org.panda.tech.data.codegen.ClassBasePackage;
import org.panda.tech.data.datasource.DataSourceConnConfig;
import org.panda.tech.data.mybatis.codegen.MybatisCodeGenConfigSupport;
import org.panda.tech.data.mybatis.codegen.MybatisCodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MybatisCodeGen extends MybatisCodeGenConfigSupport {

    @Autowired
    private MybatisCodegenProperties mybatisCodegenProperties;

    @Override
    protected DataSourceConnConfig getDataSourceConfig() {
        DataSourceConnConfig dataSourceConnConfig = mybatisCodegenProperties.transformDataSource();
        return dataSourceConnConfig;
    }

    @Override
    protected ClassBasePackage getClassBasePackage() {
        ClassBasePackage classBasePackage = mybatisCodegenProperties.transformClassPackage();
        return classBasePackage;
    }

    public void codeGenerator(String... tableNames) {
        MybatisCodeGenerator mybatisCodeGenerator = ApplicationContextBean.getBean("generator");
        mybatisCodeGenerator.generate(tableNames);
    }

    public void codeGenerator(String tableName, boolean withService) {
        MybatisCodeGenerator mybatisCodeGenerator = ApplicationContextBean.getBean("generator");
        mybatisCodeGenerator.generate(tableName, withService);
    }

}
