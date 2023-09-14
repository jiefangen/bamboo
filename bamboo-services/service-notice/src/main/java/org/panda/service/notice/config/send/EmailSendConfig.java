package org.panda.service.notice.config.send;

import org.panda.bamboo.common.annotation.helper.EnumValueHelper;
import org.panda.service.notice.common.NoticeConstants;
import org.panda.service.notice.core.domain.model.NoticeMode;
import org.panda.service.notice.core.domain.model.email.EmailSource;
import org.panda.service.notice.core.domain.single.email.provider.MessageEmailProvider;
import org.panda.service.notice.model.entity.NoticeConfigTemplate;
import org.panda.service.notice.repository.NoticeConfigTemplateRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Example;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Optional;

/**
 * 邮件发送配置
 *
 * @author fangen
 **/
@Configuration
public class EmailSendConfig {

    @Autowired
    private NoticeConfigTemplateRepo configTemplateRepo;

    /**
     * 默认邮件发送器
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
     * 默认邮件发送内容提供
     * 配置文件中提取
     */
    @Bean
    @ConfigurationProperties(prefix = "email.send.provider")
    public MessageEmailProvider defaultMessageEmail() {
        return new MessageEmailProvider();
    }

    /**
     * 验证码邮件发送内容提供
     * 数据库中动态提取
     */
    @Bean
    public MessageEmailProvider verificationCodeMessageEmail() {
        NoticeConfigTemplate configTemplateParam = new NoticeConfigTemplate();
        configTemplateParam.setNoticeMode(EnumValueHelper.getValue(NoticeMode.EMAIL));
        configTemplateParam.setNoticeProviderType(NoticeConstants.TYPE_VERIFY_CODE);
        configTemplateParam.setIsActive(true);
        Optional<NoticeConfigTemplate> configTemplate = configTemplateRepo.findOne(Example.of(configTemplateParam));
        if (configTemplate.isPresent()) {
            MessageEmailProvider verificationCodeMessage = new MessageEmailProvider();
            verificationCodeMessage.setType(NoticeConstants.TYPE_VERIFY_CODE);
            verificationCodeMessage.setTitle(configTemplate.get().getTemplateContentTitle());
            verificationCodeMessage.setContent(configTemplate.get().getTemplateContent());
            return verificationCodeMessage;
        }
        return null;
    }
}
