package org.panda.service.auth.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.panda.bamboo.common.constant.Commons;
import org.panda.service.auth.infrastructure.security.app.AppServiceModel;
import org.panda.service.auth.service.AppServiceService;
import org.panda.tech.core.exception.ExceptionEnum;
import org.panda.tech.core.web.restful.RestfulResult;
import org.panda.tech.security.config.annotation.ConfigAuthority;
import org.panda.tech.security.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Collection;

/**
 * 应用服务资源控制器
 *
 * @author fangen
 **/
@Api(tags = "应用服务控制器")
@RestController
@RequestMapping("/app/service")
public class AppServiceController {

    @Autowired
    private AppServiceService appServiceService;

    /**
     * 禁止外部资源调用
     */
    @ApiOperation("应用服务资源授权")
    @PostMapping("/authorize")
    public RestfulResult<?> authorize(@RequestBody AppServiceModel appServiceModel) {
        if (appServiceModel == null || StringUtils.isEmpty(appServiceModel.getAppName())) {
            return RestfulResult.failure();
        }
        String result = appServiceService.initServicePermission(appServiceModel);
        if (Commons.RESULT_SUCCESS.equals(result)) {
            return RestfulResult.success();
        } else {
            return RestfulResult.failure(result);
        }
    }

    @ApiOperation("应用服务资源访问验证")
    @GetMapping("/access/validate")
    @ConfigAuthority
    public RestfulResult<?> validate(@RequestParam(value = "service", required = false) String service, HttpServletResponse response) {
        Collection<? extends GrantedAuthority> grantedAuthorities = SecurityUtil.getGrantedAuthorities();
        if (CollectionUtils.isEmpty(grantedAuthorities)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return RestfulResult.getFailure(ExceptionEnum.UNAUTHORIZED);
        } else {
            boolean isGranted = appServiceService.permissionVerification(service, grantedAuthorities);
            if (!isGranted) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return RestfulResult.getFailure(ExceptionEnum.AUTH_NO_OPERA);
            }
        }
        return RestfulResult.success();
    }
}
