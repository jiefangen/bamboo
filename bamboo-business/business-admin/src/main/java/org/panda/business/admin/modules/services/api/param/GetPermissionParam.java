package org.panda.business.admin.modules.services.api.param;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class GetPermissionParam {
    /**
     * 权限资源来源，应用服务ID
     */
    private Integer id;

    /**
     * 应用名称
     */
    private String appName;
    /**
     * 资源来源ID
     */
    private Integer resourcesId;
}
