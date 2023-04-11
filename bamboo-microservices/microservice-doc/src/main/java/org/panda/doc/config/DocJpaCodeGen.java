package org.panda.doc.config;

import org.panda.bamboo.common.util.LogUtil;
import org.panda.data.jpa.codegen.JpaCodeGenConfigSupport;
import org.panda.data.jpa.codegen.JpaEntityMappingGenerator;
import org.panda.data.jpa.codegen.JpaRepoGenerator;
import org.panda.doc.common.entity.DocFile;
import org.panda.doc.dao.RepoBasePackage;

public class DocJpaCodeGen extends JpaCodeGenConfigSupport {

    @Override
    protected String getModelBasePackage() {
        return "org.panda.doc.common.entity";
    }

    @Override
    protected String getRepoBasePackage() {
        return RepoBasePackage.class.getPackageName();
    }

    @Override
    public JpaEntityMappingGenerator entityMappingGenerator() {
        return super.entityMappingGenerator();
    }

    @Override
    public JpaRepoGenerator repoGenerator() {
        try {
            JpaRepoGenerator jpaRepoGenerator = super.repoGenerator();
            jpaRepoGenerator.generate(DocFile.class, false);
            return jpaRepoGenerator;
        } catch (Exception e) {
            LogUtil.error(getClass(), e);
        }
        return null;
    }
    
}
