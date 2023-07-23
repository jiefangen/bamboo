package org.panda.business.admin.modules.monitor.api.vo;

import lombok.Getter;
import lombok.Setter;
import org.panda.business.admin.modules.monitor.service.entity.SysActionLog;
import org.panda.business.admin.modules.monitor.service.entity.SysUserToken;

/**
 * 在线用户视图
 *
 * @author fangen
 **/
@Setter
@Getter
public class OnlineVO {
    /**
     * 在线用户token信息
     */
    private SysUserToken userToken;
    /**
     * 登录操作日志
     */
    private SysActionLog actionLog;
}
