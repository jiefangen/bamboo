package org.panda.core.config;

import org.springframework.beans.factory.annotation.Value;

public class SwaggerTemplate {

    @Value("${swagger.basePackage:org.panda}")
    protected String basePackage;
    @Value("${swagger.enabled:false}")
    protected boolean swaggerEnabled;
    @Value("${swagger.apiInfo.title:Api Documentation}")
    protected String swaggerTitle;
    @Value("${swagger.apiInfo.desc:Api Documentation}")
    protected String swaggerDesc;

}
