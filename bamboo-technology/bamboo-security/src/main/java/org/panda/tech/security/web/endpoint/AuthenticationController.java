package org.panda.tech.security.web.endpoint;

import org.apache.commons.collections4.CollectionUtils;
import org.panda.tech.core.config.app.AppConstants;
import org.panda.tech.security.access.GrantedAuthorityDecider;
import org.panda.tech.security.config.annotation.ConfigAnonymous;
import org.panda.tech.security.config.annotation.ConfigAuthority;
import org.panda.tech.security.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.Collection;

/**
 * 已登录凭证控制器
 */
@RestController
@RequestMapping("/authentication")
public class AuthenticationController {

    @Autowired
    private GrantedAuthorityDecider grantedAuthorityDecider;
    @Value(AppConstants.EL_SPRING_APP_NAME)
    private String appName;

    @GetMapping("/authorized")
    @ConfigAnonymous
    public boolean isAuthorized() {
        return SecurityUtil.isAuthorized();
    }

    /**
     * 判断当前是否具有指定授权，匿名即可访问
     *
     * @param type       类型
     * @param rank       级别
     * @param permission 许可
     * @return 是否具有指定授权
     */
    @GetMapping("/granted")
    @ConfigAnonymous
    public boolean isGranted(@RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "rank", required = false) String rank,
            @RequestParam(value = "permission", required = false) String permission) {
        Collection<? extends GrantedAuthority> grantedAuthorities = SecurityUtil.getGrantedAuthorities();
        if (CollectionUtils.isEmpty(grantedAuthorities)) {
            return false;
        }
        return this.grantedAuthorityDecider.isGranted(grantedAuthorities, type, rank, this.appName, permission);
    }

    /**
     * 校验登录用户访问权限，匿名用户由框架抛出异常
     *
     * @param type       类型
     * @param rank       级别
     * @param permission 许可
     * @param response   响应
     */
    @GetMapping("/validate")
    @ConfigAuthority
    public void validate(@RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "rank", required = false) String rank,
            @RequestParam(value = "permission", required = false) String permission, HttpServletResponse response) {
        if (!isGranted(type, rank, permission)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

}
