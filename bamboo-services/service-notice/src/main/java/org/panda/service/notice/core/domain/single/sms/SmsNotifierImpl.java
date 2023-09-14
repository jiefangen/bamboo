package org.panda.service.notice.core.domain.single.sms;

import org.panda.bamboo.common.constant.basic.Strings;
import org.panda.bamboo.common.util.lang.StringUtil;
import org.panda.bamboo.core.beans.ContextInitializedBean;
import org.panda.service.notice.core.domain.model.sms.SmsNotifyResult;
import org.panda.service.notice.core.domain.single.sms.content.SmsContentProvider;
import org.panda.service.notice.core.domain.single.sms.content.SmsContentSender;
import org.panda.tech.core.util.message.MessageResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

/**
 * 短信发送器实现
 */
@Component
public class SmsNotifierImpl implements SmsNotifier, ContextInitializedBean {

    private Map<String, SmsContentProvider> contentProviders = new HashMap<>();
    private Map<String, SmsContentSender> contentSenders = new HashMap<>();
    private Map<String, Instant> sendableInstants = new HashMap<>(); // 可发送的时刻映射集
    @Autowired
    private MessageResolver messageResolver;
    @Autowired
    private ExecutorService executorService;

    @Override
    public void afterInitialized(ApplicationContext context) throws Exception {
        context.getBeansOfType(SmsContentProvider.class).forEach((id, provider) -> {
            String[] types = provider.getTypes();
            for (String type : types) {
                Assert.isNull(this.contentProviders.put(type, provider), () -> {
                    return "There is more than one SmsContentProvider for type '" + type + "'";
                });
            }
        });

        context.getBeansOfType(SmsContentSender.class).forEach((id, sender) -> {
            String[] types = sender.getTypes();
            for (String type : types) {
                Assert.isNull(this.contentSenders.put(type, sender), () -> {
                    return "There is more than one SmsContentSender for type '" + type + "'";
                });
            }
        });
    }

    @Override
    public SmsContentProvider getContentProvider(String type) {
        SmsContentProvider contentSender = this.contentProviders.get(type);
        if (contentSender == null) {
            contentSender = this.contentProviders.get(Strings.ASTERISK); // 默认内容提供者
        }
        return contentSender;
    }

    private SmsContentSender getContentSender(String type) {
        SmsContentSender contentSender = this.contentSenders.get(type);
        if (contentSender == null) {
            contentSender = this.contentSenders.get(Strings.ASTERISK); // 默认内容发送器
        }
        return contentSender;
    }

    @Override
    public SmsNotifyResult notify(String type, Map<String, Object> params, Locale locale, String... cellphones) {
        SmsContentProvider contentProvider = getContentProvider(type);
        if (contentProvider != null) {
            String content = contentProvider.getContent(params, locale);
            String signName = contentProvider.getSignName(locale);
            int maxCount = contentProvider.getMaxCount();
            String senderType = contentProvider.getSenderType();
            return this.notifyCustom(senderType, content, signName, maxCount, locale, cellphones);
        }
        return null;
    }

    @Override
    public SmsNotifyResult notifyCustom(String type, String content, String signName, int maxCount,
                                        Locale locale, String... cellphones) {
        if (content != null) {
            SmsContentSender contentSender = getContentSender(type);
            if (contentSender != null) {
                // 检查获取因时限不可发送的手机号码
                List<String> notCellphones = new ArrayList<>();
                List<String> unsendableCellphones = new ArrayList<>();
                if (cellphones.length == 1) { // 只有一个手机号码的，快速处理
                    String cellphone = cellphones[0];
                    if (!StringUtil.isCellphone(cellphone)) {
                        notCellphones.add(cellphone);
                        cellphones = new String[0];
                    } else if (getRemainingSeconds(contentSender, cellphone) > 0) {
                        unsendableCellphones.add(cellphone);
                        cellphones = new String[0];
                    }
                } else { // 多个手机号码的，循环处理
                    List<String> sendableCellphones = new ArrayList<>();
                    for (String cellphone : cellphones) {
                        if (!StringUtil.isCellphone(cellphone)) {
                            notCellphones.add(cellphone);
                        } else if (getRemainingSeconds(contentSender, cellphone) > 0) {
                            unsendableCellphones.add(cellphone);
                        } else {
                            sendableCellphones.add(cellphone);
                        }
                    }
                    if (notCellphones.size() + unsendableCellphones.size() > 0) {
                        cellphones = sendableCellphones.toArray(new String[0]);
                    }
                }

                SmsNotifyResult result = contentSender.send(signName, content, maxCount, cellphones);
                putSendableInstants(contentSender, cellphones);
                // 添加不是手机号码的错误
                notCellphones.forEach(cellphone -> {
                    Object[] args = { cellphone };
                    String errorMessage = this.messageResolver
                            .resolveMessage("error.notice.sms.invalid_cellphone", locale, args);
                    result.addFailures(errorMessage, cellphone);
                });
                // 添加因时限不能发送的错误
                unsendableCellphones.forEach(cellphone -> {
                    Object[] args = { getRemainingSeconds(contentSender, cellphone) };
                    String errorMessage = this.messageResolver
                            .resolveMessage("error.notice.sms.interval_limited", locale, args);
                    result.addFailures(errorMessage, cellphone);
                });
                return result;
            }
        }
        return null;
    }

    private int getRemainingSeconds(SmsContentSender contentSender, String cellphone) {
        String key = contentSender.toString() + Strings.MINUS + cellphone;
        Instant instant = this.sendableInstants.get(key);
        if (instant == null) { // 没有约束，剩余时间为0
            return 0;
        }
        // 计算限定时间与当前时间的秒数差
        long millis = instant.toEpochMilli() - System.currentTimeMillis();
        if (millis <= 0) { // 约束时间已过，则从缓存移除
            this.sendableInstants.remove(key);
            return 0;
        }
        return (int) (millis / 1000 + (millis % 1000 == 0 ? 0 : 1));
    }

    private void putSendableInstants(SmsContentSender contentSender, String... cellphones) {
        String prefix = contentSender.toString() + Strings.MINUS;
        Instant instant = Instant.now().plusSeconds(contentSender.getIntervalSeconds());
        for (String cellphone : cellphones) {
            String key = prefix + cellphone;
            this.sendableInstants.put(key, instant);
        }
    }

    @Override
    public void notify(String type, Map<String, Object> params, Locale locale, String[] cellphones,
            Consumer<SmsNotifyResult> callback) {
        this.executorService.submit(() -> {
            SmsNotifyResult result = notify(type, params, locale, cellphones);
            callback.accept(result);
        });
    }

    @Override
    public int getRemainingSeconds(String type, String cellphone) {
        SmsContentSender contentSender = getContentSender(type);
        if (contentSender != null) {
            return getRemainingSeconds(contentSender, cellphone);
        }
        return -1;
    }

}
