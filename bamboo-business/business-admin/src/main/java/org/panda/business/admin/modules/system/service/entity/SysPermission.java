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
 * 系统权限
 * </p>
 *
 * @author bamboo-code-generator
 * @since 2023-06-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sys_permission")
@ApiModel(value="SysPermission对象", description="系统权限")
public class SysPermission implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.INPUT)
    private Integer id;

    @ApiModelProperty(value = "权限名称")
    @TableField("permission_name")
    private String permissionName;

    @ApiModelProperty(value = "权限编码")
    @TableField("permission_code")
    private String permissionCode;

    @ApiModelProperty(value = "描述")
    @TableField("description")
    private String description;

    @ApiModelProperty(value = "资源ID")
    @TableField("resources_id")
    private String resourcesId;

    @ApiModelProperty(value = "资源类型")
    @TableField("resources_type")
    private String resourcesType;

    @ApiModelProperty(value = "权限来源")
    @TableField("source")
    private String source;

    @ApiModelProperty(value = "操作范围")
    @TableField("operation_scope")
    private String operationScope;


}
