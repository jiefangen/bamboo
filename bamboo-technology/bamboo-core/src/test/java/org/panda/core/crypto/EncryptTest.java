package org.panda.core.crypto;

import org.junit.jupiter.api.Test;
import org.panda.core.crypto.md5.Md5Encryptor;

public class EncryptTest {

    @Test
    void md5EncryptorTest() {
        Md5Encryptor encryptor = new Md5Encryptor();
        System.out.println(encryptor.encrypt("123456"));
    }

}
