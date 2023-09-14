package org.panda.service.notice.core.domain.model.email;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.context.MessageSource;

import java.util.Locale;

public class DefaultEmailSourceFactoryBean implements FactoryBean<EmailSource> {

    private String nameCode = "bamboo.notice.email.source.name";

    @Autowired
    private MailProperties mailProperties;
    @Autowired
    private MessageSource messageSource;

    public DefaultEmailSourceFactoryBean() {
    }

    public DefaultEmailSourceFactoryBean(String nameCode) {
        this.nameCode = nameCode;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public Class<?> getObjectType() {
        return EmailSource.class;
    }

    @Override
    public EmailSource getObject() throws Exception {
        String address = this.mailProperties.getUsername();
        String name = this.messageSource.getMessage(nameCode, null, nameCode, Locale.ROOT);
        String encoding = this.mailProperties.getDefaultEncoding().name();
        return new EmailSource(address, name, encoding);
    }

}
