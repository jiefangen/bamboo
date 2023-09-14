package org.panda.service.notice.config.send;

import org.apache.commons.lang3.StringUtils;
import org.panda.bamboo.common.annotation.helper.EnumValueHelper;
import org.panda.service.notice.common.NoticeConstants;
import org.panda.service.notice.core.domain.model.NoticeMode;
import org.panda.service.notice.core.domain.single.sms.content.TemplateSmsContentProvider;
import org.panda.service.notice.core.domain.single.sms.content.sdk.YunpianSmsContentSender;
import org.panda.service.notice.model.entity.NoticeConfigTemplate;
import org.panda.service.notice.repository.NoticeConfigTemplateRepo;
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
     * 云片短信发送器
     */
    @Bean
    @ConfigurationProperties(prefix = "sms.send.sender")
    public YunpianSmsContentSender yunpianSmsContentSender() {
        return new YunpianSmsContentSender();
    }

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
        configTemplateParam.setNoticeProviderType(NoticeConstants.TYPE_VERIFY_CODE);
        configTemplateParam.setIsActive(true);
        Optional<NoticeConfigTemplate> configTemplate = configTemplateRepo.findOne(Example.of(configTemplateParam));
        if (configTemplate.isPresent()) {
            TemplateSmsContentProvider verificationCodeMessage = new TemplateSmsContentProvider();
            String category = configTemplate.get().getCategory();
            if (StringUtils.isNotBlank(category)) {
                String[] types = category.split("\\,");
                verificationCodeMessage.setTypes(types);
            } else {
                String[] types = new String[]{ NoticeConstants.TYPE_VERIFY_CODE };
                verificationCodeMessage.setTypes(types);
            }
            verificationCodeMessage.setSenderType(configTemplate.get().getNoticeSenderType());
            verificationCodeMessage.setMaxCount(10);
            verificationCodeMessage.setCode(configTemplate.get().getTemplateContent());
            return verificationCodeMessage;
        }
        return null;
    }
}
