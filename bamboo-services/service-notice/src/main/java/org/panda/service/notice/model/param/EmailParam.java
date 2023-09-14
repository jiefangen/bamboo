package org.panda.service.notice.model.param;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Map;

@Data
public class EmailParam extends BaseEmail {
    /**
     * 邮件类型
     */
    @NotBlank
    private String emailType;
    /**
     * 参数映射集
     */
    private Map<String, Object> params;
}
