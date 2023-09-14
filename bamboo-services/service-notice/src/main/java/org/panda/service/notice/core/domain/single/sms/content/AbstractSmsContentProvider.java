package org.panda.service.notice.core.domain.single.sms.content;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;

import java.util.Locale;

/**
 * 抽象的短信内容提供者
 */
public abstract class AbstractSmsContentProvider implements SmsContentProvider, MessageSourceAware {

    private String[] types;
    private String senderType;
    private String signNameCode;
    private int maxCount;
    protected MessageSource messageSource;

    @Override
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    public String[] getTypes() {
        return this.types;
    }

    public void setTypes(String... types) {
        this.types = types;
    }

    @Override
    public String getSenderType() {
        return this.senderType;
    }

    public void setSenderType(String senderType) {
        this.senderType = senderType;
    }

    public void setSignNameCode(String signNameCode) {
        this.signNameCode = signNameCode;
    }

    @Override
    public String getSignName(Locale locale) {
        return StringUtils.isBlank(this.signNameCode) ? null : this.messageSource
                .getMessage(this.signNameCode, null, locale);
    }

    @Override
    public int getMaxCount() {
        return this.maxCount;
    }

    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }

}
