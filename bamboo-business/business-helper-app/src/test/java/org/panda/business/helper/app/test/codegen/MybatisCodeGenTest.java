package org.panda.business.helper.app.test.codegen;

import org.junit.jupiter.api.Test;
import org.panda.business.helper.app.test.HelperAppApplicationTest;
import org.springframework.beans.factory.annotation.Autowired;

public class MybatisCodeGenTest extends HelperAppApplicationTest {

    @Autowired
    private MybatisCodeGen mybatisCodeGen;

    @Test
    void codeGen() {
        mybatisCodeGen.codeGenerator("app_user_wechat");
    }
}
