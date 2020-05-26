package org.panda.common;

import com.alibaba.fastjson.JSONObject;
import org.junit.Test;
import org.panda.common.domain.ResultVO;
import org.panda.core.common.constant.SystemConstant;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author jiefangen
 * @since JDK 1.8  2020/5/25
 **/
//@RunWith(SpringRunner.class)
@SpringBootTest
public class UtilTest {

    @Test
    public void test() {
        String json = JSONObject.toJSONString(
                ResultVO.getFailure(SystemConstant.ILLEGAL_TOKEN, SystemConstant.ILLEGAL_TOKEN_FAILE));
        System.out.println(json);
    }
}
