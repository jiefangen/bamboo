package org.panda.business.admin.modules.services.api.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 认证服务视图
 *
 * @author fangen
 **/
@Data
public class AppServerVO implements Serializable {
    private static final long serialVersionUID = -2909021108889612775L;

    private Integer id;

    @ApiModelProperty(value = "应用服务名称")
    private String appName;

    @ApiModelProperty(value = "应用服务编码")
    private String appCode;

    @ApiModelProperty(value = "应用运行环境")
    private String env;

    @ApiModelProperty(value = "运行服务器地址")
    private String host;

    @ApiModelProperty(value = "标题")
    private String caption;

    @ApiModelProperty(value = "业务")
    private String business;

    @ApiModelProperty(value = "状态：0-停用；1-正常；2-维护中；4-故障")
    private Integer status;

    @ApiModelProperty(value = "应用服务范围")
    private String scope;

}
