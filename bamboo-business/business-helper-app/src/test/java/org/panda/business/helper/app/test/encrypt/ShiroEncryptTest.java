package org.panda.business.helper.app.test.encrypt;

import org.junit.jupiter.api.Test;
import org.panda.bamboo.common.util.lang.StringUtil;

public class ShiroEncryptTest {

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
}
