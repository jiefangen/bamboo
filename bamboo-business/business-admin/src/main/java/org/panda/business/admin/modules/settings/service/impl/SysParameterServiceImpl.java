package org.panda.business.admin.modules.settings.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.panda.bamboo.common.constant.basic.Strings;
import org.panda.business.admin.modules.settings.api.param.ParameterQueryParam;
import org.panda.business.admin.modules.settings.service.SysParameterService;
import org.panda.business.admin.modules.settings.service.entity.SysParameter;
import org.panda.business.admin.modules.settings.service.repository.SysParameterMapper;
import org.panda.tech.data.model.query.QueryResult;
import org.panda.tech.data.mybatis.config.QueryPageHelper;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * <p>
 * 系统参数 服务实现类
 * </p>
 *
 * @author bamboo-code-generator
 * @since 2023-07-26
 */
@Service
public class SysParameterServiceImpl extends ServiceImpl<SysParameterMapper, SysParameter> implements SysParameterService {

    @Override
    public Optional<String> getParamValueByKey(String paramKey) {
        return getParamValue(paramKey, Strings.EMPTY_OBJ);
    }

    @Override
    public Optional<String> getParamValueByKey(String paramKey, String appRange) {
        return getParamValue(paramKey, appRange);
    }

    @Override
    public QueryResult<SysParameter> getParamByPage(ParameterQueryParam queryParam) {
        Page<SysParameter> page = new Page<>(queryParam.getPageNo(), queryParam.getPageSize());
        LambdaQueryWrapper<SysParameter> queryWrapper = new LambdaQueryWrapper<>();
        String parameterName = queryParam.getParameterName();
        queryWrapper.like(StringUtils.isNotBlank(parameterName), SysParameter::getParameterName, parameterName);
        String parameterKey = queryParam.getParameterKey();
        queryWrapper.like(StringUtils.isNotBlank(parameterKey), SysParameter::getParameterKey, parameterKey);
        if (StringUtils.isNotBlank(queryParam.getStartDate()) && StringUtils.isNotBlank(queryParam.getEndDate())) {
            queryWrapper.between(SysParameter::getCreateTime, queryParam.getStartDate(), queryParam.getEndDate());
        }
        queryWrapper.orderByAsc(SysParameter::getCreateTime);
        IPage<SysParameter> paramPage = this.page(page, queryWrapper);
        QueryResult<SysParameter> queryResult = QueryPageHelper.convertQueryResult(paramPage);
        return queryResult;
    }

    private Optional<String> getParamValue(String paramKey, String appRange) {
        LambdaQueryWrapper<SysParameter> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysParameter::getParameterKey, paramKey);
        if (StringUtils.isNotEmpty(appRange)) {
            queryWrapper.eq(SysParameter::getAppRange, appRange);
        }
        queryWrapper.eq(SysParameter::getStatus, 1);
        SysParameter parameter = this.getOne(queryWrapper, false);
        if (parameter != null) {
            return Optional.ofNullable(parameter.getParameterValue());
        }
        return Optional.empty();
    }

}
