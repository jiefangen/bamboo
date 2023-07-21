package org.panda.business.admin.test.codegen;

import org.junit.jupiter.api.Test;
import org.panda.business.admin.test.AdminApplicationTest;

public class MybatisCodeGenTest extends AdminApplicationTest {

    @Test
    void codeGen() {
        MybatisCodeGen mybatisCodeGen = new MybatisCodeGen();
//        mybatisCodeGen.codeGenerator("sys_user", "sys_role");
        mybatisCodeGen.codeGenerator("sys_user_token", true);
    }

}
