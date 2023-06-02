package org.panda.tech.security.web.access;

import org.panda.tech.security.access.GrantedAuthorityDecider;
import org.panda.tech.security.user.UserConfigAuthority;
import org.panda.tech.security.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * 请求获权判定器
 */
@Component
public class RequestGrantedAuthorityDecider {

    @Autowired
    private ConfigAuthorityResolver configAuthorityResolver;
    @Autowired
    private GrantedAuthorityDecider grantedAuthorityDecider;

    /**
     * 判断当前用户对指定请求是否具有权限
     *
     * @param uri    请求地址
     * @param method 请求方法
     * @return 当前用户对指定请求是否具有权限
     */
    public boolean isGranted(String uri, HttpMethod method) {
        return isGranted(SecurityUtil.getGrantedAuthorities(), uri, method);
    }

    /**
     * 判断指定已获权清单对指定请求是否具有权限
     *
     * @param grantedAuthorities 已获权清单
     * @param uri                请求地址
     * @param method             请求方法
     * @return 指定已获权清单对指定请求是否具有权限
     */
    public boolean isGranted(Collection<? extends GrantedAuthority> grantedAuthorities, String uri, HttpMethod method) {
        Collection<UserConfigAuthority> configAuthorities = this.configAuthorityResolver.resolveConfigAuthorities(
                uri, method);
        if (configAuthorities != null) {
            for (UserConfigAuthority configAuthority : configAuthorities) {
                // 所需权限清单中只要有一个配置权限匹配，则视为权限匹配
                if (this.grantedAuthorityDecider.isGranted(grantedAuthorities, configAuthority.getType(),
                        configAuthority.getRank(), configAuthority.getApp(), configAuthority.getPermission())) {
                    return true;
                }
            }
        }
        return false;
    }

}
