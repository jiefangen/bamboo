package org.panda.service.notice.model.param;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CustomEmailParam extends BaseEmail {
    /**
     * 邮件标题-自定义邮件类型时必须
     */
    @NotBlank
    private String title;
    /**
     * 邮件内容-自定义邮件类型时必须
     */
    @NotBlank
    private String content;
}
