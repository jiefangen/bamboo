package org.panda.business.admin.modules.settings.api.controller;

import io.swagger.annotations.Api;
import org.panda.business.admin.common.constant.Authority;
import org.panda.business.admin.modules.settings.api.param.ParameterQueryParam;
import org.panda.business.admin.modules.settings.service.SysParameterService;
import org.panda.business.admin.modules.settings.service.entity.SysParameter;
import org.panda.tech.core.web.restful.RestfulResult;
import org.panda.tech.data.model.query.QueryResult;
import org.panda.tech.security.config.annotation.ConfigAuthorities;
import org.panda.tech.security.config.annotation.ConfigAuthority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系统参数
 *
 * @author fangen
 **/
@Api(tags = "系统配置管理")
@RestController
@RequestMapping("/settings/parameter")
public class ParameterController {

    @Autowired
    private SysParameterService parameterService;

    @PostMapping("/page")
    @ConfigAuthorities({
            @ConfigAuthority(permission = Authority.ROLE_SYSTEM),
            @ConfigAuthority(type = Authority.TYPE_MANAGER, permission = "settings_param_page")
    })
    public RestfulResult page(@RequestBody ParameterQueryParam queryParam) {
        QueryResult<SysParameter> actionLogPage = parameterService.getParamByPage(queryParam);
        return RestfulResult.success(actionLogPage);
    }
}
