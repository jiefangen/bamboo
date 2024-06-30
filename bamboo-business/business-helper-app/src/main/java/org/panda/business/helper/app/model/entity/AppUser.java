package org.panda.business.helper.app.model.entity;

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
 * APP用户
 * </p>
 *
 * @author bamboo-code-generator
 * @since 2024-06-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("app_user")
@ApiModel(value="AppUser对象", description="APP用户")
public class AppUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "用户名")
    @TableField("username")
    private String username;

    @ApiModelProperty(value = "密码")
    @TableField("password")
    private String password;

    @ApiModelProperty(value = "用户级别：1～n")
    @TableField("user_rank")
    private Integer userRank;

    @ApiModelProperty(value = "随机盐")
    @TableField("salt")
    private String salt;

    @ApiModelProperty(value = "昵称")
    @TableField("nickname")
    private String nickname;

    @ApiModelProperty(value = "手机号")
    @TableField("phone")
    private String phone;

    @ApiModelProperty(value = "邮箱")
    @TableField("email")
    private String email;

    @ApiModelProperty(value = "性别：0-未知；1-男性；2-女性")
    @TableField("gender")
    private Integer gender;

    @ApiModelProperty(value = "头像")
    @TableField("avatar")
    private String avatar;

    @ApiModelProperty(value = "微信小程序用户唯一标识")
    @TableField("openid")
    private String openid;

    @ApiModelProperty(value = "应用appid")
    @TableField("appid")
    private String appid;

    @ApiModelProperty(value = "用户来源：WECHAT-MINI-微信小程序；ANDROID-安卓；IOS-苹果")
    @TableField("source")
    private String source;

    @ApiModelProperty(value = "来源渠道")
    @TableField("source_channel")
    private String source_channel;

    @ApiModelProperty(value = "状态：0-虚拟；1-正常；4-删除；9-锁定")
    @TableField("status")
    private Integer status;

    @ApiModelProperty(value = "启用")
    @TableField("enabled")
    private Boolean enabled;

    @ApiModelProperty(value = "创建时间")
    @TableField("create_time")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField("update_time")
    private LocalDateTime updateTime;


}
