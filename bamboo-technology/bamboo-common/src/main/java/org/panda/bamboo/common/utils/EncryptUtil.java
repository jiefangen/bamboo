package org.panda.bamboo.common.utils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.panda.bamboo.common.constant.StringsConstant;
import org.springframework.util.DigestUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * 加密工具类
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
                return IOUtils.toByteArray((Reader) source, StringsConstant.ENCODING_UTF8);
            } else if (source instanceof byte[]) {
                return (byte[]) source;
            } else {
                return source.toString().getBytes();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
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

}
