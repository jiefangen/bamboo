package org.panda.business.admin.modules.monitor.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.panda.business.admin.modules.monitor.service.entity.SysActionLog;
import org.panda.business.admin.modules.monitor.service.entity.SysUserToken;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 在线用户视图
 *
 * @author fangen
 **/
@Setter
@Getter
@ApiModel(value="在线用户视图对象")
public class OnlineVO implements Serializable {

    private static final long serialVersionUID = 3460773064110862919L;

    @ApiModelProperty(value = "token唯一标识")
    private Long tokenId;

    @ApiModelProperty(value = "身份标识")
    private String identity;

    @ApiModelProperty(value = "交互凭证")
    private String token;

    @ApiModelProperty(value = "状态：1-在线；2-离线；3-失效；4-登出")
    private Integer status;

    @ApiModelProperty(value = "失效时间间隔（单位秒）")
    private Integer expiredInterval;

    @ApiModelProperty(value = "失效时间")
    private LocalDateTime expirationTime;

    @ApiModelProperty(value = "创建/登录时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "登出时间")
    private LocalDateTime logoutTime;

    @ApiModelProperty(value = "主机地址")
    private String host;

    @ApiModelProperty(value = "IP归属地址")
    private String ipAddress;

    @ApiModelProperty(value = "终端设备")
    private String terminalDevice;

    @ApiModelProperty(value = "终端操作系统")
    private String terminalOs;

    public void transform(SysUserToken userToken, SysActionLog actionLog) {
        this.tokenId = userToken.getId();
        this.identity = userToken.getIdentity();
        this.token = userToken.getToken();
        this.status = userToken.getStatus();
        this.expiredInterval = userToken.getExpiredInterval();
        this.expirationTime = userToken.getExpirationTime();
        this.createTime = userToken.getCreateTime();
        this.logoutTime = userToken.getLogoutTime();
        if (actionLog != null) {
            this.host = actionLog.getHost();
            this.ipAddress = actionLog.getIpAddress();
            this.terminalDevice = actionLog.getTerminalDevice();
            this.terminalOs = actionLog.getTerminalOs();
        }
    }
}
