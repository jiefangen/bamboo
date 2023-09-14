package org.panda.service.notice.model.param;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CustomSmsParam extends BaseSms {
    /**
     * 短信内容-自定义邮件类型时必须
     */
    @NotBlank
    private String content;
}
