package org.panda.service.notice.core.domain.single.sms.content.splitable;

import java.util.List;

/**
 * 短信内容分割器<br>
 * 短信对内容有长度限制，各短信接口对字符长度的计算可能不一致，需针对性的进行内容分割
 */
public interface SmsContentSplitter {
    /**
     * 分割指定内容
     *
     * @param content  内容
     * @param maxCount 最大数量，&lt;=0表示不限
     * @return 分割成的内容清单
     */
    List<String> split(String content, int maxCount);
}
