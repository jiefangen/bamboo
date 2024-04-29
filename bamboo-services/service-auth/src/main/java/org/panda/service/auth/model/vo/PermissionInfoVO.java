package org.panda.service.auth.model.vo;

import lombok.Data;

/**
 * 权限信息视图
 *
 * @author fangen
 **/
@Data
public class PermissionInfoVO {
    /**
     * 权限名称
     */
    private String permissionName;
    /**
     * 权限编码
     */
    private String permissionCode;
    /**
     * 资源ID
     */
    private Integer resourcesId;
    /**
     * 资源类型
     */
    private String resourcesType;
    /**
     * 权限来源
     */
    private String source;
    /**
     * 资源
     */
    private String resources;
    /**
     * 权限范围
     */
    private String scope;
    /**
     * 描述
     */
    private String description;
    /**
     * 角色范围
     */
    private String roleScope;
}
