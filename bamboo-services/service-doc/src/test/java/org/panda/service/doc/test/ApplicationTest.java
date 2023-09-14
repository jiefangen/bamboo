package org.panda.service.doc.test;

import org.junit.jupiter.api.Test;
import org.panda.bamboo.common.util.LogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ApplicationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testController() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.GET, "/home"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello World!"));
    }

    /**
     * 测试日志输出：SLF4J日志级别从小到大trace>debug>info>warn>error
     */
    @Test
    void testLogOutput() {
        // 日志级别，由低到高
        LogUtil.trace(getClass(),"trace 级别日志");
        LogUtil.debug(getClass(),"debug 级别日志");
        LogUtil.info(getClass(),"info 级别日志");
        LogUtil.warn(getClass(),"warn 级别日志");
        LogUtil.error(getClass(),"error 级别日志");
    }

}
