package org.panda.tech.core.crypto.sha;

import org.panda.bamboo.common.util.EncryptUtil;
import org.panda.tech.core.crypto.Encryptor;

/**
 * SHA系列算法加密器（不可逆）
 * 算法简介：SHA是一种密码学哈希函数，用于将数据转换为固定长度的散列值。
 * 使用场景：数据完整性验证、数字签名、数据加密、随机数生成等。
 *
 * @author fangen
 */
public class ShaEncryptor implements Encryptor {
    // 存在安全性缺陷，推荐弃用
    public static final String SHA_1 = "SHA-1";

    public static final String SHA_224 = "SHA-224";
    public static final String SHA_256 = "SHA-256";
    public static final String SHA_384  = "SHA-384";
    public static final String SHA_512  = "SHA-512";

    /** 最新的SHA哈希算法，安全性更高，但速度相对较慢 */
    public static final String SHA3_224 = "SHA3-224";
    public static final String SHA3_256 = "SHA3-256";
    public static final String SHA3_384 = "SHA3-384";
    public static final String SHA3_512 = "SHA3-512";

    @Override
    public String encrypt(Object source) {
        return EncryptUtil.encryptBySha(source, SHA_256);
    }

}
