package org.panda.service.notice.core.domain.single.email.send;

import org.panda.service.notice.core.domain.model.email.EmailMessage;
import org.panda.service.notice.core.domain.model.email.EmailSource;
import org.springframework.mail.javamail.MimeMessagePreparator;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * 简单的多用消息准备器
 */
public class SimpleMimeMessagePreparator implements MimeMessagePreparator {

    private EmailSource source;
    private EmailMessage message;

    /**
     * @param source  邮件源
     * @param message 邮件消息
     */
    public SimpleMimeMessagePreparator(EmailSource source, EmailMessage message) {
        this.source = source;
        this.message = message;
    }

    @Override
    public void prepare(MimeMessage mimeMessage) throws Exception {
        String encoding = this.source.getEncoding();
        mimeMessage.setFrom(new InternetAddress(this.source.getAddress(), this.source.getName(), encoding));
        // 设置收件人
        String[] targetAddresses = this.message.getAddresses();
        Address[] addresses = new Address[targetAddresses.length];
        for (int i = 0; i < targetAddresses.length; i++) {
            addresses[i] = new InternetAddress(targetAddresses[i]);
        }
        mimeMessage.setRecipients(Message.RecipientType.TO, addresses);
        // 设置标题
        mimeMessage.setSubject(this.message.getTitle(), encoding);
        MimeMultipart mm = new MimeMultipart("alternative");
        BodyPart bodyPart = new MimeBodyPart();
        bodyPart.setContent(this.message.getContent(), "text/html;charset=" + encoding);
        mm.addBodyPart(bodyPart);
        // 设置内容
        mimeMessage.setContent(mm);
    }

}
