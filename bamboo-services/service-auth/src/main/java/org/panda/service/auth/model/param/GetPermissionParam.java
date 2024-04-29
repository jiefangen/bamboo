package org.panda.service.auth.model.param;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class GetPermissionParam {
    /**
     * 应用名称
     */
    private String appName;
    /**
     * 资源来源ID
     */
    private Integer resourcesId;
}
