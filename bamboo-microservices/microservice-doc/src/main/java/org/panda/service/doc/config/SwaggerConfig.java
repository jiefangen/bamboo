package org.panda.service.doc.config;

import lombok.Setter;
import org.panda.bamboo.Framework;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Setter
@Configuration
@ConfigurationProperties(prefix = "swagger.config")
@EnableSwagger2
public class SwaggerConfig {
    private static final String SWAGGER_TITLE = "Microservice Doc API";
    private static final String SWAGGER_DESC = "文档微服务，致力于传统Office套件文件解析、转换、存储等服务。";
    /**
     * 自动化API文档控制开关
     */
    private boolean enabled;
    /**
     * Swagger扫描基础包位置
     */
    private String basePackage;

    @Bean(value = "defaultApi2")
    public Docket defaultApi2() {
        return new Docket(DocumentationType.SWAGGER_2)
                .enable(this.enabled)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage(this.basePackage))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(SWAGGER_TITLE)
                .description(SWAGGER_DESC)
                .version("1.0.0")
                .contact(new Contact(Framework.OWNER, "", Framework.EMAIL))
                .license("Apache 2.0")
                .licenseUrl("https://www.apache.org/licenses/LICENSE-2.0.html")
                .build();
    }

}
