package org.panda.service.notice.model.param;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Map;

@Data
public class SmsParam extends BaseSms {
    /**
     * 短信类型
     */
    @NotBlank
    private String smsType;
    /**
     * 参数映射集
     */
    private Map<String, Object> params;
}
