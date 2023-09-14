package org.panda.service.notice.core.domain.single.email.send;

import org.apache.commons.lang3.StringUtils;
import org.panda.bamboo.common.constant.basic.Strings;
import org.panda.service.notice.core.domain.model.email.EmailMessage;
import org.panda.service.notice.core.domain.model.email.EmailSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;

/**
 * 邮件发送线程指令
 */
public class EmailSendCommand implements Runnable {
    /**
     * Java邮件发送器
     */
    private JavaMailSender sender;
    /**
     * 邮件源
     */
    private EmailSource source;
    /**
     * 邮件消息集
     */
    private Iterable<EmailMessage> messages;
    /**
     * 发送间隔，单位：毫秒
     */
    private long interval;
    /**
     * 邮件发送进度
     */
    private EmailSendProgress progress;

    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 构建记录发送进度的指令
     *
     * @param sender   Java邮件发送器
     * @param source   邮件源
     * @param messages 邮件消息清单
     * @param interval 每次发送之间的间隔，单位：毫秒
     * @param progress 邮件发送进度
     */
    public EmailSendCommand(JavaMailSender sender, EmailSource source,
            Iterable<EmailMessage> messages, long interval,
            EmailSendProgress progress) {
        this.sender = sender;
        this.source = source;
        this.messages = messages;
        this.interval = interval;
        this.progress = progress;
    }

    @Override
    public void run() {
        if (this.progress != null) {
            for (EmailMessage message : this.messages) {
                try {
                    this.logger.info("======= Begin to send email to {} =======",
                            StringUtils.join(message.getAddresses(), Strings.COMMA));
                    this.sender.send(new SimpleMimeMessagePreparator(this.source, message));
                    this.logger.info("======= Sent email to {} =======",
                            StringUtils.join(message.getAddresses(), Strings.COMMA));
                    this.progress.addSuccess(message);
                } catch (Exception e) {
                    this.logger.error(e.getMessage(), e);
                    this.progress.addFailure(message, e);
                }
                if (this.interval > 0) {
                    try {
                        Thread.sleep(this.interval);
                    } catch (InterruptedException e) {
                        this.logger.error(e.getMessage(), e);
                    }
                }
                // 如果被提前设置为终止则停止发送后续邮件
                if (this.progress.isStop()) {
                    return;
                }
            }
        } else {
            for (EmailMessage message : this.messages) {
                try {
                    this.logger.info("======= Begin to send email to {} =======",
                            StringUtils.join(message.getAddresses(), Strings.COMMA));
                    this.sender.send(new SimpleMimeMessagePreparator(this.source, message));
                    this.logger.info("======= Sent email to {} =======",
                            StringUtils.join(message.getAddresses(), Strings.COMMA));
                } catch (Exception e) { // 尽量尝试发送所有邮件
                    this.logger.error(e.getMessage(), e);
                }
            }
        }
    }
}
