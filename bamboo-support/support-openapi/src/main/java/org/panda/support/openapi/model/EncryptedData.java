package org.panda.support.openapi.model;

/**
 * 加密数据体
 *
 * @author fangen
 * @since JDK 11 2024/6/30
 **/
public class EncryptedData {
    /**
     * 包括敏感数据在内的完整用户信息的加密数据
     */
    private String encryptedData;
    /**
     * 加密算法的初始向量
     */
    private String iv;

    public String getEncryptedData() {
        return encryptedData;
    }

    public void setEncryptedData(String encryptedData) {
        this.encryptedData = encryptedData;
    }

    public String getIv() {
        return iv;
    }

    public void setIv(String iv) {
        this.iv = iv;
    }
}
