package org.panda.business.admin.v1.modules.system.service.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 系统角色权限关系
 * </p>
 *
 * @author bamboo-code-generator
 * @since 2023-06-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sys_role_permission")
@ApiModel(value="SysRolePermission对象", description="系统角色权限关系")
public class SysRolePermission implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "角色表关联ID")
    @TableId("role_id")
    private Integer roleId;

    @ApiModelProperty(value = "权限表关联ID")
    @TableField("permission_id")
    private Integer permissionId;


}
