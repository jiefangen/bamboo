package org.panda.service.notice.core.domain.single.sms.content.http.strategy;

import org.apache.commons.lang3.StringUtils;
import org.panda.bamboo.common.constant.basic.Strings;
import org.panda.service.notice.core.domain.single.sms.content.http.AbstractHttpSmsContentSendStrategy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 瑞信通的短信发送策略
 */
public class RxtSmsContentSendStrategy extends AbstractHttpSmsContentSendStrategy {

    private String contentSuffix;

    public RxtSmsContentSendStrategy() {
        setEncoding("gb2312");
    }

    public void setContentSuffix(String contentSuffix) {
        this.contentSuffix = contentSuffix;
    }

    @Override
    public boolean isBatchable() {
        return false;
    }

    @Override
    public boolean isValid(String cellphone) {
        return true;
    }

    @Override
    public Map<String, Object> getParams(List<String> contents, int index, Set<String> cellphones) {
        Map<String, Object> params;
        if (this.defaultParams == null) {
            params = new HashMap<>();
        } else {
            params = new HashMap<>(this.defaultParams);
        }

        // smsText
        StringBuffer contentString = new StringBuffer();
        if (index < 0) {
            for (String content : contents) {
                contentString.append(content);
            }
        } else {
            String content = contents.get(index);
            contentString.append(content);
        }
        if (StringUtils.isNotBlank(this.contentSuffix)) {
            contentString.append(this.contentSuffix);
        }
        params.put("message", contentString.toString());

        // smsMob
        params.put("mobile", StringUtils.join(cellphones, Strings.COMMA));
        return params;
    }

    @Override
    public Map<String, String> getFailures(int statusCode, String content) {
        return super.getFailures(statusCode, content);
    }

}
