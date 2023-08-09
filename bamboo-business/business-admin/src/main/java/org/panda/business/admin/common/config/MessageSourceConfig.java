package org.panda.business.admin.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import java.util.List;

/**
 * 国际化资源加载配置器
 *
 * @author fangen
 **/
@Configuration
@ConfigurationProperties("spring.messages")
public class MessageSourceConfig {
    /**
     * 国际化文件资源
     */
    private List<String> names;

    @Bean
    public ReloadableResourceBundleMessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        String basenamePrefix = "classpath:static/i18n/";
        String basenameSuffix = "/messages";
        String[] baseNames = new String[names.size()];
        for (int i = 0; i < names.size(); i++) {
            baseNames[i] = basenamePrefix + names.get(i) + basenameSuffix;
        }
        messageSource.setBasenames(baseNames);
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setCacheSeconds(-1); // 禁用缓存
        return messageSource;
    }

    public void setNames(List<String> names) {
        this.names = names;
    }
}
