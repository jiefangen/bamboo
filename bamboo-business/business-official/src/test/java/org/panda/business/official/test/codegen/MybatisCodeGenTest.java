package org.panda.business.official.test.codegen;

import org.junit.jupiter.api.Test;
import org.panda.business.official.test.OfficialApplicationTest;

public class MybatisCodeGenTest extends OfficialApplicationTest {

    @Test
    void codeGen() {
        MybatisCodeGen mybatisCodeGen = new MybatisCodeGen();
//        mybatisCodeGen.codeGenerator("sys_user", "sys_role");
        mybatisCodeGen.codeGenerator("sys_user_role", true);
    }

}
