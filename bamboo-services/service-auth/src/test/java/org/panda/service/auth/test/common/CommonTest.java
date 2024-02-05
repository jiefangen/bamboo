package org.panda.service.auth.test.common;

import org.junit.jupiter.api.Test;
import org.panda.bamboo.common.constant.basic.Strings;
import org.panda.bamboo.common.util.lang.StringUtil;
import org.panda.tech.core.util.CommonUtil;

import java.util.HashSet;
import java.util.Set;

/**
 * 常用工具类测试
 **/
public class CommonTest {

    @Test
    void getDefaultPermission() {
        String uri = "/payment/prepare/{gateway}/{gatewayName}/{gatewayName}";
        String defaultPermission = CommonUtil.getDefaultPermission(uri);
        if (uri.contains(Strings.LEFT_BRACE) && uri.contains(Strings.RIGHT_BRACE) ) {
            defaultPermission += "_*";
        }
        System.out.println(defaultPermission);
    }

    @Test
    void antPathMatchOneOf() {
        String service = "/payment/callback/show/{gatewayName}/{terminal}";
        System.out.println(CommonUtil.countPairsOfBraces(service));
        String apiCode = CommonUtil.getDefaultPermission(service).toUpperCase();
        Set<String> permissions = new HashSet<>();
        permissions.add("PAYMENT_CALLBACK_SHOW_*_*");
        permissions.add("PAYMENT_PREPARE_*");
        boolean bool = StringUtil.antPathMatchOneOf(apiCode, permissions);
        System.out.println(bool);
    }

}
