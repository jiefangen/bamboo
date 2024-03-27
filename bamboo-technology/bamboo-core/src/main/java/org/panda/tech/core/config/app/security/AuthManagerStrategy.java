package org.panda.tech.core.config.app.security;

import org.panda.tech.core.web.restful.RestfulResult;

/**
 * 认证授权管理策略
 *
 * @author fangen
 **/
public interface AuthManagerStrategy {

    /**
     * 获取服务授权token
     *
     * @param server 可访问的服务名
     * @param username 账户
     * @param password 密码
     * @return 认证授权token
     */
    RestfulResult<String> getAuthToken(String server, String username, String password);

    /**
     * 获取服务授权token
     *
     * @param secretKey 密钥
     * @param credentials 账户凭证
     * @param server 可访问的服务名
     * @return 认证授权token
     */
    RestfulResult<String> getTokenByCredentials(String secretKey, String credentials, String server);

    /**
     * 服务授权验证
     *
     * @param token jwt
     * @param service 服务api
     * @return 验证结果
     */
    boolean verification(String token, String service);

}
