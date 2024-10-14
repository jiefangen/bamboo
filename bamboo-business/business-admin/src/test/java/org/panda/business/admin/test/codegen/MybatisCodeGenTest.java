package org.panda.business.admin.test.codegen;

import org.junit.jupiter.api.Test;
import org.panda.business.admin.test.AdminApplicationTest;
import org.springframework.beans.factory.annotation.Autowired;

public class MybatisCodeGenTest extends AdminApplicationTest {

    @Autowired
    private MybatisCodeGen mybatisCodeGen;

    @Test
    void codeGen() {
        mybatisCodeGen.codeGenerator("sys_dictionary", "sys_dictionary_data");
//        mybatisCodeGen.codeGenerator("sys_parameter", true);
    }
}
