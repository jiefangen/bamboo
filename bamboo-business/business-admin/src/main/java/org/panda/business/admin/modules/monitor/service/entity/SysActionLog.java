package org.panda.business.admin.modules.monitor.service.entity;

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
 * 系统操作日志
 * </p>
 *
 * @author bamboo-code-generator
 * @since 2023-06-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sys_action_log")
@ApiModel(value="SysActionLog对象", description="系统操作日志")
public class SysActionLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "操作类型")
    @TableField("action_type")
    private String actionType;

    @ApiModelProperty(value = "来源ID")
    @TableField("source_id")
    private String sourceId;

    @ApiModelProperty(value = "操作内容")
    @TableField("content")
    private String content;

    @ApiModelProperty(value = "主机地址")
    @TableField("host")
    private String host;

    @ApiModelProperty(value = "IP归属地址")
    @TableField("ip_address")
    private String ipAddress;

    @ApiModelProperty(value = "终端设备")
    @TableField("terminal_device")
    private String terminalDevice;

    @ApiModelProperty(value = "终端操作系统")
    @TableField("terminal_os")
    private String terminalOs;

    @ApiModelProperty(value = "身份标识")
    @TableField("identity")
    private String identity;

    @ApiModelProperty(value = "操作时间")
    @TableField("operating_time")
    private LocalDateTime operatingTime;

    @ApiModelProperty(value = "耗时")
    @TableField("elapsed_time")
    private Long elapsedTime;

    @ApiModelProperty(value = "操作结果状态")
    @TableField("status_code")
    private Integer statusCode;

    @ApiModelProperty(value = "异常信息")
    @TableField("exception_info")
    private String exceptionInfo;


}
