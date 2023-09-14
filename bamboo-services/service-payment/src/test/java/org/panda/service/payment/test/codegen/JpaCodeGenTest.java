package org.panda.service.payment.test.codegen;

import org.junit.jupiter.api.Test;
import org.panda.service.payment.model.entity.PayOrderNotify;
import org.panda.service.payment.test.ServiceApplicationTest;

public class JpaCodeGenTest extends ServiceApplicationTest {

    private JpaCodeGen jpaCodeGenerator = new JpaCodeGen();

    @Test
    void jpaEntityGen() {
        jpaCodeGenerator.commonEntityGenerator("pay_order", "pay_request_records",
                "pay_order_notify", "pay_order_settlement", "pay_order_refund");
    }

    @Test
    public void jpaRepoGen() {
        jpaCodeGenerator.commonRepoGenerator();
    }

    @Test
    public void jpaRepoxGen() {
        jpaCodeGenerator.repoxGenerator(PayOrderNotify.class, true);
    }

}
