package org.panda.business.admin.modules.settings.api.controller;

import io.swagger.annotations.Api;
import org.panda.bamboo.common.constant.Commons;
import org.panda.bamboo.common.exception.business.BusinessException;
import org.panda.business.admin.common.constant.Authority;
import org.panda.business.admin.modules.settings.api.param.ParameterParam;
import org.panda.business.admin.modules.settings.api.param.ParameterQueryParam;
import org.panda.business.admin.modules.settings.service.SysParameterService;
import org.panda.business.admin.modules.settings.service.entity.SysParameter;
import org.panda.tech.core.spec.log.ActionType;
import org.panda.tech.core.web.config.annotation.WebOperationLog;
import org.panda.tech.core.web.restful.RestfulResult;
import org.panda.tech.data.model.query.QueryResult;
import org.panda.tech.security.config.annotation.ConfigAuthorities;
import org.panda.tech.security.config.annotation.ConfigAuthority;
import org.panda.tech.security.config.annotation.ConfigPermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 系统参数
 *
 * @author fangen
 **/
@Api(tags = "系统参数设置")
@RestController
@RequestMapping("/settings/parameter")
public class ParameterController {

    @Autowired
    private SysParameterService parameterService;

    @PostMapping("/page")
    @ConfigPermission
    public RestfulResult page(@RequestBody ParameterQueryParam queryParam) {
        QueryResult<SysParameter> actionLogPage = parameterService.getParamByPage(queryParam);
        return RestfulResult.success(actionLogPage);
    }

    @PostMapping("/add")
    @ConfigAuthorities({
            @ConfigAuthority(permission = Authority.ROLE_SYSTEM),
            @ConfigAuthority(type = Authority.TYPE_MANAGER, permission = "settings_param_add")
    })
    @WebOperationLog(actionType = ActionType.ADD, intoStorage = true)
    public RestfulResult add(@RequestBody @Valid ParameterParam parameterParam) {
        String result = parameterService.addParameter(parameterParam);
        if (Commons.RESULT_SUCCESS.equals(result)) {
            return RestfulResult.success();
        }
        return RestfulResult.failure(result);
    }

    @PutMapping("/edit")
    @ConfigAuthorities({
            @ConfigAuthority(permission = Authority.ROLE_SYSTEM),
            @ConfigAuthority(type = Authority.TYPE_MANAGER, permission = "settings_param_edit")
    })
    @WebOperationLog(actionType = ActionType.UPDATE, intoStorage = true)
    public RestfulResult edit(@RequestBody @Valid ParameterParam parameterParam) {
        if (parameterService.updateParameter(parameterParam)) {
            return RestfulResult.success();
        }
        return RestfulResult.failure();
    }

    @DeleteMapping("/del/{id}")
    @ConfigAuthorities({
            @ConfigAuthority(permission = Authority.ROLE_SYSTEM),
            @ConfigAuthority(type = Authority.TYPE_MANAGER, permission = "settings_param_del")
    })
    @WebOperationLog(actionType = ActionType.DEL, intoStorage = true)
    public RestfulResult del(@PathVariable Integer id) {
        try {
            if (parameterService.deleteParameter(id)) {
                return RestfulResult.success();
            }
        } catch (BusinessException e){
            return RestfulResult.failure(e.getMessage());
        }
        return RestfulResult.failure();
    }

}
