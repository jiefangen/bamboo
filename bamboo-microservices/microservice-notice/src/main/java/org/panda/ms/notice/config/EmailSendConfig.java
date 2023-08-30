package org.panda.ms.notice.config;

import org.panda.ms.notice.core.domain.model.email.EmailSource;
import org.panda.ms.notice.core.domain.single.email.provider.MessageEmailProvider;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

/**
 * 邮件发送配置
 *
 * @author fangen
 **/
@Configuration
public class EmailSendConfig {
    /**
     * 邮件发送器
     */
    @Bean
    public JavaMailSender javaMailSender() {
        return new JavaMailSenderImpl();
    }

    /**
     * 邮件发送源
     */
    @Bean
    @ConfigurationProperties(prefix = "email.send.source")
    public EmailSource emailSource() {
        return new EmailSource();
    }

    /**
     * 邮件发送内容提供
     */
    @Bean
    @ConfigurationProperties(prefix = "email.send.provider")
    public MessageEmailProvider messageEmailProvider() {
        return new MessageEmailProvider();
    }
}
