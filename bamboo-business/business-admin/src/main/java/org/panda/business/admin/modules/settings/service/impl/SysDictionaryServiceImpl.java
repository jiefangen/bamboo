package org.panda.business.admin.modules.settings.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.panda.bamboo.common.constant.Commons;
import org.panda.bamboo.common.exception.business.BusinessException;
import org.panda.business.admin.application.resolver.MessageSourceResolver;
import org.panda.business.admin.modules.settings.api.param.DictionaryParam;
import org.panda.business.admin.modules.settings.api.param.DictionaryQueryParam;
import org.panda.business.admin.modules.settings.service.SysDictionaryDataService;
import org.panda.business.admin.modules.settings.service.SysDictionaryService;
import org.panda.business.admin.modules.settings.service.entity.SysDictionary;
import org.panda.business.admin.modules.settings.service.entity.SysDictionaryData;
import org.panda.business.admin.modules.settings.service.repository.SysDictionaryMapper;
import org.panda.tech.data.model.query.QueryResult;
import org.panda.tech.data.mybatis.util.QueryPageHelper;
import org.panda.tech.security.user.UserSpecificDetails;
import org.panda.tech.security.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 系统字典 服务实现类
 * </p>
 *
 * @author bamboo-code-generator
 * @since 2023-07-29
 */
@Service
@Transactional
public class SysDictionaryServiceImpl extends ServiceImpl<SysDictionaryMapper, SysDictionary> implements SysDictionaryService {

    @Autowired
    private SysDictionaryDataService dictionaryDataService;

    @Autowired
    private MessageSourceResolver messageSourceResolver;

    @Override
    public QueryResult<SysDictionary> getDictByPage(DictionaryQueryParam queryParam) {
        Page<SysDictionary> page = new Page<>(queryParam.getPageNo(), queryParam.getPageSize());
        LambdaQueryWrapper<SysDictionary> queryWrapper = new LambdaQueryWrapper<>();
        String dictName = queryParam.getDictName();
        queryWrapper.like(StringUtils.isNotBlank(dictName), SysDictionary::getDictName, dictName);
        String dictKey = queryParam.getDictKey();
        queryWrapper.like(StringUtils.isNotBlank(dictKey), SysDictionary::getDictKey, dictKey);
        Integer status = queryParam.getStatus();
        queryWrapper.eq(status != null, SysDictionary::getStatus, status);
        if (StringUtils.isNotBlank(queryParam.getStartDate()) && StringUtils.isNotBlank(queryParam.getEndDate())) {
            queryWrapper.between(SysDictionary::getCreateTime, queryParam.getStartDate(), queryParam.getEndDate());
        }
        queryWrapper.orderByAsc(SysDictionary::getCreateTime);
        IPage<SysDictionary> paramPage = this.page(page, queryWrapper);
        QueryResult<SysDictionary> queryResult = QueryPageHelper.convertQueryResult(paramPage);
        return queryResult;
    }

    @Override
    public String addDictionary(DictionaryParam dictionaryParam) {
        // 参数key重复性校验
        String dictKey = dictionaryParam.getDictKey();
        LambdaQueryWrapper<SysDictionary> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysDictionary::getDictKey, dictKey);
        if (this.count(queryWrapper) > 0) {
            return messageSourceResolver.findI18nMessage("admin.settings.dictionary.error_add");
        }
        SysDictionary dictionary = new SysDictionary();
        dictionaryParam.transform(dictionary);
        UserSpecificDetails userSpecificDetails = SecurityUtil.getAuthorizedUserDetails();
        String principalUsername = userSpecificDetails.getUsername();
        dictionary.setCreator(principalUsername);
        if (this.save(dictionary)) {
            return Commons.RESULT_SUCCESS;
        } else {
            return Commons.RESULT_FAILURE;
        }
    }

    @Override
    public boolean updateDictionary(DictionaryParam dictionaryParam) {
        if (dictionaryParam.getId() == null) {
            return false;
        }
        SysDictionary dictionary = new SysDictionary();
        dictionaryParam.transform(dictionary);
        UserSpecificDetails userSpecificDetails = SecurityUtil.getAuthorizedUserDetails();
        String principalUsername = userSpecificDetails.getUsername();
        dictionary.setUpdater(principalUsername);
        return this.updateById(dictionary);
    }

    @Override
    public boolean deleteDictionary(Integer id) throws BusinessException {
        if (id != null) {
            SysDictionary dictionary = this.getById(id);
            if (dictionary != null && dictionary.getStatus() == 0) { // 停用的状态才可以删除
                LambdaQueryWrapper<SysDictionaryData> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(SysDictionaryData::getDictId, id);
                if (dictionaryDataService.count(queryWrapper) > 0) {
                    String errorMessage = messageSourceResolver.findI18nMessage("admin.settings.dictionary.error_del");
                    throw new BusinessException(errorMessage);
                }
                if ("systemInit".equals(dictionary.getCreator())) { // 系统初始化的重要参数不可删除
                    String errorMessage = messageSourceResolver.findI18nMessage("admin.settings.dictionary.error_del.1");
                    throw new BusinessException(errorMessage);
                }
                return this.removeById(id);
            } else {
                String errorMessage = messageSourceResolver.findI18nMessage("admin.settings.dictionary.error_del.2");
                throw new BusinessException(errorMessage);
            }
        }
        return false;
    }

}
