package org.panda.business.helper.app.common.utils;

import org.panda.bamboo.common.constant.basic.Strings;
import org.panda.bamboo.common.util.lang.StringUtil;
import org.panda.tech.core.util.PasswordUtil;

/**
 * App密码工具类
 *
 * @author fangen
 * @since JDK 11 2024/7/29
 **/
public class AppPassUtils {
    /**
     * 随机生成盐
     */
    public static String getRandomSalt(){
        return StringUtil.randomLetters(36, Strings.EMPTY);
    }

    /**
     * 依据生成的盐加密
     */
    public static String encryptPassword(String password, String salt){
        return PasswordUtil.encryptPassword(password, salt);
    }
}
