package org.panda.tech.core.config.app;

/**
 * 应用的对外门面
 */
public class AppFacade {

    private String name;
    private String caption;
    private String business;
    private String contextUri;
    private String loginedUri;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCaption() {
        return this.caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getBusiness() {
        return this.business;
    }

    public void setBusiness(String business) {
        this.business = business;
    }

    public String getContextUri() {
        return this.contextUri;
    }

    public void setContextUri(String contextUri) {
        this.contextUri = contextUri;
    }

    public String getLoginedUri() {
        return this.loginedUri;
    }

    public void setLoginedUri(String loginedUri) {
        this.loginedUri = loginedUri;
    }

}
