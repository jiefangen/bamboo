package org.panda.business.official.modules.system.service.entity;

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
 * 系统用户角色关系
 * </p>
 *
 * @author bamboo-code-generator
 * @since 2023-06-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sys_user_role")
@ApiModel(value="SysUserRole对象", description="系统用户角色关系")
public class SysUserRole implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户表关联ID")
    @TableId("user_id")
    private Integer userId;

    @ApiModelProperty(value = "角色表关联ID")
    @TableField("role_id")
    private Integer roleId;


}
