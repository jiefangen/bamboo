package org.panda.service.notice.core.domain.single.sms.content.http;

import org.panda.bamboo.common.constant.basic.Strings;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashMap;
import java.util.Map;

/**
 * 抽象的HTTP短信发送策略
 */
public abstract class AbstractHttpSmsContentSendStrategy implements HttpSmsContentSendStrategy {

    private String url;
    private RequestMethod requestMethod = RequestMethod.POST;
    private String encoding = Strings.ENCODING_UTF8;
    protected Map<String, Object> defaultParams;

    @Override
    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public RequestMethod getRequestMethod() {
        return this.requestMethod;
    }

    public void setRequestMethod(RequestMethod requestMethod) {
        this.requestMethod = requestMethod;
    }

    @Override
    public String getEncoding() {
        return this.encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public void setDefaultParams(Map<String, Object> defaultParams) {
        this.defaultParams = defaultParams;
    }

    @Override
    public Map<String, String> getFailures(int statusCode, String content) {
        Map<String, String> failures = new HashMap<>();
        failures.put(String.valueOf(statusCode), content);
        return failures;
    }

}
