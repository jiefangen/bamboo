package org.panda.common.utils;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.panda.core.encrypter.ShiroEncrypter;
import org.panda.modules.system.domain.po.UserPO;

/**
 * 加密器工具类
 *
 * @author jiefangen
 * @since JDK 1.8  2020/5/13
 **/
public class EncrypterUtil {
    ShiroEncrypter shiroEncrypter = new ShiroEncrypter();

    /**
     * 新增用户加密,盐是随机生成
     * @param user
     */
    public void encrypterPwd(UserPO user){
        String salt = shiroEncrypter.getSalt();
        user.setSalt(salt);
        user.setPassword(shiroEncrypter.encryptPassword(user.getPassword(), salt));
    }

    /**
     * 修改用户加密,盐是已有的
     * @param password
     * @param salt
     * @return
     */
    public String encrypterPwd(String password, String salt){
         return new SimpleHash(shiroEncrypter.getAlgorithmName(), password,
                ByteSource.Util.bytes(salt), shiroEncrypter.getHashIterations()).toHex();
    }

}
