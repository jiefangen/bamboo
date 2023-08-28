package org.panda.ms.notice.core.domain.model.sms;

import java.util.HashMap;
import java.util.Map;

/**
 * 短信通知结果
 */
public class SmsNotifyResult {
    private Sms sms;
    private Map<String, String> failures = new HashMap<>();

    public SmsNotifyResult(Sms sms) {
        this.sms = sms;
    }

    public Sms getSms() {
        return this.sms;
    }

    /**
     * @return 发送失败的手机号码-错误消息映射集
     */
    public Map<String, String> getFailures() {
        return this.failures;
    }

    public void addFailures(String errorMessage, String... failures) {
        for (String failure : failures) {
            this.failures.put(failure, errorMessage);
        }
    }

    /**
     * @return 是否全部发送成功
     */
    public boolean isAllSuccessful() {
        return this.failures.isEmpty();
    }

}
