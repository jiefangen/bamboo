package org.panda.tech.security.web.access.intercept;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.panda.bamboo.common.constant.basic.Strings;
import org.panda.bamboo.common.util.LogUtil;
import org.panda.bamboo.common.util.lang.StringUtil;
import org.panda.bamboo.core.beans.ContextInitializedBean;
import org.panda.tech.core.web.mvc.servlet.mvc.method.HandlerMethodMapping;
import org.panda.tech.core.web.mvc.util.WebMvcUtil;
import org.panda.tech.security.config.annotation.*;
import org.panda.tech.security.user.UserConfigAuthority;
import org.panda.tech.security.web.AuthoritiesBizExecutor;
import org.panda.tech.security.web.access.ConfigAuthorityResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.web.method.HandlerMethod;

import java.lang.annotation.Annotation;
import java.util.*;

/**
 * WEB过滤器调用安全元数据源<br>
 * 用于获取访问资源需要具备的权限
 */
public class WebFilterInvocationSecurityMetadataSource
        implements FilterInvocationSecurityMetadataSource, ContextInitializedBean, ConfigAuthorityResolver {

    @Autowired
    private HandlerMethodMapping handlerMethodMapping;
    /**
     * 映射集：方法名称-所需权限清单，用户具有权限清单中的任意一个权限，即可访问对应方法
     */
    private final Map<String, Collection<UserConfigAuthority>> methodConfigAuthoritiesMapping = new HashMap<>();

    private FilterInvocationSecurityMetadataSource origin;

    private AuthoritiesBizExecutor authoritiesBizExecutor = new AuthoritiesBizExecutor() {
    };

    public void setOrigin(FilterInvocationSecurityMetadataSource origin) {
        this.origin = origin;
    }

    public WebFilterInvocationSecurityMetadataSource() {
    }

    public WebFilterInvocationSecurityMetadataSource(AuthoritiesBizExecutor authoritiesBizExecutor) {
        this.authoritiesBizExecutor = authoritiesBizExecutor;
    }

    @Override
    public void afterInitialized(ApplicationContext context) {
        this.handlerMethodMapping.getAllHandlerMethods().forEach((action, handlerMethod) -> {
            Collection<UserConfigAuthority> authorities = getUserConfigAuthorities(handlerMethod);
            if (authorities != null) {
                String methodKey = handlerMethod.getMethod().toString();
                this.methodConfigAuthoritiesMapping.put(methodKey, authorities);
            }
        });
    }

    private Collection<UserConfigAuthority> getUserConfigAuthorities(HandlerMethod handlerMethod) {
        // 允许匿名访问，则忽略权限限定
        if (handlerMethod.getMethodAnnotation(ConfigAnonymous.class) != null) {
            return null;
        }
        Collection<UserConfigAuthority> authorities = new ArrayList<>();
        String url = WebMvcUtil.getRequestMappingUrl(handlerMethod);
        Annotation[] annotations = handlerMethod.getMethod().getAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation instanceof ConfigAuthority) {
                ConfigAuthority configAuthority = (ConfigAuthority) annotation;
                authorities.add(new UserConfigAuthority(configAuthority.type(), configAuthority.rank(),
                        configAuthority.app(), configAuthority.permission(), configAuthority.intranet()));
            } else if (annotation instanceof ConfigPermission) {
                ConfigPermission configPermission = (ConfigPermission) annotation;
                authorities.add(new UserConfigAuthority(configPermission.type(), configPermission.rank(),
                        configPermission.app(), getDefaultPermission(url), configPermission.intranet()));
            } else if (annotation instanceof ConfigAuthorities) {
                ConfigAuthorities configAuthorities = (ConfigAuthorities) annotation;
                for (ConfigAuthority configAuthority : configAuthorities.value()) {
                    authorities.add(new UserConfigAuthority(configAuthority.type(), configAuthority.rank(),
                            configAuthority.app(), configAuthority.permission(), configAuthority.intranet()));
                }
            } else if (annotation instanceof ConfigPermissions) {
                ConfigPermissions configPermissions = (ConfigPermissions) annotation;
                for (ConfigPermission configPermission : configPermissions.value()) {
                    authorities.add(new UserConfigAuthority(configPermission.type(), configPermission.rank(),
                            configPermission.app(), getDefaultPermission(url), configPermission.intranet()));
                }
            }
        }
        if (authorities.isEmpty()) { // 没有配置权限限定，则拒绝所有访问
            authorities.add(UserConfigAuthority.ofDenyAll());
        } else {
            StringBuilder authStr = new StringBuilder();
            authorities.forEach(authority -> {
                authStr.append(Strings.COMMA).append(authority.toString());
            });
            authStr.delete(0, 1);
            LogUtil.info(getClass(), "Config authorities: {} => {}", url, authStr.toString());
            // 权限集业务扩展操作
            if (StringUtil.antPathMatchOneOf(url, authoritiesBizExecutor.getUrlPatterns())) {
                authoritiesBizExecutor.setApiConfigAuthoritiesMapping(url, authorities);
            }
        }
        return authorities;
    }

    private String getDefaultPermission(String url) {
        // 确保头尾都有/
        url = StringUtils.wrapIfMissing(url, Strings.SLASH);
        // 移除可能包含的路径变量
        if (url.endsWith("/{id}/")) { // 以路径变量id结尾的，默认视为detail
            url = url.replaceAll("/\\{id\\}/", "/detail/");
        }
        url = url.replaceAll("/\\{[^}]*\\}/", Strings.SLASH);
        // 去掉头尾的/
        url = StringUtils.strip(url, Strings.SLASH);
        // 替换中间的/为_
        return url.replaceAll(Strings.SLASH, Strings.UNDERLINE);
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        Collection<ConfigAttribute> configAttributes = new ArrayList<>();
        this.methodConfigAuthoritiesMapping.values().forEach(configAttributes::addAll);
        return configAttributes;
    }

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        Collection<ConfigAttribute> attributes = null;
        if (this.origin != null) {
            attributes = this.origin.getAttributes(object);
        }
        if (supports(attributes)) {
            attributes = attributes == null ? new HashSet<>() : new HashSet<>(attributes);
            FilterInvocation fi = (FilterInvocation) object;
            HandlerMethod handlerMethod = this.handlerMethodMapping.getHandlerMethod(fi.getRequest());
            if (handlerMethod != null) {
                Collection<UserConfigAuthority> configAttributes = getConfigAuthorities(handlerMethod);
                if (configAttributes != null) {
                    attributes.addAll(configAttributes);
                } else { // 至少加入一个没有权限限制的必备权限，以标记进行过处理
                    attributes.add(new UserConfigAuthority());
                }
            }
        }
        return attributes;
    }

    private Collection<UserConfigAuthority> getConfigAuthorities(HandlerMethod handlerMethod) {
        String methodKey = handlerMethod.getMethod().toString();
        return this.methodConfigAuthoritiesMapping.get(methodKey);
    }

    private boolean supports(Collection<ConfigAttribute> originalAttributes) {
        // 原始配置属性为空，一定不是登录才能访问，不支持
        if (CollectionUtils.isEmpty(originalAttributes)) {
            return false;
        }
        for (ConfigAttribute attribute : originalAttributes) {
            // 原始配置属性包含不限制访问，说明也不是登录才能访问，不支持
            if ("permitAll".equals(attribute.getAttribute()) || "permitAll".equals(attribute.toString())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Collection<UserConfigAuthority> resolveConfigAuthorities(String uri, HttpMethod method) {
        HandlerMethod handlerMethod = this.handlerMethodMapping.getHandlerMethod(uri, method);
        if (handlerMethod == null) {
            LogUtil.warn(getClass(), "There is not handlerMethod for {}->{}", method.name(), uri);
            return null;
        }
        return getConfigAuthorities(handlerMethod);
    }

}
