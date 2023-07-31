package org.panda.business.admin.modules.settings.service.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 系统字典数据
 * </p>
 *
 * @author bamboo-code-generator
 * @since 2023-07-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sys_dictionary_data")
@ApiModel(value="SysDictionaryData对象", description="系统字典数据")
public class SysDictionaryData implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "关联字典ID")
    @TableField("dict_id")
    private Integer dictId;

    @ApiModelProperty(value = "字典数据标签")
    @TableField("dict_label")
    private String dictLabel;

    @ApiModelProperty(value = "字典数据值")
    @TableField("dict_value")
    private String dictValue;

    @ApiModelProperty(value = "状态：0-停用；1-正常；")
    @TableField("status")
    private Integer status;

    @ApiModelProperty(value = "是否默认：Y-是；N-否；")
    @TableField("is_default")
    private String isDefault;

    @ApiModelProperty(value = "备注")
    @TableField("remark")
    private String remark;

    @ApiModelProperty(value = "排序")
    @TableField("sort")
    private Integer sort;

    @ApiModelProperty(value = "数据回显样式")
    @TableField("echo_class")
    private String echoClass;

    @ApiModelProperty(value = "数据样式属性")
    @TableField("style_attribute")
    private String styleAttribute;

    @ApiModelProperty(value = "创建者")
    @TableField("creator")
    private String creator;

    @ApiModelProperty(value = "更新者")
    @TableField("updater")
    private String updater;

    @ApiModelProperty(value = "创建时间")
    @TableField("create_time")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField("update_time")
    private LocalDateTime updateTime;


}
