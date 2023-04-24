package org.panda.service.sms.model;

import lombok.Data;

import java.util.List;
import java.util.Locale;
import java.util.Map;

@Data
public class SmsParam {
    /**
     * 短信类型
     */
    private String type;
    /**
     * 区域
     */
    private Locale locale;
    /**
     * 参数映射集
     */
    private Map<String, Object> params;
    /**
     * 手机号清单
     */
    private List<String> mobilePhones;
}
