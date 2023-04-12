package org.panda.service.doc.config;

import org.panda.bamboo.common.util.LogUtil;
import org.panda.tech.data.jpa.codegen.JpaCodeGenConfigSupport;
import org.panda.tech.data.jpa.codegen.JpaEntityGenerator;
import org.panda.tech.data.jpa.codegen.JpaRepoGenerator;
import org.panda.service.doc.dao.RepoBasePackage;

public class DocJpaCodeGen extends JpaCodeGenConfigSupport {
    public static final String BASE_PACKAGE = "org.panda.service.doc.common.entity";

    @Override
    protected String getModelBasePackage() {
        return BASE_PACKAGE;
    }

    @Override
    protected String getRepoBasePackage() {
        return RepoBasePackage.class.getPackageName();
    }

    public void docEntityGenerator(String entityName) {
        JpaEntityGenerator jpaEntityGenerator = super.entityGenerator();
        try {
            jpaEntityGenerator.generate(entityName);
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
