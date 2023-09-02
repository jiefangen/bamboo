package org.panda.ms.payment.test.codegen;

import org.junit.jupiter.api.Test;
import org.panda.ms.payment.test.ServiceApplicationTest;

public class JpaCodeGenTest extends ServiceApplicationTest {

    private JpaCodeGen jpaCodeGenerator = new JpaCodeGen();

    @Test
    void jpaEntityGen() {
        jpaCodeGenerator.docEntityGenerator("", "");
    }

    @Test
    public void jpaRepoGen() {
        jpaCodeGenerator.docRepoGenerator();
    }

}
