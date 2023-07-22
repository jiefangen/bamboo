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
