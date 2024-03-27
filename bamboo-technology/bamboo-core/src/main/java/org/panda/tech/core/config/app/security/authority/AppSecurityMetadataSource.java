package org.panda.tech.core.config.app.security.authority;

import org.panda.bamboo.common.constant.basic.Strings;
import org.panda.bamboo.common.util.LogUtil;
import org.panda.bamboo.common.util.lang.StringUtil;
import org.panda.bamboo.core.beans.ContextInitializedBean;
import org.panda.tech.core.config.annotation.GrantAuthority;
import org.panda.tech.core.web.config.security.WebSecurityProperties;
import org.panda.tech.core.web.mvc.servlet.mvc.method.HandlerMethodMapping;
import org.panda.tech.core.web.mvc.util.WebMvcUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.method.HandlerMethod;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 应用安全元数据源
 *
 * @author fangen
 **/
public class AppSecurityMetadataSource implements ContextInitializedBean {

    /**
     * 映射集：方法名称-所需权限清单，用户具有权限清单中的任意一个权限，即可访问对应方法
     */
    private final Map<String, Collection<AppConfigAuthority>> methodConfigAuthoritiesMapping = new HashMap<>();

    private AuthoritiesAppExecutor authoritiesAppExecutor;

    @Autowired
    private HandlerMethodMapping handlerMethodMapping;
    @Autowired
    private WebSecurityProperties securityProperties;

    public AppSecurityMetadataSource(AuthoritiesAppExecutor authoritiesAppExecutor) {
        this.authoritiesAppExecutor = authoritiesAppExecutor;
    }

    @Override
    public void afterInitialized(ApplicationContext context) throws Exception {
        this.handlerMethodMapping.getAllHandlerMethods().forEach((action, handlerMethod) -> {
            Collection<AppConfigAuthority> authorities = getAppConfigAuthorities(handlerMethod);
            if (authorities != null) {
                String methodKey = handlerMethod.getMethod().toString();
                this.methodConfigAuthoritiesMapping.put(methodKey, authorities);
            }
        });
        if (this.authoritiesAppExecutor != null) {
            this.authoritiesAppExecutor.execute();
        }
    }

    private Collection<AppConfigAuthority> getAppConfigAuthorities(HandlerMethod handlerMethod) {
        Collection<AppConfigAuthority> authorities = new ArrayList<>();
        String url = WebMvcUtil.getRequestMappingUrl(handlerMethod);
        Annotation[] annotations = handlerMethod.getMethod().getAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation instanceof GrantAuthority) {
                GrantAuthority configAuthority = (GrantAuthority) annotation;
                String[] permissions = configAuthority.permission();
                String permission = Strings.EMPTY;
                if (permissions != null) {
                    permission = String.join(Strings.COMMA, permissions);
                }
                authorities.add(new AppConfigAuthority(configAuthority.type(), configAuthority.rank(),
                        configAuthority.app(), permission, configAuthority.mode().name()));
            }
        }
        if (authorities.isEmpty()) {
            // 没有权限注解且允许匿名访问无权限注解，则允许所有请求匿名访问
            if (this.securityProperties.isAnonymousWithoutAnnotation()) {
                authorities.add(new AppConfigAuthority());
            } else { // 拒绝所有访问
                authorities.add(AppConfigAuthority.ofDenyAll());
            }
        } else {
            StringBuilder authStr = new StringBuilder();
            authorities.forEach(authority -> {
                authStr.append(Strings.COMMA).append(authority.toString());
            });
            authStr.delete(0, 1);
            LogUtil.info(getClass(), "Config authorities: {} => {}", url, authStr.toString());
        }

        if (this.authoritiesAppExecutor != null) {
            // 匹配资源加入到认证权限集合中
            if (StringUtil.antPathMatchOneOf(url, this.authoritiesAppExecutor.getUrlPatterns())) {
                this.authoritiesAppExecutor.setApiConfigAuthoritiesMapping(url, authorities);
            }
        }
        return authorities;
    }

}
