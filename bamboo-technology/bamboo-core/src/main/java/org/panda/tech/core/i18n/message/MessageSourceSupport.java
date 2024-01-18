package org.panda.tech.core.i18n.message;

import org.panda.bamboo.common.constant.basic.Strings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;

import java.util.List;

/**
 * 国际化资源加载配置器支持
 **/
public abstract class MessageSourceSupport {
    /**
     * 获取国际化文件资源
     */
    protected abstract List<String> getNames();

    @Bean
    public ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        String basenamePrefix = getBasenamePrefix();
        String basenameSuffix = getBasenameSuffix();
        List<String> names = getNames();
        String[] baseNames = new String[names.size()];
        for (int i = 0; i < names.size(); i++) {
            baseNames[i] = basenamePrefix + names.get(i) + basenameSuffix;
        }
        messageSource.setBasenames(baseNames);
        messageSource.setDefaultEncoding(Strings.ENCODING_UTF8);
        messageSource.setCacheSeconds(-1); // 禁用缓存
        return messageSource;
    }

    protected String getBasenamePrefix() {
        return "META-INF/i18n/";
    }

    protected String getBasenameSuffix() {
        return "/messages";
    }
}
