package org.panda.doc.config;

import org.panda.data.jpa.codegen.JpaCodeGenConfigSupport;

public class DocJpaCodeGen extends JpaCodeGenConfigSupport {

    @Override
    protected String getModelBasePackage() {
        return "org.panda.doc.common.entity";
    }
    
}
