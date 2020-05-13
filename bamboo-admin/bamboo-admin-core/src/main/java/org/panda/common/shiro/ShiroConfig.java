package org.panda.common.shiro;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;

/**
 * shiro配置类
 *
 * @author jvfagan
 * @since JDK 1.8  2020/5/11
 **/
@Configuration
public class ShiroConfig {
    @Bean
    public AdminRealm adminRealm() {
        AdminRealm adminRealm = new AdminRealm();
        // 使用credentialsMatcher加密算法类来验证密文
        adminRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        adminRealm.setCachingEnabled(false);
        return adminRealm;
    }

    @Bean(name = "credentialsMatcher")
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName("MD5");// 散列算法, 与注册时使用的散列算法相同
        hashedCredentialsMatcher.setHashIterations(2);// 散列次数, 与注册时使用的散列册数相同
        hashedCredentialsMatcher.setStoredCredentialsHexEncoded(true);// 生成16进制, 与注册时的生成格式相同
        return hashedCredentialsMatcher;
    }

    /**
     * shiro 安全管理器设置
     */
    @Bean
    public DefaultWebSecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(adminRealm());
        return securityManager;
    }

    @Bean
    public ShiroFilterChainDefinition shiroFilterChainDefinition() {
        LinkedHashMap<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        filterChainDefinitionMap.put("/index", "anon");
        filterChainDefinitionMap.put("/system/user/doLogin","anon");
        filterChainDefinitionMap.put("/css/**", "anon");
        filterChainDefinitionMap.put("/js/**", "anon");
        filterChainDefinitionMap.put("/fonts/**", "anon");
        filterChainDefinitionMap.put("/img/**", "anon");
        filterChainDefinitionMap.put("/system/user/logout", "logout");
        filterChainDefinitionMap.put("/**", "authc");
        DefaultShiroFilterChainDefinition definition = new DefaultShiroFilterChainDefinition();
        definition.addPathDefinitions(filterChainDefinitionMap);
        return definition;
    }

    /**
     * 开启shiro aop注解支持
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }
}
