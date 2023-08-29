package org.panda.ms.notice.test.codegen;

import org.junit.jupiter.api.Test;
import org.panda.ms.notice.test.ServiceApplicationTest;

public class JpaCodeGenTest extends ServiceApplicationTest {

    private JpaCodeGen jpaCodeGenerator = new JpaCodeGen();

    @Test
    void jpaEntityGen() {
        jpaCodeGenerator.docEntityGenerator("notice_config_template", "notice_send_record");
    }

    @Test
    public void jpaRepoGen() {
        jpaCodeGenerator.docRepoGenerator();
    }

}
