package org.panda.bamboo.authorization;

import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;

/**
 * @author jvfagan
 * @date: 2019-03-24
 **/
public class AuthorizationServer implements AuthorizationServerConfigurer {
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        //配置token的数据源、自定义的tokenServices等信息
//        endpoints.authenticationManager(authenticationManager)
//                .tokenStore(tokenStore(dataSource))
//                .tokenServices(authorizationServerTokenServices())
//                .accessTokenConverter(accessTokenConverter())
//                .exceptionTranslator(webResponseExceptionTranslator);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()");

    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        //配置客户端认证
//        clients.withClientDetails(clientDetailsService(dataSource));
    }
}
