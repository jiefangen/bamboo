package org.panda.business.helper.app.infrastructure.security.encrypt;

import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

/**
 * Shiro加密器
 *
 * @author fangen
 * @since 2020/5/13
 **/
public class ShiroEncrypt {

    private RandomNumberGenerator randomNumberGenerator = new SecureRandomNumberGenerator();
    private String algorithmName = "md5";
    private int hashIterations = 2;

    /**
     * 随机生成盐
     */
    public String getRandomSalt(){
        return randomNumberGenerator.nextBytes().toHex();
    }

    /**
     * 依据生成的盐加密
     */
    public String encryptPassword(String password, String salt){
        return new SimpleHash(algorithmName, password, ByteSource.Util.bytes(salt), hashIterations).toHex();
    }

    public String getAlgorithmName() {
        return algorithmName;
    }

    public int getHashIterations() {
        return hashIterations;
    }
}
