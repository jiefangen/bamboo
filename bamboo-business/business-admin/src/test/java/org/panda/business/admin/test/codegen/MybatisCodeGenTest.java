package org.panda.business.admin.test.codegen;

import org.junit.jupiter.api.Test;
import org.panda.business.admin.test.AdminApplicationTest;

public class MybatisCodeGenTest extends AdminApplicationTest {

    @Test
    void codeGen() {
        MybatisCodeGen mybatisCodeGen = new MybatisCodeGen();
        mybatisCodeGen.codeGenerator("sys_dictionary", "sys_dictionary_data");
//        mybatisCodeGen.codeGenerator("sys_parameter", true);
    }
}
