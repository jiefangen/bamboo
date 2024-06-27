package org.panda.business.helper.app.model.vo;

import lombok.Data;
import org.panda.business.helper.app.model.entity.AppUser;

/**
 * 用户详情
 *
 * @author fangen
 * @since JDK 11 2024/6/13
 **/
@Data
public class UserInfo {
    /**
     * 用户ID
     */
    private Integer userId;
    /**
     * 用户名
     */
    private String username;
    /**
     * 用户级别
     */
    private Integer userRank;
    /**
     * 昵称
     */
    private String nickname;
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
     * 用户交互凭证
     */
    private String token;
    /**
     * token有效时间
     */
    private Long tokenEffectiveTime;

    public void transform(AppUser appUser) {
        this.userId = appUser.getId();
        this.username = appUser.getUsername();
        this.userRank = appUser.getUserRank();
        this.nickname = appUser.getNickname();
        this.phone = appUser.getPhone();
        this.email = appUser.getEmail();
        this.avatar = appUser.getAvatar();
    }
}
