package org.panda.business.official.test.codegen;

import org.junit.jupiter.api.Test;
import org.panda.business.official.test.OfficialApplicationTest;
import org.springframework.beans.factory.annotation.Autowired;

public class MybatisCodeGenTest extends OfficialApplicationTest {

    @Autowired
    private MybatisCodeGen mybatisCodeGen;

    @Test
    void codeGen() {
        // 代码生成器默认带有service类
        mybatisCodeGen.codeGenerator("sys_user");
        // 可控制不自动生成service类
//        mybatisCodeGen.codeGenerator("sys_user", false);
    }
}
