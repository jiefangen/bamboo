package org.panda.bamboo.common.util;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.panda.bamboo.common.constant.basic.Strings;
import org.springframework.util.DigestUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * 加密算法工具类
 *
 * @author fangen
 */
public class EncryptUtil {

    private static byte[] toBytes(Object source) {
        try {
            if (source instanceof File) {
                return FileUtils.readFileToByteArray((File) source);
            } else if (source instanceof InputStream) {
                return IOUtils.toByteArray((InputStream) source);
            } else if (source instanceof Reader) {
                return IOUtils.toByteArray((Reader) source, Strings.ENCODING_UTF8);
            } else if (source instanceof byte[]) {
                return (byte[]) source;
            } else {
                return source.toString().getBytes();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String encryptByBase64(Object source) {
        if (source != null) {
            byte[] data = toBytes(source);
            return Base64.getEncoder().encodeToString(data).replaceAll("\n", "");
        }
        return null;
    }

    public static String decryptByBase64(String encryptedText) {
        if (encryptedText != null) {
            byte[] data = Base64.getDecoder().decode(encryptedText);
            if (data != null) {
                return new String(data);
            }
        }
        return null;
    }

    public static String encryptByMd5(Object source) {
        byte[] data = toBytes(source);
        return DigestUtils.md5DigestAsHex(data);
    }

    public static String encryptBySha(Object source, String shaType) {
        try {
            byte[] data = toBytes(source);
            MessageDigest digest = MessageDigest.getInstance(shaType);
            byte[] digestBytes = digest.digest(data);
            StringBuffer result = new StringBuffer();
            for (int i = 0; i < digestBytes.length; i++) {
                String shaHex = Integer.toHexString(digestBytes[i] & 0xFF);
                if (shaHex.length() < 2) {
                    result.append(0);
                }
                result.append(shaHex);
            }
            return result.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String encryptByAes(String source, String key) {
        try {
            // 对密钥进行处理
            byte[] keyBytes = key.getBytes(Strings.ENCODING_UTF8);
            byte[] paddedKeyBytes = new byte[16];
            int keyLength = keyBytes.length;
            if (keyLength > 16) {
                keyLength = 16;
            }
            System.arraycopy(keyBytes, 0, paddedKeyBytes, 0, keyLength);
            // 初始化加密器
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec keySpec = new SecretKeySpec(paddedKeyBytes, "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(paddedKeyBytes);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
            // 进行加密
            byte[] encrypted = cipher.doFinal(source.getBytes(Strings.ENCODING_UTF8));
            // 对结果进行base64编码并返回
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String decryptByAes(String encryptedText, String key) {
        try {
            // 对密钥进行处理
            byte[] keyBytes = key.getBytes(Strings.ENCODING_UTF8);
            byte[] paddedKeyBytes = new byte[16];
            int keyLength = keyBytes.length;
            if (keyLength > 16) {
                keyLength = 16;
            }
            System.arraycopy(keyBytes, 0, paddedKeyBytes, 0, keyLength);
            // 初始化解密器
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec keySpec = new SecretKeySpec(paddedKeyBytes, "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(paddedKeyBytes);
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
            // 对数据进行base64解码并进行解密
            byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(encryptedText));
            // 返回解密结果
            return new String(decrypted, Strings.ENCODING_UTF8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String encryptByRsa(Object source, Object publicKey) {
        try {
            PublicKey pubKey = null;
            if (publicKey instanceof InputStream) { // 从文件中读取公钥
                ObjectInputStream ois = new ObjectInputStream((InputStream) publicKey);
                pubKey = (PublicKey) ois.readObject();
                ois.close();
            } else if (publicKey instanceof String) { // 从Base64串中读取公钥
                String publicKeyStr = (String) publicKey;
                byte[] pubKeyBytes = Base64.getDecoder().decode(publicKeyStr);
                KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(pubKeyBytes));
            }
            if (pubKey != null) {
                // 得到Cipher对象来实现对源数据的RSA加密
                Cipher cipher = Cipher.getInstance("RSA");
                cipher.init(Cipher.ENCRYPT_MODE, pubKey);
                byte[] data = toBytes(source);
                // 执行加密操作
                byte[] b1 = cipher.doFinal(data);
                return encryptByBase64(b1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String decryptByRsa(String encryptedText, Object privateKey) {
        try {
            PrivateKey privKey = null;
            if (privateKey instanceof InputStream) { // 从文件中读取私钥
                ObjectInputStream ois = new ObjectInputStream((InputStream) privateKey);
                privKey = (PrivateKey) ois.readObject();
                ois.close();
            } else if (privateKey instanceof String) { // 从Base64串中读取私钥
                String privateKeyStr = (String) privateKey;
                byte[] privKeyBytes = Base64.getDecoder().decode(privateKeyStr);
                KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                privKey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(privKeyBytes));
            }

            if (privKey != null) {
                // 得到Cipher对象对已用公钥加密的数据进行RSA解密
                Cipher cipher = Cipher.getInstance("RSA");
                cipher.init(Cipher.DECRYPT_MODE, privKey);
                return decryptByBase64(encryptedText);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
