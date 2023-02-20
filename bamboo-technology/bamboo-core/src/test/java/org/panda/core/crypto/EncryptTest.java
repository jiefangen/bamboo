package org.panda.core.crypto;

import org.junit.jupiter.api.Test;
import org.panda.core.crypto.aes.AesEncryptor;
import org.panda.core.crypto.md5.Md5Encryptor;
import org.panda.core.crypto.sha.ShaEncryptor;

public class EncryptTest {

    @Test
    void md5EncryptorTest() {
        Md5Encryptor encryptor = new Md5Encryptor();
        System.out.println(encryptor.encrypt("Hello World!"));
    }

    @Test
    void shaEncryptorTest() {
        ShaEncryptor encryptor = new ShaEncryptor();
        System.out.println(encryptor.encrypt("Hello World!"));
    }

    @Test
    void aesEncryptorTest() {
        AesEncryptor encryptor = new AesEncryptor();
        String source = "Hello World!";
        String key = "ajikouehnfkolkjh";
        String encryptSource = encryptor.encrypt(source, key);
        System.out.println(encryptSource);
        System.out.println("-----------------------------");
        System.out.println(encryptor.decrypt(encryptSource, key));
    }

}
