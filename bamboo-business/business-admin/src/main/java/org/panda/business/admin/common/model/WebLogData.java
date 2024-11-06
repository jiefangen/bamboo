package org.panda.business.admin.common.model;

import lombok.Data;
import org.panda.tech.core.web.model.WebLogRange;

/**
 * Web操作日志扩展模型
 *
 * @author fangen
 **/
@Data
public class WebLogData extends WebLogRange {
    private static final long serialVersionUID = -2302592404778182775L;
    /**
     * 日志来源，绑定用户登录token数据
     */
    private String sourceId;

    public void transform(WebLogRange webLogRange) {
        super.setStartTimeMillis(webLogRange.getStartTimeMillis());
        super.setHost(webLogRange.getHost());
        super.setIpAddress(webLogRange.getIpAddress());
        super.setActionType(webLogRange.getActionType());
        super.setContent(webLogRange.getContent());
        super.setIdentity(webLogRange.getIdentity());
        super.setBodyStr(webLogRange.getBodyStr());
        super.setTakeTime(webLogRange.getTakeTime());

        super.setTerminalDevice(webLogRange.getTerminalDevice());
        super.setTerminalOs(webLogRange.getTerminalOs());
    }
}
