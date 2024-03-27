package org.panda.tech.core.jwt.encrypt;

/**
 * JWT加密方
 */
public interface JwtEncryption {

    /**
     * 获取当前加密方名称，一般为当前应用名称
     *
     * @return 加密方名称
     */
    String getEncryptionName();

    /**
     * 获取拼接在jwt串中的负载信息
     *
     * @param type 业务类型
     * @return 负载信息
     */
    default String getPayload(String type) {
        return null;
    }

    /**
     * 判断指定业务类型的密钥是否对称密钥
     *
     * @param type 业务类型
     * @return 是否对称密钥
     */
    boolean isSymmetric(String type);

    /**
     * 获取加密密钥
     *
     * @param type 业务类型
     * @return 加密密钥
     */
    String getEncryptSecretKey(String type);
}
