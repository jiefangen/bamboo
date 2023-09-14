package org.panda.service.notice.core.domain.single.email.send;

import org.panda.bamboo.common.util.lang.StringUtil;
import org.panda.bamboo.core.beans.ContextInitializedBean;
import org.panda.service.notice.core.domain.model.email.EmailMessage;
import org.panda.service.notice.core.domain.model.email.EmailSource;
import org.panda.service.notice.core.domain.single.email.provider.EmailProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.Executor;

/**
 * 邮件发送器默认实现
 */
@Component
public class EmailSenderImpl implements EmailSender, ContextInitializedBean {

    @Autowired
    private JavaMailSender sender;
    @Autowired
    private EmailSource source;
    /**
     * 线程执行器
     */
    private Executor taskExecutor;

    private int interval = 1000;
    /**
     * 邮件实体映射集
     */
    private Map<String, EmailProvider> providers = new HashMap<>();

    /**
     * @param taskExecutor 线程执行器，未配置时不采用多线程的方式执行
     */
    @Autowired
    public void setExecutor(Executor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    @Override
    public void afterInitialized(ApplicationContext context) throws Exception {
        Map<String, EmailProvider> providerMap = context.getBeansOfType(EmailProvider.class);
        for (EmailProvider provider : providerMap.values()) {
            String type = provider.getType();
            if (type != null) {
                this.providers.put(type, provider);
            }
        }
    }

    @Override
    public EmailProvider getProvider(String type) {
        return this.providers.get(type);
    }

    @Override
    public void send(String type, Iterable<String> addressees, Map<String, Object> params, Locale locale,
            EmailSendProgress progress) {
        EmailProvider provider = getProvider(type);
        if (provider != null && addressees.iterator().hasNext()) {
            String title = provider.getTitle(params, locale);
            String content = provider.getContent(params, locale);
            List<EmailMessage> messages = new ArrayList<>();
            for (String addressee : addressees) {
                if (StringUtil.isEmail(addressee)) {
                    messages.add(new EmailMessage(addressee, title, content));
                }
            }
            EmailSendCommand command = new EmailSendCommand(this.sender, this.source, messages, this.interval, progress);
            if (this.taskExecutor == null) {
                command.run();
            } else {
                this.taskExecutor.execute(command);
            }
        }
    }

    @Override
    public void sendCustom(Iterable<String> addressees, String title, String content, EmailSendProgress progress) {
        if (addressees.iterator().hasNext()) {
            List<EmailMessage> messages = new ArrayList<>();
            for (String addressee : addressees) {
                if (StringUtil.isEmail(addressee)) {
                    messages.add(new EmailMessage(addressee, title, content));
                }
            }
            EmailSendCommand command = new EmailSendCommand(this.sender, this.source, messages, this.interval, progress);
            if (this.taskExecutor == null) {
                command.run();
            } else {
                this.taskExecutor.execute(command);
            }
        }
    }
}
