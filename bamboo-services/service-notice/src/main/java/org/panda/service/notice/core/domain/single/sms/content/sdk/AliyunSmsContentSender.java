package org.panda.service.notice.core.domain.single.sms.content.sdk;

import org.panda.service.notice.core.aliyun.AliyunSmsAccessor;
import org.panda.service.notice.core.domain.single.sms.content.AbstractSmsContentSender;
import org.panda.service.notice.core.domain.model.sms.SmsModel;
import org.panda.service.notice.core.domain.model.sms.SmsNotifyResult;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 阿里云的短信内容发送器
 */
public class AliyunSmsContentSender extends AbstractSmsContentSender {

    private AliyunSmsAccessor smsAccessor;
    private String templateCode;

    @Autowired
    public void setSmsAccessor(AliyunSmsAccessor smsAccessor) {
        this.smsAccessor = smsAccessor;
    }

    public void setTemplateCode(String templateCode) {
        this.templateCode = templateCode;
    }

    @Override
    public SmsNotifyResult send(String signName, String content, int maxCount, String... cellphones) {
        SmsModel sms = new SmsModel();
        sms.setContents(List.of(content));
        sms.setCellphones(cellphones);
        sms.setSendTime(LocalDateTime.now());
        SmsNotifyResult result = new SmsNotifyResult(sms);
        Map<String, String> failures = this.smsAccessor.send(signName, this.templateCode, content, cellphones);
        if (failures != null) {
            result.getFailures().putAll(failures);
        }
        return result;
    }


}
