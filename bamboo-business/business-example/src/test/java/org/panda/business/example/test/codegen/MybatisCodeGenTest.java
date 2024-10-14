package org.panda.business.example.test.codegen;

import org.junit.jupiter.api.Test;
import org.panda.business.example.test.ExampleApplicationTest;
import org.springframework.beans.factory.annotation.Autowired;

public class MybatisCodeGenTest extends ExampleApplicationTest {

    @Autowired
    private MybatisCodeGen mybatisCodeGen;

    @Test
    void codeGen() {
//        mybatisCodeGen.codeGenerator("sys_user", "sys_role");
        mybatisCodeGen.codeGenerator("sys_user_role", true);
    }
}
