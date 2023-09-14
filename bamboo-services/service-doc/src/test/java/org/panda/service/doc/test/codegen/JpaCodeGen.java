package org.panda.service.doc.test.codegen;

import org.panda.bamboo.common.util.LogUtil;
import org.panda.bamboo.core.context.ApplicationContextBean;
import org.panda.service.doc.test.config.JpaCodegenProperties;
import org.panda.tech.data.jpa.codegen.JpaCodeGenConfigSupport;
import org.panda.tech.data.jpa.codegen.JpaEntityGenerator;
import org.panda.tech.data.jpa.codegen.JpaRepoGenerator;

public class JpaCodeGen extends JpaCodeGenConfigSupport {

    private JpaCodegenProperties jpaCodegenProperties;

    private void initConfig() {
        if (jpaCodegenProperties == null) {
            jpaCodegenProperties = ApplicationContextBean.getBean(JpaCodegenProperties.class);
        }
    }

    @Override
    protected String getModelBasePackage() {
        initConfig();
        String entityLocation = jpaCodegenProperties.getEntityLocation();
        return entityLocation;
    }

    @Override
    protected String getRepoBasePackage() {
        initConfig();
        String repoLocation = jpaCodegenProperties.getRepoLocation();
        return repoLocation;
    }

    public void docEntityGenerator(String... tableOrEntityNames) {
        JpaEntityGenerator jpaEntityGenerator = super.entityGenerator();
        try {
            jpaEntityGenerator.generate(tableOrEntityNames);
        } catch (Exception e) {
            LogUtil.error(getClass(), e);
        }
    }

    public void docEntityGenerator(String tableOrEntityName) {
        JpaEntityGenerator jpaEntityGenerator = super.entityGenerator();
        try {
            jpaEntityGenerator.generate(tableOrEntityName);
        } catch (Exception e) {
            LogUtil.error(getClass(), e);
        }
    }

    public void docRepoGenerator() {
        JpaRepoGenerator jpaRepoGenerator = super.repoGenerator();
        try {
            jpaRepoGenerator.generate();
        } catch (Exception e) {
            LogUtil.error(getClass(), e);
        }
    }
    
}
