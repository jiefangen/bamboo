package org.panda.service.notice.core.aliyun;

import com.aliyuncs.exceptions.ClientException;
import org.apache.commons.lang3.StringUtils;
import org.panda.bamboo.common.constant.basic.Strings;
import org.panda.bamboo.common.util.LogUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 阿里云短信接口访问器
 */
public abstract class AliyunSmsAccessor extends AliyunAcsAccessSupport {

    /**
     * 发送短信
     *
     * @param signName     签名。必须是已经在阿里云通过审核的签名
     * @param templateCode 模版代号。必须是已经在阿里云通过审核的模版
     * @param content      内容
     * @param cellphones   手机号码清单
     * @return 发送失败的手机号码-错误消息的映射集
     */
    public final Map<String, String> send(String signName, String templateCode, String content,
            String... cellphones) {
        if (cellphones.length == 0) {
            return null;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("RegionId", getRegionId());
        params.put("SignName", signName);
        params.put("TemplateCode", templateCode);
        params.put("TemplateParam", content);
        params.put("PhoneNumbers", StringUtils.join(cellphones, Strings.COMMA));
        try {
            Map<String, Object> data = post("SendSms", params);
            String responseCode = (String) data.get("Code");
            if ("OK".equalsIgnoreCase(responseCode)) {
                return null;
            } else {
                String errorMessage = (String) data.get("Message");
                return buildFailures(errorMessage, cellphones);
            }
        } catch (ClientException e) {
            LogUtil.error(getClass(), e);
            return buildFailures(e.getErrMsg(), cellphones);
        }
    }

    private Map<String, String> buildFailures(String errorMessage, String... cellphones) {
        Map<String, String> failures = new HashMap<>();
        for (String cellphone : cellphones) {
            failures.put(cellphone, errorMessage);
        }
        return failures;
    }

}
