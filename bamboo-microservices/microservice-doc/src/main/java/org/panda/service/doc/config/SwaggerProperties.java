package org.panda.service.doc.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "swagger.config")
@Data
public class SwaggerProperties {
    /**
     * 自动化API文档控制开关
     */
    private boolean enabled;
    /**
     * Swagger扫描基础包位置
     */
    private String basePackage;
    /**
     * Swagger标题
     */
    private String title;
    /**
     * Swagger描述
     */
    private String desc;

}
