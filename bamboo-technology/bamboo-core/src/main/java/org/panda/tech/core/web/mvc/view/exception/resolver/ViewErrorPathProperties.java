package org.panda.tech.core.web.mvc.view.exception.resolver;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("bamboo.web.view.error.path")
public class ViewErrorPathProperties {

    private String business = "/error/business";
    private String format = "/error/format";
    private String badRequest = "/error/400";
    private String notFound = "/error/404";
    private String internal = "/error/500";

    public String getBusiness() {
        return this.business;
    }

    public void setBusiness(String business) {
        this.business = business;
    }

    public String getFormat() {
        return this.format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getBadRequest() {
        return this.badRequest;
    }

    public void setBadRequest(String badRequest) {
        this.badRequest = badRequest;
    }

    public String getNotFound() {
        return this.notFound;
    }

    public void setNotFound(String notFound) {
        this.notFound = notFound;
    }

    public String getInternal() {
        return this.internal;
    }

    public void setInternal(String internal) {
        this.internal = internal;
    }

}
