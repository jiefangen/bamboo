package org.panda.service.notice.core.domain.single.sms.content.http;

import org.panda.tech.core.spec.http.HttpRequestMethod;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * HTTP短信发送策略
 */
public interface HttpSmsContentSendStrategy {

    /**
     * 获取请求URL
     *
     * @return 请求URL
     */
    String getUrl();

    /**
     * 获取请求方式
     *
     * @return 请求方式
     */
    HttpRequestMethod getRequestMethod();

    /**
     * 获取编码方式
     *
     * @return 编码方式
     */
    String getEncoding();

    /**
     * @return 是否支持一次性向一个手机号码批量发送多条短信
     */
    boolean isBatchable();

    /**
     * 判断指定手机号码是否有效
     *
     * @param cellphone 手机号码
     * @return 指定手机号码是否有效
     */
    boolean isValid(String cellphone);

    /**
     * 获取发送请求参数集
     *
     * @param contents   短信内容清单，每一个内容为一条短信
     * @param index      内容索引下标，支持批量发送时，传入小于0的值
     * @param cellphones 手机号码集
     * @return 发送请求参数集
     */
    Map<String, Object> getParams(List<String> contents, int index, Set<String> cellphones);

    /**
     * 根据响应获取发送失败的手机号码清单
     *
     * @param statusCode 响应状态码
     * @param content    响应内容
     * @return 发送失败的手机号码清单
     */
    Map<String, String> getFailures(int statusCode, String content);
}
