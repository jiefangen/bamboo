package org.panda.tech.core.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 环境配置属性集
 */
@Configuration
@ConfigurationProperties("bamboo.profile")
public class ProfileProperties {

    /**
     * 当前环境是否正式环境
     */
    private boolean formal;

    public boolean isFormal() {
        return this.formal;
    }

    public void setFormal(boolean formal) {
        this.formal = formal;
    }

}
