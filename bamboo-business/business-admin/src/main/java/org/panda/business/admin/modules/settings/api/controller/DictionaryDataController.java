package org.panda.business.admin.modules.settings.api.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.panda.bamboo.common.constant.Commons;
import org.panda.bamboo.common.exception.business.BusinessException;
import org.panda.business.admin.common.constant.Authority;
import org.panda.business.admin.modules.settings.api.param.DictDataParam;
import org.panda.business.admin.modules.settings.api.param.DictDataQueryParam;
import org.panda.business.admin.modules.settings.service.SysDictionaryDataService;
import org.panda.business.admin.modules.settings.service.SysDictionaryService;
import org.panda.business.admin.modules.settings.service.entity.SysDictionary;
import org.panda.business.admin.modules.settings.service.entity.SysDictionaryData;
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
 * 系统字典数据
 *
 * @author fangen
 **/
@Api(tags = "系统字典数据管理")
@RestController
@RequestMapping("/settings/dictionary/data")
public class DictionaryDataController {

    @Autowired
    private SysDictionaryDataService dictionaryDataService;
    @Autowired
    private SysDictionaryService dictionaryService;

    @PostMapping("/page")
    @ConfigPermission
    public RestfulResult page(@RequestBody @Valid DictDataQueryParam queryParam) {
        if (queryParam.getDictId() == null) {
            LambdaQueryWrapper<SysDictionary> queryWrapper = new LambdaQueryWrapper<>();
            String dictKey = queryParam.getDictKey();
            queryWrapper.eq(StringUtils.isNotBlank(dictKey), SysDictionary::getDictKey, dictKey);
            SysDictionary dictionary = dictionaryService.getOne(queryWrapper, false);
            queryParam.setDictId(dictionary.getId());
        }
        QueryResult<SysDictionaryData> dictionaryPage = dictionaryDataService.getDictDataByPage(queryParam);
        return RestfulResult.success(dictionaryPage);
    }

    @PostMapping("/add")
    @ConfigAuthorities({
            @ConfigAuthority(permission = Authority.ROLE_SYSTEM),
            @ConfigAuthority(type = Authority.TYPE_MANAGER, permission = "settings_dict_data_add")
    })
    @WebOperationLog(actionType = ActionType.ADD, intoStorage = true)
    public RestfulResult add(@RequestBody @Valid DictDataParam dictDataParam) {
        LambdaQueryWrapper<SysDictionary> queryWrapper = new LambdaQueryWrapper<>();
        String dictKey = dictDataParam.getDictKey();
        queryWrapper.eq(StringUtils.isNotBlank(dictKey), SysDictionary::getDictKey, dictKey);
        SysDictionary dictionary = dictionaryService.getOne(queryWrapper, false);
        if (dictionary != null) {
            dictDataParam.setDictId(dictionary.getId());
        }
        String result = dictionaryDataService.addDictData(dictDataParam);
        if (Commons.RESULT_SUCCESS.equals(result)) {
            return RestfulResult.success();
        }
        return RestfulResult.failure(result);
    }

    @PutMapping("/edit")
    @ConfigAuthorities({
            @ConfigAuthority(permission = Authority.ROLE_SYSTEM),
            @ConfigAuthority(type = Authority.TYPE_MANAGER, permission = "settings_dict_data_edit")
    })
    @WebOperationLog(actionType = ActionType.UPDATE, intoStorage = true)
    public RestfulResult edit(@RequestBody @Valid DictDataParam dictDataParam) {
        LambdaQueryWrapper<SysDictionary> queryWrapper = new LambdaQueryWrapper<>();
        String dictKey = dictDataParam.getDictKey();
        queryWrapper.eq(StringUtils.isNotBlank(dictKey), SysDictionary::getDictKey, dictKey);
        SysDictionary dictionary = dictionaryService.getOne(queryWrapper, false);
        if (dictionary != null) {
            dictDataParam.setDictId(dictionary.getId());
        }
        if (dictionaryDataService.updateDictData(dictDataParam)) {
            return RestfulResult.success();
        }
        return RestfulResult.failure();
    }

    @DeleteMapping("/del/{id}")
    @ConfigAuthorities({
            @ConfigAuthority(permission = Authority.ROLE_SYSTEM),
            @ConfigAuthority(type = Authority.TYPE_MANAGER, permission = "settings_dict_data_del")
    })
    @WebOperationLog(actionType = ActionType.DEL, intoStorage = true)
    public RestfulResult del(@PathVariable Integer id) {
        try {
            if (dictionaryDataService.deleteDictData(id)) {
                return RestfulResult.success();
            }
        } catch (BusinessException e){
            return RestfulResult.failure(e.getMessage());
        }
        return RestfulResult.failure();
    }

}
