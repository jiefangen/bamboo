package org.panda.business.admin.common.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.apache.commons.lang3.StringUtils;
import org.panda.business.admin.common.constant.SystemConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Token工具类
 *
 * @author jiefangen
 * @since JDK 1.8  2020/5/25
 **/
public class TokenUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(TokenUtil.class);

    private static final long EXPIRE_TIME = 1 * 60 * 60 * 1000L; // 1小时后失效
    private static final String TOKEN_SECRET = "BAMBOO-ADMIN";

    /**
     * 签名生成
     */
    public static String sign(String username) {
        Date expiresAt = new Date(System.currentTimeMillis() + EXPIRE_TIME);
        // 设置头部信息
        Map<String, Object> header = new HashMap<>(2);
        header.put("Type", "Jwt");
        header.put("alg", "HS256");
        String token = JWT.create()
                .withHeader(header)
                .withIssuer("auth0")
                .withClaim("username", username)
                // 生成的token可以不带过期时间，使用redis进行过期管理，续期功能等
                .withExpiresAt(expiresAt)
                // 使用了HMAC256加密算法。
                .sign(Algorithm.HMAC256(TOKEN_SECRET));
        return token;
    }

    /**
     * 签名验证
     */
    public static boolean verify(String token) {
        Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
        JWTVerifier verifier = JWT.require(algorithm).withIssuer("auth0").build();
        DecodedJWT jwt = verifier.verify(token);
        LOGGER.debug( "{}：username: {}, {}", SystemConstants.VIA_TOKEN_INTERCEPTOR,
                jwt.getClaim("username").asString(), "expire date：", DateUtil.formatLong(jwt.getExpiresAt()));
        return true;
    }

    /**
     * 获取token用户名
     *
     * @param token 签名
     * @return 用户名
     */
    public static String getUsername(String token) {
        if (StringUtils.isNotBlank(token)) {
            try {
                DecodedJWT jwt = JWT.decode(token);
                String username = jwt.getClaim("username").asString();
                return username;
            } catch (JWTDecodeException e) {
                // do nothing
            }
        }
        return null;
    }
}
