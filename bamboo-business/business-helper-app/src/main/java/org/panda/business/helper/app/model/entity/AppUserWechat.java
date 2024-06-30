package org.panda.business.helper.app.model.entity;

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
 * 微信用户标识
 * </p>
 *
 * @author bamboo-code-generator
 * @since 2024-06-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("app_user_wechat")
@ApiModel(value="AppUserWechat对象", description="微信用户标识")
public class AppUserWechat implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "关联用户ID")
    @TableField("user_id")
    private Integer userId;

    @ApiModelProperty(value = "微信应用类型：MP-小程序；SA-公众号；WEB-网站")
    @TableField("app_type")
    private String appType;

    @ApiModelProperty(value = "微信小程序用户唯一标识")
    @TableField("openid")
    private String openid;

    @ApiModelProperty(value = "用户在开放平台的唯一标识符")
    @TableField("union_id")
    private String unionId;

    @ApiModelProperty(value = "会话密钥")
    @TableField("session_key")
    private String sessionKey;

    @ApiModelProperty(value = "访问凭证")
    @TableField("access_token")
    private String accessToken;

    @ApiModelProperty(value = "凭证有效时间（单位秒）")
    @TableField("expires_in")
    private Integer expiresIn;

    @ApiModelProperty(value = "创建时间")
    @TableField("create_time")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField("update_time")
    private LocalDateTime updateTime;


}
