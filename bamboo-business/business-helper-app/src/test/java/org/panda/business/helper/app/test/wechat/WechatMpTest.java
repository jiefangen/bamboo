package org.panda.business.helper.app.test.wechat;

import org.junit.jupiter.api.Test;
import org.panda.bamboo.common.util.jackson.JsonUtil;
import org.panda.business.helper.app.infrastructure.thirdparty.wechat.WechatMpManager;
import org.panda.business.helper.app.test.HelperAppApplicationTest;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

public class WechatMpTest extends HelperAppApplicationTest {

    @Autowired
    private WechatMpManager wechatMpManager;

    @Test
    void decryptUserDetail() {
        String encryptedData = "aPKb6+dbD7O4Oth5EBU2qpmaOJ3+KA+yCUMBotMR8+W89S5j2PDiB5XMf3G/msgDmqt8q0FX94akz3RKZVqIyF/sGrWcCuy4uqfF4Rp6dEY3u9hvm0+shI3Qk/z7TDl7AVG3t4jAuwjDwHpTNgKvws/RGmS7eiXiXFdEQOJ24YriwWa5+DPwbqVuN0c4stNoQGyQueGNUiNad5thd8ZhRw==";
        String iv = "W+3TpSYzZt4wT9cH7z8Rsw==";
        String sessionKey = "DSeMIEFbcJE43fGIDjOsMA==";
        Map<String, Object> dataMap = wechatMpManager.decryptData(encryptedData, iv, sessionKey);
        System.out.println(JsonUtil.toJson(dataMap));
    }
}
