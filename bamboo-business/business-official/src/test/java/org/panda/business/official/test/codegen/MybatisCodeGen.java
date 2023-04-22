package org.panda.business.official.test.codegen;

import org.panda.bamboo.common.util.LogUtil;
import org.panda.bamboo.core.context.ApplicationContextBean;
import org.panda.tech.data.codegen.ClassBasePackage;
import org.panda.tech.data.mybatis.codegen.MybatisCodeGenConfigSupport;
import org.panda.tech.data.mybatis.codegen.MybatisCodeGenerator;

public class MybatisCodeGen extends MybatisCodeGenConfigSupport {

    private MybatisCodegenProperties mybatisCodegenProperties;

    @Override
    protected ClassBasePackage getClassBasePackage() {
        ClassBasePackage classBasePackage = new ClassBasePackage();
        return classBasePackage;
    }

    private void initConfig() {
        if (mybatisCodegenProperties == null) {
            mybatisCodegenProperties = ApplicationContextBean.getBean(MybatisCodegenProperties.class);
        }
    }

    public void codeGenerator(String... tableNames) {
        MybatisCodeGenerator mybatisCodeGenerator = super.generator();
        try {
            mybatisCodeGenerator.generate(tableNames);
        } catch (Exception e) {
            LogUtil.error(getClass(), e);
        }
    }

}
