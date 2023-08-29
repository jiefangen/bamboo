package org.panda.ms.notice.core.domain.single.email.provider;

import org.panda.bamboo.common.parser.FreeMarkerTemplateParser;
import org.panda.bamboo.common.parser.TemplateParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import java.util.Locale;
import java.util.Map;

/**
 * 基于消息的邮件提供者
 */
public class MessageEmailProvider extends AbstractEmailProvider {

    @Autowired
    private MessageSource messageSource;
    private TemplateParser parser = new FreeMarkerTemplateParser();
    private String content;

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String getTitle(Map<String, Object> params, Locale locale) {
        this.messageSource.getMessage(this.title, null, locale);
        return this.parser.parse(title, params, locale);
    }

    @Override
    public String getContent(Map<String, Object> params, Locale locale) {
        this.messageSource.getMessage(this.title, null, locale);
        return this.parser.parse(content, params, locale);
    }

}
