package org.panda.ms.doc.test.codegen;

import org.junit.jupiter.api.Test;
import org.panda.ms.doc.test.DocApplicationTest;

public class DocJpaCodeGenTest extends DocApplicationTest {

    private DocJpaCodeGen jpaCodeGenerator = new DocJpaCodeGen();

    @Test
    void jpaEntityGen() {
        jpaCodeGenerator.docEntityGenerator("doc_file", "doc_excel");
    }

    @Test
    public void jpaRepoGen() {
        jpaCodeGenerator.docRepoGenerator();
    }

}
