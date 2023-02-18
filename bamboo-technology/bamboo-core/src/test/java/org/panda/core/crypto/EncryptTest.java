package org.panda.core.crypto;

import org.junit.jupiter.api.Test;
import org.panda.core.crypto.md5.Md5Encryptor;
import org.panda.core.crypto.sha.ShaEncryptor;

public class EncryptTest {

    @Test
    void md5EncryptorTest() {
        Md5Encryptor encryptor = new Md5Encryptor();
        System.out.println(encryptor.encrypt("123456"));
    }

    @Test
    void shaEncryptorTest() {
        ShaEncryptor encryptor = new ShaEncryptor();
        System.out.println(encryptor.encrypt("87654678"));
    }

}
