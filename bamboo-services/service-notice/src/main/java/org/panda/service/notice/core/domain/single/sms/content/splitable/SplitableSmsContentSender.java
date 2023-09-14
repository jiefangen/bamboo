package org.panda.service.notice.core.domain.single.sms.content.splitable;

import org.panda.service.notice.core.domain.single.sms.content.AbstractSmsContentSender;
import org.panda.service.notice.core.domain.model.sms.SmsNotifyResult;

import java.util.List;

/**
 * 可分割的短信内容发送器
 */
public abstract class SplitableSmsContentSender extends AbstractSmsContentSender {

    private SmsContentSplitter splitter = new DefaultSmsContentSplitter();

    /**
     * @param splitter 短信内容分割器
     */
    public void setSplitter(SmsContentSplitter splitter) {
        this.splitter = splitter;
    }

    @Override
    public SmsNotifyResult send(String signName, String content, int maxCount, String... cellphones) {
        List<String> contents = this.splitter.split(content, maxCount);
        return send(signName, contents, cellphones);
    }

    /**
     * 分成指定条数的内容发送短信
     *
     * @param signName   签名
     * @param contents   内容清单，每一个内容为一条短信
     * @param cellphones 手机号码清单
     * @return 发送结果
     */
    protected abstract SmsNotifyResult send(String signName, List<String> contents, String... cellphones);

}
