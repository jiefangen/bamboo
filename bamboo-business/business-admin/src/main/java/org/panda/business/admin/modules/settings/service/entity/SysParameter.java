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
 * 系统参数
 * </p>
 *
 * @author bamboo-code-generator
 * @since 2023-07-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sys_parameter")
@ApiModel(value="SysParameter对象", description="系统参数")
public class SysParameter implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "参数名称")
    @TableField("parameter_name")
    private String parameterName;

    @ApiModelProperty(value = "参数键")
    @TableField("parameter_key")
    private String parameterKey;

    @ApiModelProperty(value = "参数值")
    @TableField("parameter_value")
    private String parameterValue;

    @ApiModelProperty(value = "参数类型：internal-内置；external-外置；")
    @TableField("parameter_type")
    private String parameterType;

    @ApiModelProperty(value = "状态：0-关闭；1-开启；")
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
