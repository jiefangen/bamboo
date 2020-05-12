package org.panda;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 基础单元测试
 * @author jvfagan
 * @since JDK 1.8  2020/5/9
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class AdminApplicationTests {

    @Test
    public void classLoader(){
        System.out.println("Springboot 1.5.10 Start...");
    }
}
