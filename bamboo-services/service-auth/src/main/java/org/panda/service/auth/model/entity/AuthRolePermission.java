package org.panda.service.auth.model.entity;

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
 * 角色权限关系
 * </p>
 *
 * @author bamboo-code-generator
 * @since 2023-11-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("auth_role_permission")
@ApiModel(value="AuthRolePermission对象", description="角色权限关系")
public class AuthRolePermission implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "角色表关联ID")
    @TableId("role_id")
    private Integer roleId;

    @ApiModelProperty(value = "权限表关联ID")
    @TableField("permission_id")
    private Integer permissionId;


}
