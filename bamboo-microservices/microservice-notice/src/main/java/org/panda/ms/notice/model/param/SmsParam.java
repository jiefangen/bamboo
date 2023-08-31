package org.panda.ms.notice.model.param;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

@Data
public class SmsParam {
    /**
     * 短信类型
     */
    private String smsType;
    /**
     * 参数映射集
     */
    private Map<String, Object> params;
    /**
     * 手机号清单
     */
    @NotNull
    private List<String> mobilePhones;
}
