package org.panda.ms.notice.model.param;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

@Data
public class EmailParam {
    /**
     * 邮件类型
     */
    private String emailType;
    /**
     * 参数映射集
     */
    private Map<String, Object> params;
    /**
     * 收件人地址
     */
    @NotNull
    private List<String> addressees;

    /**
     * 邮件标题-自定义邮件类型时必须
     */
    private String title;
    /**
     * 邮件内容-自定义邮件类型时必须
     */
    private String content;
}
