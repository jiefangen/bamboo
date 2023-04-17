package org.panda.service.doc.test.codegen;

import org.junit.jupiter.api.Test;
import org.panda.service.doc.test.DocApplicationTest;

public class DocJpaCodeGenTest extends DocApplicationTest {

    @Test
    public void jpaCodeGen() {
        DocJpaCodeGen jpaCodeGenerator = new DocJpaCodeGen();
        jpaCodeGenerator.docEntityGenerator("doc_file", "doc_excel");
        jpaCodeGenerator.docRepoGenerator();
    }

}
