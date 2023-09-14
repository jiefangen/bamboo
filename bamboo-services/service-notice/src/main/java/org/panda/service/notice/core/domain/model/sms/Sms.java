package org.panda.service.notice.core.domain.model.sms;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 短信
 */
public interface Sms {

    /**
     * @return 内容清单
     */
    List<String> getContents();

    /**
     * @return 手机号码清单
     */
    String[] getCellphones();

    /**
     * @return 发送时间
     */
    LocalDateTime getSendTime();

    /**
     * @return 接收时间
     */
    LocalDateTime getReceiveTime();

}
