package org.panda.business.admin.modules.settings.service.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 系统字典
 * </p>
 *
 * @author bamboo-code-generator
 * @since 2023-07-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sys_dictionary")
@ApiModel(value="SysDictionary对象", description="系统字典")
public class SysDictionary implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "字典名称")
    @TableField("dict_name")
    private String dictName;

    @ApiModelProperty(value = "字典键名")
    @TableField("dict_key")
    private String dictKey;

    @ApiModelProperty(value = "字典类型")
    @TableField("dict_type")
    private String dictType;

    @ApiModelProperty(value = "状态：0-停用；1-正常；")
    @TableField("status")
    private Integer status;

    @ApiModelProperty(value = "应用范围")
    @TableField("app_range")
    private String appRange;

    @ApiModelProperty(value = "备注")
    @TableField("remark")
    private String remark;

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
