package org.panda.business.admin.v1.modules;

import io.swagger.annotations.Api;
import org.panda.business.admin.v1.common.constant.Authority;
import org.panda.tech.core.web.restful.RestfulResult;
import org.panda.tech.security.config.annotation.ConfigAnonymous;
import org.panda.tech.security.config.annotation.ConfigAuthorities;
import org.panda.tech.security.config.annotation.ConfigAuthority;
import org.panda.tech.security.config.annotation.ConfigPermission;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 权限验证模版控制器
 *
 * @author fangen
 **/
@Api(tags = "权限验证模版控制器")
@RestController("/authority")
public class AuthorityController {
    /**
     * 匿名即可访问
     */
    @GetMapping("/accessAnonymous")
    @ConfigAnonymous
    public RestfulResult accessAnonymous() {
        return RestfulResult.success(true);
    }
    /**
     * 只要认证成功即可访问
     */
    @GetMapping("/accessAuthSucceed")
    @ConfigPermission
    public RestfulResult accessAuthSucceed() {
        return RestfulResult.success(true);
    }

    /**
     * 需要拥有manager用户类型才可以访问
     */
    @GetMapping("/accessManagerType")
    @ConfigPermission(type = Authority.TYPE_MANAGER)
    public RestfulResult accessManagerType() {
        return RestfulResult.success(true);
    }

    /**
     * 需要达到用户等级1才可以访问
     */
    @GetMapping("/accessRank1")
    @ConfigPermission(rank = Authority.RANK_1)
    public RestfulResult accessRank1() {
        return RestfulResult.success(true);
    }

    /**
     * 需要拥有SYSTEM角色权限才可以访问
     */
    @GetMapping("/accessSystemPer")
    @ConfigAuthorities({
            @ConfigAuthority(permission = Authority.PER_SYSTEM),
            @ConfigAuthority(permission = Authority.PER_USER),
            @ConfigAuthority(permission = Authority.PER_CUSTOMER)
    })
    public RestfulResult accessSystemPer() {
        return RestfulResult.success(true);
    }


    /**
     * 需要同时拥有指定的所有权限才可以访问
     */
    @GetMapping("/accessAllPermission")
    @ConfigAuthority(type = Authority.TYPE_MANAGER, rank = Authority.RANK_0, permission = Authority.PER_ACTUATOR)
    public RestfulResult accessAllPermission() {
        return RestfulResult.success(true);
    }

    /**
     * 拒绝所有权限访问
     */
    @GetMapping("/accessDenyAll")
    public RestfulResult accessDenyAll() {
        return RestfulResult.success(true);
    }

}
