package org.panda.business.helper.app.model.params;

import lombok.Data;
import org.panda.support.openapi.model.EncryptedData;

import javax.validation.constraints.NotNull;

/**
 * 更新用户参数
 *
 * @author fangen
 * @since 2024/6/6
 **/
@Data
public class UpdateUserParam {
    /**
     * 用户ID
     */
    @NotNull
    private Integer userId;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 头像
     */
    private String avatar;
    /**
     * 用户昵称
     */
    private String nickname;

/* ---------- 微信小程序专用参数 ---------- */
    /**
     * 加密数据
     */
    private EncryptedData encryptedData;
}
