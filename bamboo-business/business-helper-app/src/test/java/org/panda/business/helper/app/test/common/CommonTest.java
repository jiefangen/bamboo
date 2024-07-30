package org.panda.business.helper.app.test.common;

import org.junit.jupiter.api.Test;
import org.panda.bamboo.common.util.lang.StringUtil;
import org.panda.business.helper.app.common.utils.AppPassUtils;
import org.panda.tech.core.util.PasswordUtil;

public class CommonTest {

    @Test
    void encrypt() {
        String salt = AppPassUtils.getRandomSalt();
        System.out.println("randomSalt: " + salt + " " + salt.length());
        String encodedPassword = AppPassUtils.encryptPassword("123456", salt);
        System.out.println("encodedPassword: " + encodedPassword + " " + encodedPassword.length());

        String originalPassword = PasswordUtil.encryptPassword("123456");
        System.out.println("originalPassword: " + originalPassword + " " + originalPassword.length());
    }

    @Test
    void randomNormal() {
        System.out.println("randomNormal: " + StringUtil.randomNormalMixeds(11));
    }

    @Test
    void cutString() {
        String str = "你好 世界！";
        System.out.println("strLength: " + str.length());
        String cutStr = StringUtil.cutForBytes(str, 10);
        System.out.println("cutStr: " + cutStr + "; length: " + cutStr.length());
        String cutStr1 = StringUtil.cut(str, 10);
        System.out.println("cutStr: " + cutStr1 + "; length: " + cutStr1.length());
    }

}
