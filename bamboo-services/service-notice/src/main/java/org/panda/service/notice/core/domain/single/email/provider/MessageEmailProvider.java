package org.panda.service.notice.core.domain.single.email.provider;

import org.panda.bamboo.common.parser.FreeMarkerTemplateParser;
import org.panda.bamboo.common.parser.TemplateParser;
import org.panda.tech.core.util.message.MessageResolver;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Locale;
import java.util.Map;

/**
 * 基于消息的邮件提供者
 */
public class MessageEmailProvider extends AbstractEmailProvider {

    @Autowired
    private MessageResolver messageResolver;
    private TemplateParser parser = new FreeMarkerTemplateParser();
    private String content;

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String getTitle(Map<String, Object> params, Locale locale) {
        String title = this.messageResolver.resolveMessage(this.title, locale);
        return this.parser.parse(title, params, locale);
    }

    @Override
    public String getContent(Map<String, Object> params, Locale locale) {
        String content = this.messageResolver.resolveMessage(this.content, locale);
        return this.parser.parse(content, params, locale);
    }

}
