package org.panda.business.admin.common.config;

import org.panda.tech.core.i18n.message.MessageSourceSupport;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 国际化资源加载配置器
 *
 * @author fangen
 **/
@Configuration
@ConfigurationProperties("spring.messages")
public class MessageSourceConfig extends MessageSourceSupport {
    /**
     * 国际化文件资源
     */
    private List<String> names;

    @Override
    protected List<String> getNames() {
        return this.names;
    }

    public void setNames(List<String> names) {
        this.names = names;
    }
}
