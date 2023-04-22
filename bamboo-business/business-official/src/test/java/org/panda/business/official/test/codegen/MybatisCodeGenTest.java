package org.panda.business.official.test.codegen;

import org.junit.jupiter.api.Test;
import org.panda.business.official.test.OfficialApplicationTest;

public class MybatisCodeGenTest extends OfficialApplicationTest {

    @Test
    void jpaEntityGen() {
        MybatisCodeGen mybatisCodeGen = new MybatisCodeGen();
        mybatisCodeGen.codeGenerator("sys_user");
    }

}
