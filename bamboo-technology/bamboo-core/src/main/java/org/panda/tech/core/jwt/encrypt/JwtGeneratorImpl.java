package org.panda.tech.core.jwt.encrypt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.lang3.StringUtils;
import org.panda.bamboo.common.constant.Profiles;
import org.panda.bamboo.common.constant.basic.Strings;
import org.panda.bamboo.common.util.EncryptUtil;
import org.panda.bamboo.common.util.jackson.JacksonUtil;
import org.panda.tech.core.jwt.encrypt.constants.JwtConstants;
import org.panda.tech.core.util.supplier.ProfileSupplier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.util.Date;

/**
 * JWT生成器实现
 */
public class JwtGeneratorImpl implements JwtGenerator {

    @Autowired(required = false)
    private JwtEncryption encryption;
    @Autowired
    private ProfileSupplier profileSupplier;
    private KeyFactory rsaKeyFactory;

    public JwtGeneratorImpl() {
        try {
            this.rsaKeyFactory = KeyFactory.getInstance(JwtConstants.ASYMMETRIC_ALGORITHM_NAME);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isAvailable() {
        return this.encryption != null;
    }

    @Override
    public String generate(String type, Object source) {
        if (isAvailable() && source != null) {
            if (StringUtils.isBlank(type)) {
                type = Strings.EMPTY;
            } else {
                Assert.isTrue(!type.contains(Strings.SLASH), () -> "The type must not contain '/'");
            }
            Algorithm algorithm = getAlgorithm(type);
            if (algorithm != null) {
                String encryptionName = this.encryption.getEncryptionName();
                Assert.isTrue(!encryptionName.contains(Strings.SLASH), () -> "The encryptionName must not contain '/'");

                String payload = this.encryption.getPayload(type);
                if (StringUtils.isBlank(payload)) {
                    payload = Strings.EMPTY;
                } else {
                    Assert.isTrue(!payload.contains(Strings.SLASH), () -> "The payload must not contain '/'");
                }

                int expiredIntervalSeconds = getExpiredIntervalSeconds(this.profileSupplier.get());
                long expiredTimeMillis = System.currentTimeMillis() + expiredIntervalSeconds * 1000L;

                try {
                    String audienceJson = JacksonUtil.CLASSED_MAPPER.writeValueAsString(source);
                    String token = JWT.create()
                            .withExpiresAt(new Date(expiredTimeMillis))
                            .withAudience(audienceJson)
                            .sign(algorithm);
                    // 形如：jwt:[encryptionName]/[payload]/token
                    return JwtConstants.JWT_PREFIX + encryptionName
                            + Strings.SLASH + payload
                            + Strings.SLASH + token;
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return null;
    }

    /**
     * 获取JWT过期时间秒数
     *
     * @param profile 运行环境
     * @return JWT的过期时间秒数
     */
    protected int getExpiredIntervalSeconds(String profile) {
        switch (profile) {
            case Profiles.LOCAL:
            case Profiles.DEV:
                return 1000;
            case Profiles.TEST:
                return 100;
            default:
                return 10;
        }
    }

    protected Algorithm getAlgorithm(String type) {
        String secretKey = this.encryption.getEncryptSecretKey(type);
        if (secretKey != null) {
            if (this.encryption.isSymmetric(type)) {
                return Algorithm.HMAC256(secretKey);
            } else {
                RSAPrivateKey privateKey = EncryptUtil.generatePrivate(this.rsaKeyFactory, secretKey);
                return Algorithm.RSA256(null, privateKey);
            }
        }
        return null;
    }

}
