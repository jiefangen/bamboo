package org.panda.service.doc.config;

import org.panda.bamboo.common.util.LogUtil;
import org.panda.data.jpa.codegen.JpaCodeGenConfigSupport;
import org.panda.data.jpa.codegen.JpaRepoGenerator;
import org.panda.service.doc.dao.RepoBasePackage;

public class DocJpaCodeGen extends JpaCodeGenConfigSupport {
    public static final String BASE_PACKAGE = "org.panda.doc.common.entity";

    @Override
    protected String getModelBasePackage() {
        return BASE_PACKAGE;
    }

    @Override
    protected String getRepoBasePackage() {
        return RepoBasePackage.class.getPackageName();
    }

    @Override
    public JpaRepoGenerator repoGenerator() {
        try {
            JpaRepoGenerator jpaRepoGenerator = super.repoGenerator();
            jpaRepoGenerator.generate();
            return jpaRepoGenerator;
        } catch (Exception e) {
            LogUtil.error(getClass(), e);
        }
        return null;
    }
    
}
