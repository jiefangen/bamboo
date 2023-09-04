package org.panda.business.admin.modules.common.manager;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.panda.bamboo.common.constant.basic.Strings;
import org.panda.business.admin.common.util.CommonUtil;
import org.panda.business.admin.modules.common.config.SettingsKeys;
import org.panda.business.admin.modules.settings.service.SysDictionaryDataService;
import org.panda.business.admin.modules.settings.service.SysDictionaryService;
import org.panda.business.admin.modules.settings.service.SysParameterService;
import org.panda.business.admin.modules.settings.service.entity.SysDictionary;
import org.panda.business.admin.modules.settings.service.entity.SysDictionaryData;
import org.panda.business.admin.modules.settings.service.entity.SysParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 系统配置组件服务
 *
 * @author fangen
 **/
@Component
public class SettingsManager {

    @Autowired
    private SysParameterService parameterService;
    @Autowired
    private SysDictionaryService dictionaryService;
    @Autowired
    private SysDictionaryDataService dictionaryDataService;

    /**
     * 获取配置参数的值
     *
     * @param paramKey 参数键
     * @param appRange 应用范围
     * @return 参数值
     */
    public Optional<String> getParamValue(String paramKey, String appRange){
        LambdaQueryWrapper<SysParameter> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotEmpty(appRange), SysParameter::getAppRange, appRange);
        queryWrapper.eq(SysParameter::getParamKey, paramKey);
        queryWrapper.eq(SysParameter::getStatus, 1);
        SysParameter parameter = parameterService.getOne(queryWrapper, false);
        if (parameter != null) {
            return Optional.ofNullable(parameter.getParamValue());
        }
        return Optional.empty();
    }

    /**
     * 获取配置参数的值
     *
     * @param paramKey 参数键
     * @return 参数值
     */
    public Optional<String> getParamValueByKey(String paramKey) {
        return getParamValue(paramKey, Strings.STR_NULL);
    }

    /**
     * 获取字典数据
     *
     * @param dictKey 字典键
     * @param appRange 字典应用范围
     * @param dictDataValue 字典数据值
     * @return 字典数据
     */
    public List<SysDictionaryData> getDictData(String dictKey, String appRange, String dictDataValue) {
        LambdaQueryWrapper<SysDictionary> dictWrapper = new LambdaQueryWrapper<>();
        dictWrapper.eq(StringUtils.isNotEmpty(appRange), SysDictionary::getAppRange, appRange);
        dictWrapper.eq(SysDictionary::getDictKey, dictKey);
        dictWrapper.eq(SysDictionary::getStatus, 1);
        SysDictionary dictionary = dictionaryService.getOne(dictWrapper, false);

        List<SysDictionaryData> dictDataList = new ArrayList<>();
        // 字典数据关联查询
        if (dictionary != null) {
            LambdaQueryWrapper<SysDictionaryData> dictDataWrapper = new LambdaQueryWrapper<>();
            dictDataWrapper.eq(SysDictionaryData::getDictId, dictionary.getId());
            dictDataWrapper.eq(StringUtils.isNotEmpty(dictDataValue), SysDictionaryData::getDictValue, dictDataValue);
            dictDataWrapper.eq(SysDictionaryData::getStatus, 1);
            dictDataWrapper.orderByAsc(SysDictionaryData::getSort);
            dictDataList = dictionaryDataService.list(dictDataWrapper);
        }
        return dictDataList;
    }

    /**
     * 获取字典数据
     *
     * @param dictKey 字典键
     * @param appRange 字典应用范围
     * @return 字典数据
     */
    public List<SysDictionaryData> getDictData(String dictKey, String appRange) {
        return getDictData(dictKey, appRange, Strings.STR_NULL);
    }

    /**
     * 获取单一字典数据
     *
     * @param dictKey 字典键
     * @param appRange 字典应用范围
     * @param dictDataValue 字典数据值
     * @return 字典数据
     */
    public Optional<SysDictionaryData> getSingleDictData(String dictKey, String appRange, String dictDataValue) {
        List<SysDictionaryData> dictDataList = getDictData(dictKey, appRange, dictDataValue);
        if (CollectionUtils.isNotEmpty(dictDataList)) {
            SysDictionaryData dictionaryData = dictDataList.get(0);
            return Optional.of(dictionaryData);
        }
        return Optional.empty();
    }

    /**
     * 获取国际化字典数据
     *
     * @param dictDataValue 字典数据值
     * @return 对应语言的展示值
     */
    public Optional<String> getI18nDictLabel(String dictDataValue, String appRange) {
        String language = CommonUtil.getGlobalLanguage();
        String i18nDictKey = SettingsKeys.I18N_KEY_PREFIX + language;
        List<SysDictionaryData> dictDataList = getDictData(i18nDictKey, appRange, dictDataValue);
        if (CollectionUtils.isNotEmpty(dictDataList)) {
            SysDictionaryData dictionaryData = dictDataList.get(0);
            return Optional.of(dictionaryData.getDictLabel());
        }
        return Optional.empty();
    }

    /**
     * 获取系统配置的所有的字典类型
     */
    public List<SysDictionary> getAllDictionary() {
        LambdaQueryWrapper<SysDictionary> queryWrapper = new LambdaQueryWrapper<>();
        List<SysDictionary> dictionaryList = dictionaryService.list(queryWrapper);
        dictionaryList.stream().map(dictionary -> {
            String dictKey = dictionary.getDictKey();
            Optional<String> dictDataShow = getI18nDictLabel(dictKey, SettingsKeys.APP_RANGE_ADMIN);
            if (dictDataShow.isPresent()) {
                dictionary.setDictName(dictDataShow.get());
            }
            return dictionary;
        }).collect(Collectors.toList());
        return dictionaryList;
    }
}
