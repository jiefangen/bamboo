package org.panda.ms.payment.test.codegen;

import org.junit.jupiter.api.Test;
import org.panda.ms.payment.test.ServiceApplicationTest;

public class JpaCodeGenTest extends ServiceApplicationTest {

    private JpaCodeGen jpaCodeGenerator = new JpaCodeGen();

    @Test
    void jpaEntityGen() {
        jpaCodeGenerator.commonEntityGenerator("pay_order", "pay_order_settlement", "pay_order_refund");
    }

    @Test
    public void jpaRepoGen() {
        jpaCodeGenerator.commonRepoGenerator();
    }

    @Test
    public void jpaRepoxGen() {
        jpaCodeGenerator.repoxGenerator(PayOrder1.class, true);
    }

}
