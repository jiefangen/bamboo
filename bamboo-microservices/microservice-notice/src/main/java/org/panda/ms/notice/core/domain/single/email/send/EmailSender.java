package org.panda.ms.notice.core.domain.single.email.send;

import org.panda.ms.notice.core.domain.single.email.provider.EmailProvider;

import java.util.Locale;
import java.util.Map;

/**
 * 邮件发送器
 */
public interface EmailSender {

    /**
     * 获取指定类型的邮件提供者
     *
     * @param type 邮件类型
     * @return 指定类型的邮件提供者
     */
    EmailProvider getProvider(String type);

    /**
     * 异步发送指定邮件到多个收件人，每个收件人单独收到该邮件
     *
     * @param type       邮件类型
     * @param addressees 收件人地址
     * @param params     参数集
     * @param locale     区域
     * @param progress   发送进度
     */
    void send(String type, Iterable<String> addressees, Map<String, Object> params, Locale locale,
            EmailSendProgress progress);

}
