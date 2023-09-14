package org.panda.service.notice.core;

import org.panda.bamboo.common.constant.Commons;
import org.panda.bamboo.common.constant.basic.Strings;
import org.panda.bamboo.common.util.LogUtil;
import org.panda.service.notice.core.domain.model.NoticeMode;
import org.panda.service.notice.core.domain.single.email.send.EmailSendProgress;
import org.panda.service.notice.core.domain.single.email.send.EmailSender;
import org.panda.service.notice.core.domain.single.sms.SmsNotifier;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * 抽象消息通知发送支持
 */
public abstract class NoticeSendSupport {

    @Autowired
    private SmsNotifier smsNotifier;
    @Autowired
    private EmailSender emailSender;

    protected SmsNotifier getSmsNotifier() {
        return smsNotifier;
    }

    protected EmailSender getEmailSender() {
        return emailSender;
    }

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

    /**
     * 消息自定义通知发送
     *
     * @param title 标题
     * @param content 内容
     * @param noticeTargets 通知目标集
     * @return 通知结果
     */
    protected Object sendCustom(String title, String content, String[] noticeTargets) {
        if (noticeTargets == null || noticeTargets.length < 1) {
            return Commons.RESULT_FAILURE;
        }
        NoticeMode noticeMode = getNoticeMode();
        switch (noticeMode) {
            case SMS:
                return smsNotifier.notifyCustom(Strings.ASTERISK, content, Strings.EMPTY, 10,null, noticeTargets);
            case EMAIL:
                EmailSendProgress emailSendProgress = new EmailSendProgress(noticeTargets.length);
                emailSender.sendCustom(List.of(noticeTargets), title, content, emailSendProgress);
                return Commons.RESULT_SUCCESS;
            default:
                LogUtil.warn(getClass(), "Notification sending mode is illegal");
                break;
        }
        return Commons.RESULT_FAILURE;
    }

}
