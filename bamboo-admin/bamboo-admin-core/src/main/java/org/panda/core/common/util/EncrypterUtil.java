package org.panda.core.common.util;

import org.panda.common.encrypter.ShiroEncrypter;
import org.panda.core.modules.system.domain.po.UserPO;

/**
 * 加密器工具类
 *
 * @author jiefangen
 * @since JDK 1.8  2020/5/13
 **/
public class EncrypterUtil {
    ShiroEncrypter shiroEncrypter = new ShiroEncrypter();

    public void encrypterPwd(UserPO user){
        String salt = shiroEncrypter.getSalt();
        user.setSalt(salt);
        user.setPassword(shiroEncrypter.encryptPassword(user.getPassword(), salt));
    }
}
