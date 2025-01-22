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
 * 应用服务
 * </p>
 *
 * @author bamboo-code-generator
 * @since 2025-01-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("app_service")
@ApiModel(value="AppService对象", description="应用服务")
public class AppService implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "应用服务名称")
    @TableField("app_name")
    private String appName;

    @ApiModelProperty(value = "应用服务编码")
    @TableField("app_code")
    private String appCode;

    @ApiModelProperty(value = "应用运行环境")
    @TableField("env")
    private String env;

    @ApiModelProperty(value = "标题")
    @TableField("caption")
    private String caption;

    @ApiModelProperty(value = "服务网关URI")
    @TableField("gateway_uri")
    private String gatewayUri;

    @ApiModelProperty(value = "上下文路径")
    @TableField("context_path")
    private String contextPath;

    @ApiModelProperty(value = "状态：0-故障；1-正常；2-维护中")
    @TableField("status")
    private Integer status;

    @ApiModelProperty(value = "应用服务范围")
    @TableField("scope")
    private String scope;


}
