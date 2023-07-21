package org.panda.tech.security.config;

import org.apache.commons.lang3.StringUtils;
import org.panda.bamboo.common.constant.basic.Strings;
import org.panda.tech.core.web.config.meta.ApiMetaProperties;
import org.panda.tech.core.web.config.security.WebSecurityProperties;
import org.panda.tech.core.web.mvc.servlet.mvc.method.HandlerMethodMapping;
import org.panda.tech.core.web.mvc.util.SwaggerUtil;
import org.panda.tech.security.access.UserAuthorityAccessDecisionManager;
import org.panda.tech.security.config.annotation.ConfigAnonymous;
import org.panda.tech.security.web.AuthoritiesBizExecutor;
import org.panda.tech.security.web.SecurityUrlProvider;
import org.panda.tech.security.web.access.AccessDeniedBusinessExceptionHandler;
import org.panda.tech.security.web.access.intercept.WebFilterInvocationSecurityMetadataSource;
import org.panda.tech.security.web.authentication.JwtAuthenticationFilter;
import org.panda.tech.security.web.authentication.WebAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * WebMvc安全配置器支持
 */
@EnableWebSecurity
public abstract class WebSecurityConfigurerSupport extends WebSecurityConfigurerAdapter {

    @Autowired
    private HandlerMethodMapping handlerMethodMapping;
    @Autowired
    private RedirectStrategy redirectStrategy;
    @Autowired
    private WebSecurityProperties securityProperties;
    @Autowired
    private ApiMetaProperties apiMetaProperties;
    @Autowired
    private AuthoritiesBizExecutor authoritiesBizExecutor;

    protected SecurityUrlProvider urlProvider = new SecurityUrlProvider() {
        // 所有方法都有默认实现，默认实例无需提供
    };

    @Autowired(required = false)
    public void setUrlProvider(SecurityUrlProvider urlProvider) {
        this.urlProvider = urlProvider;
    }

    // 获取访问资源需要具备的权限
    @Bean
    public WebFilterInvocationSecurityMetadataSource securityMetadataSource() {
        return new WebFilterInvocationSecurityMetadataSource(authoritiesBizExecutor);
    }

    // 登录用户访问资源的权限判断
    @Bean
    public UserAuthorityAccessDecisionManager accessDecisionManager() {
        return new UserAuthorityAccessDecisionManager();
    }

    // 匿名用户试图访问登录用户才能访问的资源后的错误处理
    @Bean
    public WebAuthenticationEntryPoint authenticationEntryPoint() {
        return new WebAuthenticationEntryPoint(this.urlProvider);
    }

    // 登录用户越权访问资源后的错误处理
    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new AccessDeniedBusinessExceptionHandler();
    }

    // 登出成功后的处理
    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() {
        SimpleUrlLogoutSuccessHandler handler;
        // 先查询获取自定义登出处理器bean
        if (getApplicationContext().containsBean("simpleUrlLogoutSuccessHandler")) {
            handler = getApplicationContext().getBean(SimpleUrlLogoutSuccessHandler.class);
        } else {
            handler = new SimpleUrlLogoutSuccessHandler();
        }
        handler.setRedirectStrategy(this.redirectStrategy);
        String logoutSuccessUrl = this.urlProvider.getLogoutSuccessUrl();
        if (logoutSuccessUrl != null) {
            handler.setDefaultTargetUrl(logoutSuccessUrl);
        }
        handler.setTargetUrlParameter(this.apiMetaProperties.getRedirectTargetUrlParameter());
        return handler;
    }

    @Override
    @SuppressWarnings("deprecation")
    public void init(WebSecurity web) throws Exception {
        HttpSecurity http = getHttp();

        web.addSecurityFilterChainBuilder(http).postBuildAction(() -> {
            FilterSecurityInterceptor interceptor = http.getSharedObject(FilterSecurityInterceptor.class);
            WebFilterInvocationSecurityMetadataSource metadataSource = securityMetadataSource();
            FilterInvocationSecurityMetadataSource originalMetadataSource = interceptor.getSecurityMetadataSource();
            if (!(originalMetadataSource instanceof WebFilterInvocationSecurityMetadataSource)) {
                metadataSource.setOrigin(originalMetadataSource);
            }
            interceptor.setSecurityMetadataSource(metadataSource);
            interceptor.setAccessDecisionManager(accessDecisionManager());
            // 当前spring-security版本中的父类仍然是这样调用，未来根据父类情况调整
            web.securityInterceptor(interceptor);
        });

        web.ignoring().antMatchers(getIgnoringAntPatterns().toArray(new String[0]));
    }

    /**
     * 获取安全框架忽略的URL ANT样式集合
     *
     * @return 安全框架忽略的URL ANT样式集合
     */
    protected Collection<String> getIgnoringAntPatterns() {
        Collection<String> patterns = new HashSet<>();
        if (SwaggerUtil.isEnabled(getApplicationContext())) {
            patterns.add("/doc.html");
            patterns.add("/swagger-ui.html");
            patterns.add("/webjars/**");
            patterns.add("/v2/api-docs");
            patterns.add("/swagger-resources/**");
        }

        if (this.securityProperties != null) {
            List<String> ignoringPatterns = this.securityProperties.getIgnoringPatterns();
            if (ignoringPatterns != null) {
                patterns.addAll(ignoringPatterns);
            }
        }
        return patterns;
    }

    protected final String getIgnoringAntPatternFromController(Class<?> controllerClass) {
        RequestMapping mapping = controllerClass.getAnnotation(RequestMapping.class);
        return mapping.value()[0] + "/**";
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 覆盖父类的方法实现，且不调用父类方法实现，以标记AuthenticationManager由自定义创建，避免创建多个实例
    }

    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected void configure(HttpSecurity http) throws Exception {
        // 应用登录配置器
        Collection<SecurityConfigurerAdapter> configurers = getSecurityConfigurerAdapters();
        for (SecurityConfigurerAdapter configurer : configurers) {
            http.apply(configurer);
        }
        http.addFilterAfter(new JwtAuthenticationFilter(getApplicationContext()),
                UsernamePasswordAuthenticationFilter.class);

        RequestMatcher[] anonymousMatchers = getAnonymousRequestMatchers().toArray(new RequestMatcher[0]);
        // @formatter:off
        http.authorizeRequests().requestMatchers(anonymousMatchers).permitAll().anyRequest().authenticated().and()
                .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint())
                .accessDeniedHandler(accessDeniedHandler())
                .and().logout().logoutUrl(this.urlProvider.getLogoutProcessUrl()).logoutSuccessHandler(logoutSuccessHandler())
                .deleteCookies(getLogoutClearCookies()).permitAll();
        // @formatter:on

        // 附加配置
        Map<String, WebHttpSecurityConfigurer> additionalConfigurers = getApplicationContext()
                .getBeansOfType(WebHttpSecurityConfigurer.class);
        for (WebHttpSecurityConfigurer configurer : additionalConfigurers.values()) {
            configurer.configure(http);
        }
        // 默认均开启cors，是否允许所有请求，由具体的cors配置决定
        http.cors();
        if (!this.securityProperties.isCsrfEnabled()) {
            http.csrf().disable() // 禁用CSRF，因为无状态认证不依赖于会话
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS); // 禁用会话
        }
        // 默认允许同源站点内嵌访问
        http.headers().frameOptions().sameOrigin();
    }

    @SuppressWarnings({ "rawtypes" })
    protected Collection<SecurityConfigurerAdapter> getSecurityConfigurerAdapters() {
        return getApplicationContext().getBeansOfType(SecurityConfigurerAdapter.class).values();
    }

    /**
     * 获取经过安全框架控制，允许匿名访问的请求匹配器集合
     *
     * @return 可匿名访问的请求匹配器集合
     */
    protected Collection<RequestMatcher> getAnonymousRequestMatchers() {
        List<RequestMatcher> matchers = new ArrayList<>();
        matchers.add(new AntPathRequestMatcher("/error/**"));
        // 打开登录表单页面和登出的请求始终可匿名访问
        // 注意：不能将请求URL加入忽略清单中，如果加入，则请求将无法经过安全框架过滤器处理
        String loginFormUrl = this.urlProvider.getDefaultLoginUrl();
        if (loginFormUrl.startsWith(Strings.SLASH)) { // 相对路径才需要添加到匿名清单中
            int index = loginFormUrl.indexOf(Strings.QUESTION);
            if (index > 0) { // 去掉登录表单地址中可能的参数
                loginFormUrl = loginFormUrl.substring(0, index);
            }
            matchers.add(new AntPathRequestMatcher(loginFormUrl, HttpMethod.GET.name()));
        }
        String logoutProcessUrl = this.urlProvider.getLogoutProcessUrl();
        if (logoutProcessUrl.startsWith(Strings.SLASH)) { // 相对路径才需要添加到匿名清单中
            matchers.add(new AntPathRequestMatcher(logoutProcessUrl));
        }

        this.handlerMethodMapping.getAllHandlerMethods().forEach((action, handlerMethod) -> {
            Method method = handlerMethod.getMethod();
            if (Modifier.isPublic(method.getModifiers())) {
                ConfigAnonymous configAnonymous = method.getAnnotation(ConfigAnonymous.class);
                if (configAnonymous != null) {
                    HttpMethod httpMethod = action.getMethod();
                    String methodValue = httpMethod == null ? null : httpMethod.name();
                    RequestMatcher matcher;
                    String regex = configAnonymous.regex();
                    if (StringUtils.isNotBlank(regex)) { // 指定了正则表达式，则采用正则匹配器
                        matcher = new RegexRequestMatcher(regex, methodValue, true);
                    } else {
                        String pattern = action.getUri().replaceAll("\\{\\S+\\}", Strings.ASTERISK);
                        matcher = new AntPathRequestMatcher(pattern, methodValue);
                    }
                    matchers.add(matcher);
                }
            }
        });

        return matchers;
    }

    protected String[] getLogoutClearCookies() {
        return new String[] { "JSESSIONID", "SESSION" };
    }

}
