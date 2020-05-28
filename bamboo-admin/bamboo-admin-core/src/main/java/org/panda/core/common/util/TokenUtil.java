package org.panda.core.common.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * Token工具类
 *
 * @author jiefangen
 * @since JDK 1.8  2020/5/25
 **/
public class TokenUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(TokenUtil.class);

    private static final long EXPIRE_TIME = 15 * 60 * 1000; // 15分钟后失效
    private static final String TOKEN_SECRET = "bambooadmin";

    /**
     * 签名生成
     */
    public static String sign(String username) {
        Date expiresAt = new Date(System.currentTimeMillis() + EXPIRE_TIME);
        String token = JWT.create()
                .withIssuer("auth0")
                .withClaim("username", username)
                .withExpiresAt(expiresAt)
                // 使用了HMAC256加密算法。
                .sign(Algorithm.HMAC256(TOKEN_SECRET));
        return token;
    }

    /**
     * 签名验证
     */
    public static boolean verify(String token) throws Exception{
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(TOKEN_SECRET)).withIssuer("auth0").build();
        DecodedJWT jwt = verifier.verify(token);
        LOGGER.debug("Token认证通过：username: {}, {}{}", jwt.getClaim("username").asString(), "过期时间：", jwt.getExpiresAt());
        return true;
    }
}
