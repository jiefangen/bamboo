package org.panda.business.helper.app.test.codegen;

import org.panda.bamboo.core.context.SpringContextHolder;
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
        return mybatisCodegenProperties.transformDataSource();
    }

    @Override
    protected ClassBasePackage getClassBasePackage() {
        return mybatisCodegenProperties.transformClassPackage();
    }

    public void codeGenerator(String... tableNames) {
        MybatisCodeGenerator mybatisCodeGenerator = SpringContextHolder.getBean("generator");
        mybatisCodeGenerator.generate(tableNames);
    }

    public void codeGenerator(String tableName, boolean withService) {
        MybatisCodeGenerator mybatisCodeGenerator = SpringContextHolder.getBean("generator");
        mybatisCodeGenerator.generate(tableName, withService);
    }

}
