package org.panda.ms.notice.core.domain.single.sms.content.sdk;

import org.panda.bamboo.common.util.jackson.JsonUtil;
import org.panda.ms.notice.core.domain.single.sms.content.AbstractSmsContentProvider;

import java.util.Locale;
import java.util.Map;

/**
 * 阿里云短信内容提供者
 */
public class AliyunSmsContentProvider extends AbstractSmsContentProvider {

    @Override
    public String getContent(Map<String, Object> params, Locale locale) {
        return JsonUtil.toJson(params);
    }

}
