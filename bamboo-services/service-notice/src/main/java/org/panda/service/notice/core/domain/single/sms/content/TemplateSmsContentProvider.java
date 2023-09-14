package org.panda.service.notice.core.domain.single.sms.content;

import org.panda.bamboo.common.parser.SimpleElTemplateParser;
import org.panda.bamboo.common.parser.TemplateParser;
import org.springframework.context.MessageSourceAware;

import java.util.Locale;
import java.util.Map;

/**
 * 基于模版的短信提供者
 */
public class TemplateSmsContentProvider extends AbstractSmsContentProvider implements MessageSourceAware {
    private String code;
    private TemplateParser parser = new SimpleElTemplateParser();

    public void setCode(String code) {
        this.code = code;
    }

    public void setParser(TemplateParser parser) {
        this.parser = parser;
    }

    @Override
    public String getContent(Map<String, Object> params, Locale locale) {
        String templateContent = this.messageSource.getMessage(this.code, null, this.code, locale);
        return this.parser.parse(templateContent, params, locale);
    }

}
