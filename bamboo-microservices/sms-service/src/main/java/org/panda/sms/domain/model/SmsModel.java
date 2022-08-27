package org.panda.sms.domain.model;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 短信模型
 *
 * @author fangen
 * @since 2022/8/27
 */
@Data
public class SmsModel {
    /**
     * 内容清单
     */
    private String content;
    /**
     * 手机号码清单
     */
    private List<String> cellphones;
    /**
     * 发送时间
     */
    private Date sendTime;
    /**
     * 接收时间
     */
    private Date receiveTime;
}
