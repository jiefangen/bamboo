package org.panda.business.helper.app.test.codegen;

import org.junit.jupiter.api.Test;
import org.panda.business.helper.app.test.HelperAppApplicationTest;

public class MybatisCodeGenTest extends HelperAppApplicationTest {

    @Test
    void codeGen() {
        MybatisCodeGen mybatisCodeGen = new MybatisCodeGen();
        mybatisCodeGen.codeGenerator("app_user_wechat");
    }
}
