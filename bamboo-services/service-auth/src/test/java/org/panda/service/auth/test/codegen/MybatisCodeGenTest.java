package org.panda.service.auth.test.codegen;

import org.junit.jupiter.api.Test;
import org.panda.service.auth.test.AuthServiceApplicationTest;

public class MybatisCodeGenTest extends AuthServiceApplicationTest {

    @Test
    void codeGen() {
        MybatisCodeGen mybatisCodeGen = new MybatisCodeGen();
//        mybatisCodeGen.codeGenerator("auth_account", "auth_role", "auth_permission", "app_server");
        mybatisCodeGen.codeGenerator("auth_role_permission", true);
    }
}
