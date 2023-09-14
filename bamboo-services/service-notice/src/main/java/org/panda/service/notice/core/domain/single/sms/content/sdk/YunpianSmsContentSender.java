package org.panda.service.notice.core.domain.single.sms.content.sdk;

import com.yunpian.sdk.YunpianClient;
import com.yunpian.sdk.model.Result;
import com.yunpian.sdk.model.SmsSingleSend;
import org.panda.bamboo.common.util.LogUtil;
import org.panda.service.notice.core.domain.single.sms.content.AbstractSmsContentSender;
import org.panda.service.notice.core.domain.model.sms.SmsModel;
import org.panda.service.notice.core.domain.model.sms.SmsNotifyResult;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 云片短信内容发送器
 */
public class YunpianSmsContentSender extends AbstractSmsContentSender {

    private String apiKey;

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public SmsNotifyResult send(String signName, String content, int maxCount, String... cellphones) {
        SmsModel sms = new SmsModel();
        sms.setCellphones(cellphones);
        sms.setSendTime(LocalDateTime.now());
        SmsNotifyResult result = new SmsNotifyResult(sms);
        YunpianClient client = new YunpianClient(this.apiKey).init();
        Map<String, String> params = client.newParam(2);
        StringBuffer msg = new StringBuffer("【");
        msg.append(signName);
        msg.append("】").append(content);
        for (String cellphone : cellphones) {
            params.put(YunpianClient.MOBILE, cellphone);
            params.put(YunpianClient.TEXT, msg.toString());
            try {
                Result<SmsSingleSend> sendResult = client.sms().single_send(params);
                if (sendResult.getCode() != 0) {
                    result.getFailures().put(sendResult.getMsg(), cellphone);
                }
            } catch (Exception e) {
                LogUtil.error(getClass(), e);
                result.getFailures().put(e.getMessage(), cellphone);
            }
        }
        client.close();
        return result;
    }

}
