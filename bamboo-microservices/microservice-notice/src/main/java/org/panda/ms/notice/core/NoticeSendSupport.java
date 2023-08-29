package org.panda.ms.notice.core;

import org.panda.bamboo.common.constant.Commons;
import org.panda.bamboo.common.util.LogUtil;
import org.panda.ms.notice.core.domain.model.NoticeMode;
import org.panda.ms.notice.core.domain.single.email.send.EmailSendProgress;
import org.panda.ms.notice.core.domain.single.email.send.EmailSender;
import org.panda.ms.notice.core.domain.single.sms.SmsNotifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * 消息通知发送抽象支持
 */
public abstract class NoticeSendSupport {

    @Autowired
    private SmsNotifier smsNotifier;
    @Autowired
    private EmailSender emailSender;

    /**
     * 获取使用消息通知的方式
     *
     * @return 通知方式
     */
    protected abstract NoticeMode getNoticeMode();

    /**
     * 消息通知发送
     *
     * @param type 业务类型
     * @param params 参数映射集
     * @param locale 区域
     * @param noticeTargets 通知目标集
     * @return 通知结果
     */
    @Bean
    protected Object send(String type, Map<String, Object> params, Locale locale, String[] noticeTargets) {
        if (noticeTargets == null || noticeTargets.length < 1) {
            return Commons.RESULT_FAILURE;
        }
        NoticeMode noticeMode = getNoticeMode();
        switch (noticeMode) {
            case SMS:
                return smsNotifier.notify(type, params, locale, noticeTargets);
            case EMAIL:
                EmailSendProgress emailSendProgress = new EmailSendProgress(noticeTargets.length);
                emailSender.send(type, List.of(noticeTargets), params, locale, emailSendProgress);
                return Commons.RESULT_SUCCESS;
            default:
                LogUtil.warn(getClass(), "Notification sending mode is illegal");
                break;
        }
        return Commons.RESULT_FAILURE;
    }

}
