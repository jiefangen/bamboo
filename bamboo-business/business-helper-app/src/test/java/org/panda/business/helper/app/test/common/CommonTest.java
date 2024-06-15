package org.panda.business.helper.app.test.common;

import org.junit.jupiter.api.Test;
import org.panda.bamboo.common.util.lang.StringUtil;

public class CommonTest {

//    private final ShiroEncrypt shiroEncrypt = new ShiroEncrypt();

//    @Test
//    void encrypt() {
//        String salt = shiroEncrypt.getRandomSalt();
//        System.out.println("randomSalt: " + salt + " " + salt.length());
//        String encodedPassword = shiroEncrypt.encryptPassword("123456", salt);
//        System.out.println("encodedPassword: " + encodedPassword+ " " + salt.length());
//    }

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
