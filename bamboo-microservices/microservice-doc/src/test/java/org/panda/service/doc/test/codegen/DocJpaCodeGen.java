package org.panda.service.doc.test.codegen;

import org.panda.bamboo.common.util.LogUtil;
import org.panda.tech.data.jpa.codegen.JpaCodeGenConfigSupport;
import org.panda.tech.data.jpa.codegen.JpaEntityGenerator;
import org.panda.tech.data.jpa.codegen.JpaRepoGenerator;

public class DocJpaCodeGen extends JpaCodeGenConfigSupport {

    @Override
    protected String getModelBasePackage() {
        return "org.panda.service.doc.common.entity";
    }

    @Override
    protected String getRepoBasePackage() {
        return "org.panda.service.doc.dao";
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
