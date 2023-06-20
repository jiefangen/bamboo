package org.panda.business.adminv1.test.codegen;

import org.junit.jupiter.api.Test;
import org.panda.business.adminv1.test.AdminV1ApplicationTest;

public class MybatisCodeGenTest extends AdminV1ApplicationTest {

    @Test
    void codeGen() {
        MybatisCodeGen mybatisCodeGen = new MybatisCodeGen();
//        mybatisCodeGen.codeGenerator("sys_user", "sys_role");
        mybatisCodeGen.codeGenerator("sys_user_role", true);
    }

}
