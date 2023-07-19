package org.panda.business.admin.modules.system.service.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

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

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.INPUT)
    private Integer id;

    @ApiModelProperty(value = "角色表关联ID")
    @TableField("role_id")
    private Integer roleId;

    @ApiModelProperty(value = "权限表关联ID")
    @TableField("permission_id")
    private Integer permissionId;

    @ApiModelProperty(value = "关联方式：internal-内部关联")
    @TableField("association")
    private String association;

}
