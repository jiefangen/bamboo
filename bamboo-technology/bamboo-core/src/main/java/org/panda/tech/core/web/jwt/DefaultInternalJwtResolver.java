package org.panda.tech.core.web.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.lang3.StringUtils;
import org.panda.bamboo.common.constant.basic.Strings;
import org.panda.bamboo.common.util.LogUtil;
import org.panda.bamboo.common.util.jackson.JacksonUtil;
import org.panda.bamboo.common.util.lang.CollectionUtil;
import org.panda.bamboo.core.beans.ContextInitializedBean;
import org.panda.tech.core.jwt.InternalJwtConfiguration;
import org.springframework.context.ApplicationContext;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 默认内部JWT解决器实现
 */
public class DefaultInternalJwtResolver implements InternalJwtResolver, ContextInitializedBean {

    private static final String JWT_PREFIX = "jwt:";

    private Map<String, InternalJwtConfiguration> configurationMap = new HashMap<>();
    private Map<String, JWTVerifier> verifierMap = new HashMap<>();

    @Override
    public void afterInitialized(ApplicationContext context) throws Exception {
        context.getBeansOfType(InternalJwtConfiguration.class).values().forEach(configuration -> {
            String appName = configuration.getAppName();
            if (appName == null) {
                appName = Strings.EMPTY;
            }
            this.configurationMap.put(appName, configuration);
        });
    }

    private InternalJwtConfiguration getConfiguration(String appName) {
        InternalJwtConfiguration configuration = this.configurationMap.get(appName);
        if (configuration == null) {
            configuration = this.configurationMap.get(Strings.EMPTY);
        }
        return configuration;
    }

    @Override
    public boolean isGenerable(String appName) {
        return getConfiguration(appName) != null;
    }

    @Override
    public String generate(String appName, Object source) {
        if (source != null) {
            InternalJwtConfiguration configuration = getConfiguration(appName);
            long expiredTimeMillis = System.currentTimeMillis() + configuration.getExpiredIntervalSeconds() * 1000L;
            try {
                String audienceJson = JacksonUtil.CLASSED_MAPPER.writeValueAsString(source);
                String token = JWT.create()
                        .withExpiresAt(new Date(expiredTimeMillis))
                        .withAudience(audienceJson)
                        .sign(Algorithm.HMAC256(configuration.getSecretKey()));
                // 形如：jwt:[appName]/token
                return JWT_PREFIX + appName + Strings.SLASH + token;
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    private JWTVerifier getVerifier(String appName) {
        JWTVerifier verifier = this.verifierMap.get(appName);
        if (verifier == null) {
            InternalJwtConfiguration configuration = getConfiguration(appName);
            String secretKey = configuration.getSecretKey();
            verifier = JWT.require(Algorithm.HMAC256(secretKey)).build();
            this.verifierMap.put(appName, verifier);
        }
        return verifier;
    }

    @Override
    public boolean isParsable() {
        return this.configurationMap.size() > 0;
    }

    @Override
    public <T> T parse(String jwt, Class<T> type) {
        if (jwt != null && jwt.startsWith(JWT_PREFIX)) {
            int index = jwt.indexOf(Strings.SLASH);
            if (index > 0 && index < jwt.length() - 1) {
                String appName = jwt.substring(JWT_PREFIX.length(), index);
                try {
                    JWTVerifier verifier = getVerifier(appName);
                    String token = jwt.substring(index + 1);
                    List<String> audience = verifier.verify(token).getAudience();
                    String audienceJson = CollectionUtil.getFirst(audience, null);
                    if (StringUtils.isNotBlank(audienceJson)) {
                        return JacksonUtil.CLASSED_MAPPER.readValue(audienceJson, type);
                    }
                } catch (Exception e) { // 出现任何错误均只打印日志，视为没有授权
                    LogUtil.error(getClass(), e.getMessage());
                }
            }
        }
        return null;
    }

    @Override
    public boolean verify(String jwt) {
        if (jwt != null && jwt.startsWith(JWT_PREFIX)) {
            int index = jwt.indexOf(Strings.SLASH);
            if (index > 0 && index < jwt.length() - 1) {
                String appName = jwt.substring(JWT_PREFIX.length(), index);
                JWTVerifier verifier = getVerifier(appName);
                String token = jwt.substring(index + 1);
                verifier.verify(token);
                return true;
            }
        }
        return false;
    }

}
