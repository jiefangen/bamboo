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
import java.time.LocalDateTime;

/**
 * <p>
 * 应用认证账户
 * </p>
 *
 * @author bamboo-code-generator
 * @since 2023-10-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("auth_account")
@ApiModel(value="AuthAccount对象", description="应用认证账户")
public class AuthAccount implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "账户名")
    @TableField("username")
    private String username;

    @ApiModelProperty(value = "密码")
    @TableField("password")
    private String password;

    @ApiModelProperty(value = "密钥")
    @TableField("secret_key")
    private String secretKey;

    @ApiModelProperty(value = "账户凭证")
    @TableField("credentials")
    private String credentials;

    @ApiModelProperty(value = "商户号")
    @TableField("merchant_num")
    private String merchantNum;

    @ApiModelProperty(value = "账户类型")
    @TableField("account_type")
    private String accountType;

    @ApiModelProperty(value = "账户等级")
    @TableField("account_rank")
    private String accountRank;

    @ApiModelProperty(value = "邮箱")
    @TableField("email")
    private String email;

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
