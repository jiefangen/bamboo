package org.panda.tech.core.crypto.md5;

import org.panda.bamboo.common.util.EncryptUtil;
import org.panda.tech.core.crypto.Encryptor;

/**
 * MD5算法加密器（不可逆）
 * 算法简介：MD5是一种哈希函数，将任意长度的二进制数据映射为固定长度的输出（通常为128位32字节）。
 * 使用场景：数据完整性校验、密码加密、数字签名等。
 *
 * @author fangen
 */
public class Md5Encryptor implements Encryptor {

    @Override
    public String encrypt(Object source) {
        return EncryptUtil.encryptByMd5(source);
    }

}
