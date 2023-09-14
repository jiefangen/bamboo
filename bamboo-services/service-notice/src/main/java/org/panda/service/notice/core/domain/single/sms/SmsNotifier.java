package org.panda.service.notice.core.domain.single.sms;

import org.panda.service.notice.core.domain.model.sms.SmsNotifyResult;
import org.panda.service.notice.core.domain.single.sms.content.SmsContentProvider;

import java.util.Locale;
import java.util.Map;
import java.util.function.Consumer;

/**
 * 短信通知器
 */
public interface SmsNotifier {
    /**
     * 获取指定类型的短信提供者
     *
     * @param type 短信
     * @return 指定类型的短信提供者
     */
    SmsContentProvider getContentProvider(String type);
    /**
     * 同步通知指定类型的短信
     *
     * @param type       业务类型
     * @param params     参数映射集
     * @param locale     区域
     * @param cellphones 手机号码清单
     * @return 通知结果
     */
    SmsNotifyResult notify(String type, Map<String, Object> params, Locale locale, String... cellphones);

    /**
     * 同步通知自定义内容的短信
     *
     * @param type     标题
     * @param content     内容
     * @param cellphones 手机号码清单
     * @return 通知结果
     */
    SmsNotifyResult notifyCustom(String type, String content, String signName, int maxCount,
                                 Locale locale, String... cellphones);

    /**
     * 异步通知指定类型的短信
     *
     * @param type       业务类型
     * @param params     参数映射集
     * @param locale     区域
     * @param cellphones 手机号码清单
     * @param callback   短信通知回调
     */
    void notify(String type, Map<String, Object> params, Locale locale, String[] cellphones,
            Consumer<SmsNotifyResult> callback);

    /**
     * 获取指定业务类型下，对指定手机号码再次发送短信的剩余时间秒数
     *
     * @param type      业务类型
     * @param cellphone 手机号码
     * @return 剩余时间秒数
     */
    int getRemainingSeconds(String type, String cellphone);

}
