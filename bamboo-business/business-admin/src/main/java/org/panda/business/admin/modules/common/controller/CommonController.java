package org.panda.business.admin.modules.common.controller;

import io.swagger.annotations.Api;
import org.panda.business.admin.common.constant.Authority;
import org.panda.business.admin.modules.common.manager.SettingsManager;
import org.panda.business.admin.modules.settings.service.entity.SysDictionaryData;
import org.panda.tech.core.spec.log.ActionType;
import org.panda.tech.core.web.config.annotation.WebOperationLog;
import org.panda.tech.core.web.restful.RestfulResult;
import org.panda.tech.security.config.annotation.ConfigAuthorities;
import org.panda.tech.security.config.annotation.ConfigAuthority;
import org.panda.tech.security.config.annotation.ConfigPermission;
import org.panda.tech.security.web.AuthoritiesBizExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 系统常规通用管理
 *
 * @author fangen
 **/
@Api(tags = "系统常规通用管理")
@RestController
@RequestMapping("/common")
public class CommonController {

    @Autowired
    private SettingsManager settingsManager;
    @Autowired
    private AuthoritiesBizExecutor authoritiesBizExecutor;

    @GetMapping("/settings/getDictData")
    @ConfigPermission
    public RestfulResult getDictData(@RequestParam String dictKey, @RequestParam(required = false) String appRange,
                                     @RequestParam(required = false) boolean onlyValue) {
        List<SysDictionaryData> dictionaryData = settingsManager.getDictData(dictKey, appRange);
        if (onlyValue) {
            List<String> dictValues = dictionaryData.stream().map(SysDictionaryData::getDictValue)
                    .collect(Collectors.toList());
            return RestfulResult.success(dictValues);
        }
        return RestfulResult.success(dictionaryData);
    }

    @GetMapping("/rolePer/refresh")
    @ConfigAuthorities({
            @ConfigAuthority(permission = Authority.ROLE_SYSTEM),
            @ConfigAuthority(type = Authority.TYPE_MANAGER, permission = "common_rolePer_refresh")
    })
    @WebOperationLog(actionType = ActionType.REFRESH, intoStorage = true)
    public RestfulResult refresh() {
        authoritiesBizExecutor.execute();
        return RestfulResult.success();
    }

}
