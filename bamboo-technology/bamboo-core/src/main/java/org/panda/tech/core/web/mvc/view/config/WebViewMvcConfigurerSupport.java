package org.panda.tech.core.web.mvc.view.config;

import org.apache.commons.lang3.StringUtils;
import org.panda.bamboo.common.constant.basic.Strings;
import org.panda.bamboo.common.util.lang.StringUtil;
import org.panda.tech.core.web.mvc.servlet.filter.ForbidAccessFilter;
import org.panda.tech.core.web.mvc.servlet.resource.AntPatternResourceResolver;
import org.panda.tech.core.web.mvc.support.WebMvcConfigurerSupport;
import org.panda.tech.core.web.context.function.WebContextPathPredicate;
import org.panda.tech.core.web.mvc.view.exception.resolver.ViewDefaultExceptionResolver;
import org.panda.tech.core.web.mvc.view.exception.resolver.ViewErrorPathProperties;
import org.sitemesh.builder.SiteMeshFilterBuilder;
import org.sitemesh.config.ConfigurableSiteMeshFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

import javax.servlet.DispatcherType;
import java.util.List;

/**
 * WEB视图层MVC配置支持
 */
public abstract class WebViewMvcConfigurerSupport extends WebMvcConfigurerSupport {

    @Autowired
    private WebMvcProperties mvcProperties;
    @Autowired
    private WebProperties webProperties;
    @Autowired
    private ViewErrorPathProperties pathProperties;
    @Autowired
    private WebContextPathPredicate webContextPathPredicate;

    @Bean
    public FilterRegistrationBean<ForbidAccessFilter> forbidAccessFilter() {
        FilterRegistrationBean<ForbidAccessFilter> frb = new FilterRegistrationBean<>();
        frb.setFilter(new ForbidAccessFilter());
        frb.addUrlPatterns("*.jsp");
        frb.setOrder(Ordered.HIGHEST_PRECEDENCE + 1);
        return frb;
    }

    @Bean
    public FilterRegistrationBean<ConfigurableSiteMeshFilter> siteMeshFilter() {
        FilterRegistrationBean<ConfigurableSiteMeshFilter> frb = new FilterRegistrationBean<>();
        frb.setFilter(new ConfigurableSiteMeshFilter() {
            @Override
            protected void applyCustomConfiguration(SiteMeshFilterBuilder builder) {
                buildSiteMeshFilter(builder);
            }
        });
        frb.addUrlPatterns("/*");
        frb.setDispatcherTypes(DispatcherType.FORWARD, DispatcherType.REQUEST, DispatcherType.ERROR);
        frb.setOrder(Ordered.LOWEST_PRECEDENCE - 1);
        return frb;
    }

    protected void buildSiteMeshFilter(SiteMeshFilterBuilder builder) {
        builder.addExcludedPath("/swagger-ui.html");
        builder.addExcludedPath("/doc.html");
        builder.addExcludedPath("/v2/api-docs");
    }

    @Override
    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {
        resolvers.add(new ViewDefaultExceptionResolver(this.pathProperties, this.webContextPathPredicate));
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        super.addResourceHandlers(registry);

        String staticPathPattern = this.mvcProperties.getStaticPathPattern();
        if (StringUtils.isNotBlank(staticPathPattern)) {
            String[] staticPathPatterns = StringUtil.splitAndTrim(staticPathPattern, Strings.COMMA);
            String[] staticLocations = this.webProperties.getResources().getStaticLocations();
            for (String pattern : staticPathPatterns) {
                if (!pattern.startsWith(Strings.SLASH)) { // 确保样式以/开头，否则判断一定不匹配
                    pattern = Strings.SLASH + pattern;
                }
                ResourceHandlerRegistration registration = registry.addResourceHandler(pattern);
                String dir = getStaticResourceDir(pattern);
                for (String staticLocation : staticLocations) {
                    if (staticLocation.endsWith(Strings.SLASH)) { // 确保不以/结尾
                        staticLocation = staticLocation.substring(0, staticLocation.length() - 1);
                    }
                    registration.addResourceLocations(staticLocation + dir);
                }
                if (AntPatternResourceResolver.supports(pattern)) {
                    registration.resourceChain(true).addResolver(new AntPatternResourceResolver(pattern));
                }
            }
            registry.setOrder(0); // 设置静态资源优先加载
        }
    }

    /**
     * 获取指定静态资源路径Ant样式对应的资源文件存放目录
     *
     * @param pattern 资源路径Ant样式
     * @return 资源文件存放目录
     */
    protected String getStaticResourceDir(String pattern) {
        // 去掉开头的/**
        if (pattern.startsWith("/**/")) {
            return Strings.SLASH;
        }
        // 去掉*之后的部分
        int index = pattern.indexOf(Strings.ASTERISK);
        if (index >= 0) {
            pattern = pattern.substring(0, index);
            // 此时再去掉最后一个/之后的部分
            index = pattern.lastIndexOf(Strings.SLASH);
            if (index >= 0) {
                pattern = pattern.substring(0, index + 1);
            }
        }
        return pattern;
    }

}
