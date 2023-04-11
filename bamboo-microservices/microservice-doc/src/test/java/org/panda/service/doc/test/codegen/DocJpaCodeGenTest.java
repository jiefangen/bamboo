package org.panda.service.doc.test.codegen;

import org.junit.jupiter.api.Test;
import org.panda.service.doc.config.DocJpaCodeGen;
import org.panda.service.doc.test.DocApplicationTest;

public class DocJpaCodeGenTest extends DocApplicationTest {

   private DocJpaCodeGen docJpaCodeGenerator = new DocJpaCodeGen();

    @Test
    public void docJpaCodeGen() {
        docJpaCodeGenerator.repoGenerator();
    }

}
