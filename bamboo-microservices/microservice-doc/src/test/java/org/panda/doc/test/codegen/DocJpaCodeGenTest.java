package org.panda.doc.test.codegen;

import org.junit.jupiter.api.Test;
import org.panda.doc.config.DocJpaCodeGen;
import org.panda.doc.test.DocApplicationTest;

public class DocJpaCodeGenTest extends DocApplicationTest {

    DocJpaCodeGen docJpaCodeGenerator = new DocJpaCodeGen();

    @Test
    public void docJpaCodeGen() {
        docJpaCodeGenerator.repoGenerator();
    }

}
