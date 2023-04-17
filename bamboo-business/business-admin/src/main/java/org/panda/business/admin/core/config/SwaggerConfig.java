package org.panda.business.admin.core.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.panda.business.admin.common.constant.SystemConstants;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.RequestParameterBuilder;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.spring.web.plugins.WebMvcRequestHandlerProvider;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * swagger 相关配置
 *
 * @author fangen
 * @since JDK 11 2022/4/11
 */
@EnableOpenApi
@EnableKnife4j
@Configuration
public class SwaggerConfig {
    @Value("${spring.profiles.active}")
    private String env;

    @Bean
    public Docket createRestApi() {
        boolean swaggerEnabled = false;
        if("local".equals(env) || "dev".equals(env)) {
            swaggerEnabled = true;
        }
        return new Docket(DocumentationType.OAS_30)
                .enable(swaggerEnabled)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("org.panda.business.admin.modules"))
                .paths(PathSelectors.any())
                .build()
                .securityContexts(securityContext())
                .securitySchemes(securitySchemes());
//                .globalRequestParameters(authorizationParameter());
    }

    private List<SecurityScheme> securitySchemes() {
        return Collections.singletonList(new ApiKey(SystemConstants.AUTH_HEADER, SystemConstants.AUTH_HEADER, "header"));
    }

    private List<SecurityContext> securityContext() {
        SecurityContext securityContext = SecurityContext.builder()
                .securityReferences(defaultAuth())
                .build();
        return Collections.singletonList(securityContext);
    }

    List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Collections.singletonList(new SecurityReference(SystemConstants.AUTH_HEADER, authorizationScopes));
    }

    private List<RequestParameter> authorizationParameter() {
        RequestParameterBuilder tokenBuilder = new RequestParameterBuilder();
        tokenBuilder
                .name(SystemConstants.AUTH_HEADER)
                .description("JWT")
                .required(false)
                .in("header")
                .accepts(Collections.singleton(MediaType.APPLICATION_JSON))
                .build();
        return Collections.singletonList(tokenBuilder.build());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Bamboo Admin Api Guide")
                .description("Bamboo 后台管理系统")
                .contact(new Contact("介繁根", "https://github.com/jiefangen", "jiefangen@gmail.com"))
                .version("v1.0")
                .build();
    }

    @Bean
    public static BeanPostProcessor springfoxHandlerProviderBeanPostProcessor() {
        return new BeanPostProcessor() {

            @Override
            public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
                if (bean instanceof WebMvcRequestHandlerProvider) {
                    customizeSpringfoxHandlerMappings(getHandlerMappings(bean));
                }
                return bean;
            }

            private <T extends RequestMappingInfoHandlerMapping> void customizeSpringfoxHandlerMappings(List<T> mappings) {
                List<T> copy = mappings.stream()
//                        .filter(mapping -> mapping.getPatternParser() == null)
                        .collect(Collectors.toList());
                mappings.clear();
                mappings.addAll(copy);
            }

            @SuppressWarnings("unchecked")
            private List<RequestMappingInfoHandlerMapping> getHandlerMappings(Object bean) {
                try {
                    Field field = ReflectionUtils.findField(bean.getClass(), "handlerMappings");
                    field.setAccessible(true);
                    return (List<RequestMappingInfoHandlerMapping>) field.get(bean);
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    throw new IllegalStateException(e);
                }
            }
        };
    }
}
