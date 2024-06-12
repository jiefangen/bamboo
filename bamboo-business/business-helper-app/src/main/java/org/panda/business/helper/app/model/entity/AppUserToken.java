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
 * APP用户token
 * </p>
 *
 * @author bamboo-code-generator
 * @since 2024-06-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("app_user_token")
@ApiModel(value="AppUserToken对象", description="APP用户token")
public class AppUserToken implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "关联用户ID")
    @TableField("user_id")
    private Integer userId;

    @ApiModelProperty(value = "身份标识")
    @TableField("identity")
    private String identity;

    @ApiModelProperty(value = "交互凭证")
    @TableField("token")
    private String token;

    @ApiModelProperty(value = "状态：1-在线；2-离线；3-失效；4-登出")
    @TableField("status")
    private Integer status;

    @ApiModelProperty(value = "失效时间间隔（单位秒）")
    @TableField("expired_interval")
    private Integer expiredInterval;

    @ApiModelProperty(value = "失效时间")
    @TableField("expiration_time")
    private LocalDateTime expirationTime;

    @ApiModelProperty(value = "登出时间")
    @TableField("logout_time")
    private LocalDateTime logoutTime;

    @ApiModelProperty(value = "创建/登录时间")
    @TableField("create_time")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField("update_time")
    private LocalDateTime updateTime;


}
