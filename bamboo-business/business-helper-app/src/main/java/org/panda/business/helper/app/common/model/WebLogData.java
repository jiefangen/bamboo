package org.panda.business.helper.app.common.model;

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
     * 来源ID
     */
    private String sourceId;
    /**
     * 终端设备
     */
    private String terminalDevice;
    /**
     * 终端操作系统
     */
    private String terminalOs;

    public void transform(WebLogRange webLogRange) {
        super.setStartTimeMillis(webLogRange.getStartTimeMillis());
        super.setHost(webLogRange.getHost());
        super.setIpAddress(webLogRange.getIpAddress());
        super.setActionType(webLogRange.getActionType());
        super.setContent(webLogRange.getContent());
        super.setIdentity(webLogRange.getIdentity());
        super.setBodyStr(webLogRange.getBodyStr());
        super.setTakeTime(webLogRange.getTakeTime());
    }
}
