package org.panda.tech.core.web.mvc.support;

import org.apache.commons.lang3.ArrayUtils;
import org.panda.bamboo.common.util.clazz.BeanUtil;
import org.panda.bamboo.common.util.lang.CollectionUtil;
import org.panda.tech.core.config.CommonProperties;
import org.panda.tech.core.web.config.WebConstants;
import org.panda.tech.core.web.mvc.cors.CorsRegistryProperties;
import org.panda.tech.core.web.mvc.cors.IgnoreNullConfigCorsProcessor;
import org.panda.tech.core.web.mvc.cors.SingleCorsConfigurationSource;
import org.panda.tech.core.web.mvc.http.converter.JacksonHttpMessageConverter;
import org.panda.tech.core.web.mvc.util.SwaggerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.*;

import java.util.*;

/**
 * WEB MVC配置器支持，可选的控制层配置均在此配置支持体系中
 * 自定义Json转换器{@link JacksonHttpMessageConverter}
 */
public abstract class WebMvcConfigurerSupport implements WebMvcConfigurer {
    @Autowired
    private CommonProperties commonProperties;
    @Autowired
    private CorsRegistryProperties corsRegistryProperties;
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private SingleCorsConfigurationSource corsConfigurationSource;
    @Autowired
    private IgnoreNullConfigCorsProcessor ignoreNullConfigCorsProcessor;

    protected final ApplicationContext getApplicationContext() {
        return this.applicationContext;
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.removeIf(converter -> {
            // 移除多余的MappingJackson2HttpMessageConverter，已被JacksonHttpMessageConverter取代
            return converter.getClass() == MappingJackson2HttpMessageConverter.class;
        });
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        Map<String, HandlerInterceptor> interceptors = getApplicationContext().getBeansOfType(HandlerInterceptor.class);
        interceptors.forEach((name, interceptor) -> {
            registry.addInterceptor(interceptor);
        });
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        if (SwaggerUtil.isEnabled(getApplicationContext())) {
            registry.addResourceHandler("/doc.html")
                    .addResourceLocations("classpath:/META-INF/resources/");
            registry.addResourceHandler("/swagger-ui.html")
                    .addResourceLocations("classpath:/META-INF/resources/");
            registry.addResourceHandler("/webjars/**")
                    .addResourceLocations("classpath:/META-INF/resources/webjars/");
        }
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 配置的应用URI均允许跨域访问
        Set<String> allowedOriginals = new HashSet<>(this.commonProperties.getAllAppUris());
        // 加入额外配置的跨域访问白名单
        CollectionUtil.addAll(allowedOriginals, this.corsRegistryProperties.getAllowedOrigins());

        CorsRegistration registration = registry
                .addMapping(this.corsRegistryProperties.getPathPattern())
                .allowedOrigins(allowedOriginals.toArray(new String[0]))
                .allowedMethods(this.corsRegistryProperties.getAllowedMethods())
                .allowedHeaders(this.corsRegistryProperties.getAllowedHeaders())
                .allowCredentials(true);
        String[] exposedHeaders = this.corsRegistryProperties.getExposedHeaders();
        Set<String> exposedHeaderSet = new HashSet<>();
        addExposedHeaders(exposedHeaderSet);
        exposedHeaders = ArrayUtils.addAll(exposedHeaders, exposedHeaderSet.toArray(new String[0]));
        registration.exposedHeaders(exposedHeaders);
        if (this.corsRegistryProperties.getMaxAge() != null) {
            registration.maxAge(this.corsRegistryProperties.getMaxAge());
        }
        CorsConfiguration corsConfiguration = BeanUtil.getFieldValue(registration, CorsConfiguration.class);
        this.corsConfigurationSource.setCorsConfiguration(corsConfiguration);
    }

    protected void addExposedHeaders(Collection<String> exposedHeaders) {
        exposedHeaders.add(WebConstants.HEADER_REDIRECT_TO);
        exposedHeaders.add(WebConstants.HEADER_LOGIN_URL);
        exposedHeaders.add(WebConstants.HEADER_ORIGINAL_REQUEST);
    }

    @Bean("corsFilter")
    public CorsFilter corsFilter() {
        CorsFilter corsFilter = new CorsFilter(this.corsConfigurationSource);
        corsFilter.setCorsProcessor(this.ignoreNullConfigCorsProcessor);
        return corsFilter;
    }

}
