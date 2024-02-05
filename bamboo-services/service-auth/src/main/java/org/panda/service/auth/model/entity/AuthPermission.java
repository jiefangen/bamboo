package org.panda.service.auth.model.entity;

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
 * 应用资源权限
 * </p>
 *
 * @author bamboo-code-generator
 * @since 2023-10-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("auth_permission")
@ApiModel(value="AuthPermission对象", description="应用资源权限")
public class AuthPermission implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "权限名称")
    @TableField("permission_name")
    private String permissionName;

    @ApiModelProperty(value = "权限编码")
    @TableField("permission_code")
    private String permissionCode;

    @ApiModelProperty(value = "资源ID")
    @TableField("resources_id")
    private Integer resourcesId;

    @ApiModelProperty(value = "资源类型")
    @TableField("resources_type")
    private String resourcesType;

    @ApiModelProperty(value = "权限来源")
    @TableField("source")
    private String source;

    @ApiModelProperty(value = "资源")
    @TableField("resources")
    private String resources;

    @ApiModelProperty(value = "权限范围")
    @TableField("scope")
    private String scope;

    @ApiModelProperty(value = "描述")
    @TableField("description")
    private String description;


}
