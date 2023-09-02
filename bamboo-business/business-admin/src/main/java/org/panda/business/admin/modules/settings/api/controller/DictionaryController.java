package org.panda.business.admin.modules.settings.api.controller;

import io.swagger.annotations.Api;
import org.panda.bamboo.common.constant.Commons;
import org.panda.bamboo.common.exception.business.BusinessException;
import org.panda.business.admin.common.constant.Authority;
import org.panda.business.admin.modules.common.manager.SettingsManager;
import org.panda.business.admin.modules.settings.api.param.DictionaryParam;
import org.panda.business.admin.modules.settings.api.param.DictionaryQueryParam;
import org.panda.business.admin.modules.settings.service.SysDictionaryService;
import org.panda.business.admin.modules.settings.service.entity.SysDictionary;
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
import java.util.List;

/**
 * 系统字典
 *
 * @author fangen
 **/
@Api(tags = "系统字典管理")
@RestController
@RequestMapping("/settings/dictionary")
public class DictionaryController {

    @Autowired
    private SysDictionaryService dictionaryService;
    @Autowired
    private SettingsManager settingsManager;

    @PostMapping("/page")
    @ConfigPermission
    public RestfulResult page(@RequestBody DictionaryQueryParam queryParam) {
        QueryResult<SysDictionary> dictionaryPage = dictionaryService.getDictByPage(queryParam);
        return RestfulResult.success(dictionaryPage);
    }

    @GetMapping("/getAllDict")
    @ConfigPermission
    public RestfulResult getAllDict() {
        List<SysDictionary> dictionaryList = settingsManager.getAllDictionary();
        return RestfulResult.success(dictionaryList);
    }

    @PostMapping("/add")
    @ConfigAuthorities({
            @ConfigAuthority(permission = Authority.ROLE_SYSTEM),
            @ConfigAuthority(type = Authority.TYPE_MANAGER, permission = "settings_dict_add")
    })
    @WebOperationLog(actionType = ActionType.ADD, intoStorage = true)
    public RestfulResult add(@RequestBody @Valid DictionaryParam dictionaryParam) {
        String result = dictionaryService.addDictionary(dictionaryParam);
        if (Commons.RESULT_SUCCESS.equals(result)) {
            return RestfulResult.success();
        }
        return RestfulResult.failure(result);
    }

    @PutMapping("/edit")
    @ConfigAuthorities({
            @ConfigAuthority(permission = Authority.ROLE_SYSTEM),
            @ConfigAuthority(type = Authority.TYPE_MANAGER, permission = "settings_dict_edit")
    })
    @WebOperationLog(actionType = ActionType.UPDATE, intoStorage = true)
    public RestfulResult edit(@RequestBody @Valid DictionaryParam dictionaryParam) {
        if (dictionaryService.updateDictionary(dictionaryParam)) {
            return RestfulResult.success();
        }
        return RestfulResult.failure();
    }

    @DeleteMapping("/del/{id}")
    @ConfigAuthorities({
            @ConfigAuthority(permission = Authority.ROLE_SYSTEM),
            @ConfigAuthority(type = Authority.TYPE_MANAGER, permission = "settings_dict_del")
    })
    @WebOperationLog(actionType = ActionType.DEL, intoStorage = true)
    public RestfulResult del(@PathVariable Integer id) {
        try {
            if (dictionaryService.deleteDictionary(id)) {
                return RestfulResult.success();
            }
        } catch (BusinessException e){
            return RestfulResult.failure(e.getMessage());
        }
        return RestfulResult.failure();
    }

}
