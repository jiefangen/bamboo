package org.panda.service.auth.controller;

import io.swagger.annotations.Api;
import org.apache.commons.collections4.CollectionUtils;
import org.panda.service.auth.service.AppServerService;
import org.panda.tech.core.exception.ExceptionEnum;
import org.panda.tech.core.web.restful.RestfulResult;
import org.panda.tech.security.config.annotation.ConfigAuthority;
import org.panda.tech.security.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.Collection;

/**
 * 服务资源访问控制器
 *
 * @author fangen
 **/
@Api(tags = "服务资源访问控制器")
@RestController
@RequestMapping("/access")
public class AccessController {

    @Autowired
    private AppServerService appServerService;

    /**
     * 服务资源访问验证
     *
     * @param service  服务资源
     * @param response 响应
     */
    @GetMapping("/validate")
    @ConfigAuthority
    public RestfulResult validate(@RequestParam(value = "service", required = false) String service, HttpServletResponse response) {
        Collection<? extends GrantedAuthority> grantedAuthorities = SecurityUtil.getGrantedAuthorities();
        if (CollectionUtils.isEmpty(grantedAuthorities)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return RestfulResult.getFailure(ExceptionEnum.UNAUTHORIZED);
        } else {
            boolean isGranted = appServerService.permissionVerification(service, grantedAuthorities);
            if (!isGranted) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return RestfulResult.getFailure(ExceptionEnum.AUTH_NO_OPERA);
            }
        }
        return RestfulResult.success();
    }
}
