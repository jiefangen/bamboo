package org.panda.ms.notice.config;

import org.panda.bamboo.common.annotation.helper.EnumValueHelper;
import org.panda.ms.notice.common.NoticeConstants;
import org.panda.ms.notice.core.domain.model.NoticeMode;
import org.panda.ms.notice.core.domain.single.sms.content.TemplateSmsContentProvider;
import org.panda.ms.notice.model.entity.NoticeConfigTemplate;
import org.panda.ms.notice.repository.NoticeConfigTemplateRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Example;

import java.util.Optional;

/**
 * 短信发送配置
 *
 * @author fangen
 **/
@Configuration
public class SmsSendConfig {

    @Autowired
    private NoticeConfigTemplateRepo configTemplateRepo;

    /**
     * 默认邮件发送内容提供
     * 配置文件中提取
     */
    @Bean
    @ConfigurationProperties(prefix = "sms.send.provider")
    public TemplateSmsContentProvider defaultMessageSms() {
        return new TemplateSmsContentProvider();
    }

    /**
     * 验证码邮件发送内容提供
     * 数据库中动态提取
     */
    @Bean
    public TemplateSmsContentProvider verificationCodeMessageSms() {
        NoticeConfigTemplate configTemplateParam = new NoticeConfigTemplate();
        configTemplateParam.setNoticeMode(EnumValueHelper.getValue(NoticeMode.SMS));
        configTemplateParam.setTemplateName(NoticeConstants.TYPE_SMS_VERIFY_CODE);
        configTemplateParam.setIsActive(true);
        Optional<NoticeConfigTemplate> configTemplate = configTemplateRepo.findOne(Example.of(configTemplateParam));
        if (configTemplate.isPresent()) {
            TemplateSmsContentProvider verificationCodeMessage = new TemplateSmsContentProvider();
            String templateName = configTemplate.get().getTemplateName();
            String[] types = templateName.split("\\,");
            verificationCodeMessage.setTypes(types);
            verificationCodeMessage.setMaxCount(10);
            verificationCodeMessage.setCode(configTemplate.get().getTemplateContent());
            return verificationCodeMessage;
        }
        return null;
    }
}
