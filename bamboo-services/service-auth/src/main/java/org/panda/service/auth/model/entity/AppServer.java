package org.panda.service.auth.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 应用服务
 * </p>
 *
 * @author bamboo-code-generator
 * @since 2023-10-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("app_server")
@ApiModel(value="AppServer对象", description="应用服务")
public class AppServer implements Serializable {

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

    @ApiModelProperty(value = "标题")
    @TableField("caption")
    private String caption;

    @ApiModelProperty(value = "业务")
    @TableField("business")
    private String business;

    @ApiModelProperty(value = "状态：0-停用；1-正常；2-维护中；4-故障")
    @TableField("status")
    private Integer status;

    @ApiModelProperty(value = "应用服务范围")
    @TableField("scope")
    private String scope;


}
