package org.panda.service.auth.model.entity;

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
 * 应用服务节点
 * </p>
 *
 * @author bamboo-code-generator
 * @since 2025-01-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("app_service_node")
@ApiModel(value="AppServiceNode对象", description="应用服务节点")
public class AppServiceNode implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "应用服务ID")
    @TableField("service_id")
    private Integer serviceId;

    @ApiModelProperty(value = "应用服务名称")
    @TableField("app_name")
    private String appName;

    @ApiModelProperty(value = "运行服务器地址")
    @TableField("host")
    private String host;

    @ApiModelProperty(value = "服务直连URI")
    @TableField("direct_uri")
    private String directUri;

    @ApiModelProperty(value = "状态：0-故障；1-正常")
    @TableField("status")
    private Integer status;

    @ApiModelProperty(value = "创建时间")
    @TableField("create_time")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField("update_time")
    private LocalDateTime updateTime;


}
