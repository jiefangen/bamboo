package org.panda.business.admin.modules.settings.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.panda.bamboo.common.constant.Commons;
import org.panda.bamboo.common.exception.business.BusinessException;
import org.panda.business.admin.modules.settings.api.param.DictDataParam;
import org.panda.business.admin.modules.settings.api.param.DictDataQueryParam;
import org.panda.business.admin.modules.settings.service.SysDictionaryDataService;
import org.panda.business.admin.modules.settings.service.entity.SysDictionaryData;
import org.panda.business.admin.modules.settings.service.repository.SysDictionaryDataMapper;
import org.panda.tech.data.model.query.QueryResult;
import org.panda.tech.data.mybatis.config.QueryPageHelper;
import org.panda.tech.security.user.UserSpecificDetails;
import org.panda.tech.security.util.SecurityUtil;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 系统字典数据 服务实现类
 * </p>
 *
 * @author bamboo-code-generator
 * @since 2023-07-29
 */
@Service
public class SysDictionaryDataServiceImpl extends ServiceImpl<SysDictionaryDataMapper, SysDictionaryData> implements SysDictionaryDataService {

    @Override
    public QueryResult<SysDictionaryData> getDictDataByPage(DictDataQueryParam queryParam) {
        Page<SysDictionaryData> page = new Page<>(queryParam.getPageNo(), queryParam.getPageSize());
        LambdaQueryWrapper<SysDictionaryData> queryWrapper = new LambdaQueryWrapper<>();
        // 查询必须以字典主表类型为条件
        Integer dictId = queryParam.getDictId();
        queryWrapper.eq(SysDictionaryData::getDictId, dictId);
        String dictLabel = queryParam.getDictLabel();
        queryWrapper.like(StringUtils.isNotBlank(dictLabel), SysDictionaryData::getDictLabel, dictLabel);
        Integer status = queryParam.getStatus();
        queryWrapper.eq(status != null, SysDictionaryData::getStatus, status);
        queryWrapper.orderByAsc(SysDictionaryData::getCreateTime);
        IPage<SysDictionaryData> paramPage = this.page(page, queryWrapper);
        QueryResult<SysDictionaryData> queryResult = QueryPageHelper.convertQueryResult(paramPage);
        return queryResult;
    }

    @Override
    public String addDictData(DictDataParam dictDataParam) {
        // 字典数据值重复性校验
        Integer dictId = dictDataParam.getDictId();
        if (dictId == null) {
            return "The dictionary is not exist.";
        }
        String dictValue = dictDataParam.getDictValue();
        LambdaQueryWrapper<SysDictionaryData> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysDictionaryData::getDictId, dictId);
        queryWrapper.eq(SysDictionaryData::getDictValue, dictValue);
        if (this.count(queryWrapper) > 0) {
            return "The dictValue is already taken!";
        }
        SysDictionaryData dictionaryData = new SysDictionaryData();
        dictDataParam.transform(dictionaryData);
        dictionaryData.setIsDefault("N");
        LambdaQueryWrapper<SysDictionaryData> dictQueryWrapper = new LambdaQueryWrapper<>();
        dictQueryWrapper.eq(SysDictionaryData::getDictId, dictId);
        dictionaryData.setSort(this.count(dictQueryWrapper));
        UserSpecificDetails userSpecificDetails = SecurityUtil.getAuthorizedUserDetails();
        String principalUsername = userSpecificDetails.getUsername();
        dictionaryData.setCreator(principalUsername);
        if (this.save(dictionaryData)) {
            return Commons.RESULT_SUCCESS;
        } else {
            return Commons.RESULT_FAILURE;
        }
    }

    @Override
    public boolean updateDictData(DictDataParam dictDataParam) {
        if (dictDataParam.getId() == null) {
            return false;
        }
        SysDictionaryData dictionaryData = new SysDictionaryData();
        dictDataParam.transform(dictionaryData);
        UserSpecificDetails userSpecificDetails = SecurityUtil.getAuthorizedUserDetails();
        String principalUsername = userSpecificDetails.getUsername();
        dictionaryData.setUpdater(principalUsername);
        return this.updateById(dictionaryData);
    }

    @Override
    public boolean deleteDictData(Integer id) throws BusinessException {
        if (id != null) {
            SysDictionaryData dictionaryData = this.getById(id);
            if (dictionaryData != null && dictionaryData.getStatus() == 0) { // 停用的状态才可以删除
                if ("systemInit".equals(dictionaryData.getCreator())) { // 系统初始化的重要参数不可删除
                    throw new BusinessException("System initialization dictionary data cannot be deleted.");
                }
                return this.removeById(id);
            } else {
                throw new BusinessException("Please close the dictionary data first.");
            }
        }
        return false;
    }
}
