package org.panda.service.doc.config;

import lombok.Setter;
import org.panda.bamboo.Framework;
import org.panda.bamboo.common.constant.Profiles;
import org.panda.tech.core.config.app.AppConstants;
import org.panda.tech.core.web.config.WebConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

import java.util.ArrayList;
import java.util.List;

@Setter
@ConfigurationProperties(prefix = "bamboo.swagger.config")
@EnableSwagger2WebMvc
@Configuration
public class SwaggerConfig {

    private boolean enabled;
    private String version;
    private String basePackage;
    private String title;

    @Value(AppConstants.EL_SPRING_PROFILES_ACTIVE)
    private String env;

    @Bean
    public Docket defaultApi2() {
        if(Profiles.PRODUCTION.equals(env)) { // 生产环境关闭自动化文档
            this.enabled = false;
        }
        return new Docket(DocumentationType.SWAGGER_2)
                .enable(this.enabled)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage(this.basePackage))
                .paths(PathSelectors.any())
                .build()
                .globalOperationParameters(globalParameters());
    }

    private ApiInfo apiInfo() {
        this.title += "【" + env + "】";
        return new ApiInfoBuilder()
                .title(this.title)
                .description("文档微服务，致力于传统Office套件文件解析、转换、存储等服务")
                .version(this.version)
                .contact(new Contact(Framework.OWNER, "", Framework.EMAIL))
                .license("Apache 2.0")
                .licenseUrl("https://www.apache.org/licenses/LICENSE-2.0.html")
                .build();
    }

    private List<Parameter> globalParameters() {
        List<Parameter> parameters = new ArrayList<>();
        ParameterBuilder tokenParam = new ParameterBuilder();
        tokenParam.name(WebConstants.HEADER_AUTH_JWT)
                .description(WebConstants.HEADER_AUTH_JWT)
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .required(false).build();
        parameters.add(tokenParam.build());
        ParameterBuilder credentialsParam = new ParameterBuilder();
        credentialsParam.name(WebConstants.HEADER_AUTH_CREDENTIALS)
                .description(WebConstants.HEADER_AUTH_CREDENTIALS)
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .required(false).build();
        parameters.add(credentialsParam.build());
        return parameters;
    }

}
