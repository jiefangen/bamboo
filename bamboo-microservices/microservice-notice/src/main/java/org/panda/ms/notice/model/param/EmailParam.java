package org.panda.ms.notice.model.param;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Data
public class EmailParam {
    /**
     * 邮件类型
     */
    private String emailType;
    /**
     * 区域
     */
    private Locale locale;
    /**
     * 参数映射集
     */
    private Map<String, Object> params;
    /**
     * 收件人地址
     */
    @NotNull
    private List<String> addressees;
}
