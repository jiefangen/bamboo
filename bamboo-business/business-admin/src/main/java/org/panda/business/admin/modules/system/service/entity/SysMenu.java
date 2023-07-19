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
 * 系统菜单
 * </p>
 *
 * @author bamboo-code-generator
 * @since 2023-06-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sys_menu")
@ApiModel(value="SysMenu对象", description="系统菜单")
public class SysMenu implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "父级ID（0代表根目录）")
    @TableField("parent_id")
    private Integer parentId;

    @ApiModelProperty(value = "菜单路径")
    @TableField("menu_path")
    private String menuPath;

    @ApiModelProperty(value = "菜单组件")
    @TableField("component")
    private String component;

    @ApiModelProperty(value = "菜单名")
    @TableField("menu_name")
    private String menuName;

    @ApiModelProperty(value = "跳转路径")
    @TableField("redirect")
    private String redirect;

    @ApiModelProperty(value = "标题")
    @TableField("title")
    private String title;

    @ApiModelProperty(value = "图标")
    @TableField("icon")
    private String icon;

    @ApiModelProperty(value = "隐藏")
    @TableField("hidden")
    private Boolean hidden;

    @ApiModelProperty(value = "排序")
    @TableField("sort")
    private Integer sort;


}
