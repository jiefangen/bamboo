package org.panda.ms.notice.model.param;

import lombok.Data;

import java.util.Map;

@Data
public class EmailParam extends BaseEmail {
    /**
     * 邮件类型
     */
    private String emailType;
    /**
     * 参数映射集
     */
    private Map<String, Object> params;
}
