package org.panda.business.admin.infrastructure.security.shiro;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.panda.business.admin.infrastructure.security.cors.CorsFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * shiro配置类
 *
 * @author jiefangen
 * @since JDK 1.8  2020/5/11
 **/
@Configuration
public class ShiroConfig {
    @Bean
    public AdminRealm adminRealm() {
        AdminRealm adminRealm = new AdminRealm();
        adminRealm.setCachingEnabled(true);
        //启用身份验证缓存，即缓存AuthenticationInfo信息，默认false
        adminRealm.setAuthenticationCachingEnabled(true);
        //缓存AuthenticationInfo信息的缓存名称 在ehcache-shiro.xml中有对应缓存的配置
        adminRealm.setAuthenticationCacheName("authenticationCache");
        //启用授权缓存，即缓存AuthorizationInfo信息，默认false
        adminRealm.setAuthorizationCachingEnabled(true);
        //缓存AuthorizationInfo信息的缓存名称  在ehcache-shiro.xml中有对应缓存的配置
        adminRealm.setAuthorizationCacheName("authorizationCache");

        // 使用credentialsMatcher加密算法类来验证密文
        adminRealm.setCredentialsMatcher(hashedCredentialsMatcher());
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

    /**
     * 开启shiro aop注解支持
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    /**
     * Shiro配置了放行哪些资源，访问哪些需要什么权限等
     */
    @Bean
    public FilterRegistrationBean replaceTokenFilter(){
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setDispatcherTypes(DispatcherType.REQUEST);
        registration.setFilter( new CorsFilter());
        registration.addUrlPatterns("/*");
        registration.setName("CrossFilter");
        registration.setOrder(1);
        return registration;
    }

    /**
     * Shiro过滤器配置防止中文请求拦截
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(CNInvalidRequestFilter invalidRequestFilter) {
        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
        shiroFilter.setSecurityManager(securityManager());
        shiroFilter.setLoginUrl("/doLogin");
        shiroFilter.setSuccessUrl("/home");
        shiroFilter.setUnauthorizedUrl("/unauthorizedUrl");
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        // 只允许认证用户访问
        filterChainDefinitionMap.put("/auth/**", "authc");
        // 允许匿名访问
//        filterChainDefinitionMap.put("/index", "anon");
        shiroFilter.setFilterChainDefinitionMap(filterChainDefinitionMap);
        Map<String, Filter> filters = new LinkedHashMap<>();
        filters.put("invalid", invalidRequestFilter);
        shiroFilter.setFilters(filters);
        List<String> globalFilters = new LinkedList<>();
        globalFilters.add("invalid");
        shiroFilter.setGlobalFilters(globalFilters);
        return shiroFilter;
    }
}
