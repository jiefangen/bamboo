package org.panda.service.doc.test.codegen;

import org.junit.jupiter.api.Test;
import org.panda.service.doc.test.ApplicationTest;

public class JpaCodeGenTest extends ApplicationTest {

    private JpaCodeGen jpaCodeGenerator = new JpaCodeGen();

    @Test
    void jpaEntityGen() {
        jpaCodeGenerator.docEntityGenerator("doc_excel");
    }

    @Test
    public void jpaRepoGen() {
        jpaCodeGenerator.docRepoGenerator();
    }

}
