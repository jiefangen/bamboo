package org.panda.service.notice.core.domain.single.sms.content.http.strategy;

import org.apache.commons.lang3.StringUtils;
import org.panda.bamboo.common.constant.basic.Strings;
import org.panda.service.notice.core.domain.single.sms.content.http.AbstractHttpSmsContentSendStrategy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 中国网建的短信发送策略
 */
public class WebChineseSmsContentSendStrategy extends AbstractHttpSmsContentSendStrategy {

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
        Map<String, Object> params = super.defaultParams;
        if (params == null) {
            params = new HashMap<>();
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
        params.put("smsText", contentString.toString());

        // smsMob
        params.put("smsMob", StringUtils.join(cellphones, Strings.COMMA));
        return params;
    }

    @Override
    public Map<String, String> getFailures(int statusCode, String content) {
        return super.getFailures(statusCode, content);
    }

}
