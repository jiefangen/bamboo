package org.panda.business.admin.common.utils;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.panda.business.admin.infrastructure.security.encrypt.ShiroEncrypt;
import org.panda.business.admin.modules.system.model.po.UserPO;

/**
 * 加密器工具类
 *
 * @author jiefangen
 * @since JDK 1.8  2020/5/13
 **/
public class EncryptUtil {

   private ShiroEncrypt shiroEncrypt = new ShiroEncrypt();

    /**
     * 新增用户加密,盐是随机生成
     * @param user
     */
    public void encryptedPwd(UserPO user){
        String salt = shiroEncrypt.getSalt();
        user.setSalt(salt);
        user.setPassword(shiroEncrypt.encryptPassword(user.getPassword(), salt));
    }

    /**
     * 修改用户加密,盐是已有的
     * @param password
     * @param salt
     * @return
     */
    public String encryptedPwd(String password, String salt){
         return new SimpleHash(shiroEncrypt.getAlgorithmName(), password,
                ByteSource.Util.bytes(salt), shiroEncrypt.getHashIterations()).toHex();
    }

}
