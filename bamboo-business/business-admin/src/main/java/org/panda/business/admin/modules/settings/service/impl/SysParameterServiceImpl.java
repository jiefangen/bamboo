package org.panda.business.admin.modules.settings.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.panda.bamboo.common.constant.Commons;
import org.panda.bamboo.common.exception.business.BusinessException;
import org.panda.business.admin.application.resolver.MessageSourceResolver;
import org.panda.business.admin.modules.settings.api.param.ParameterParam;
import org.panda.business.admin.modules.settings.api.param.ParameterQueryParam;
import org.panda.business.admin.modules.settings.service.SysParameterService;
import org.panda.business.admin.modules.settings.service.entity.SysParameter;
import org.panda.business.admin.modules.settings.service.repository.SysParameterMapper;
import org.panda.tech.data.model.query.QueryResult;
import org.panda.tech.data.mybatis.util.QueryPageHelper;
import org.panda.tech.security.user.UserSpecificDetails;
import org.panda.tech.security.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 系统参数 服务实现类
 * </p>
 *
 * @author bamboo-code-generator
 * @since 2023-07-26
 */
@Service
@Transactional
public class SysParameterServiceImpl extends ServiceImpl<SysParameterMapper, SysParameter> implements SysParameterService {

    @Autowired
    private MessageSourceResolver messageSourceResolver;

    @Override
    public QueryResult<SysParameter> getParamByPage(ParameterQueryParam queryParam) {
        Page<SysParameter> page = new Page<>(queryParam.getPageNo(), queryParam.getPageSize());
        LambdaQueryWrapper<SysParameter> queryWrapper = new LambdaQueryWrapper<>();
        String paramName = queryParam.getParamName();
        queryWrapper.like(StringUtils.isNotBlank(paramName), SysParameter::getParamName, paramName);
        String paramKey = queryParam.getParamKey();
        queryWrapper.like(StringUtils.isNotBlank(paramKey), SysParameter::getParamKey, paramKey);
        if (StringUtils.isNotBlank(queryParam.getStartDate()) && StringUtils.isNotBlank(queryParam.getEndDate())) {
            queryWrapper.between(SysParameter::getCreateTime, queryParam.getStartDate(), queryParam.getEndDate());
        }
        queryWrapper.orderByAsc(SysParameter::getCreateTime);
        IPage<SysParameter> paramPage = this.page(page, queryWrapper);
        QueryResult<SysParameter> queryResult = QueryPageHelper.convertQueryResult(paramPage);
        return queryResult;
    }

    @Override
    public String addParameter(ParameterParam parameterParam) {
        // 参数key重复性校验
        String parameterKey = parameterParam.getParamKey();
        LambdaQueryWrapper<SysParameter> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysParameter::getParamKey, parameterKey);
        if (this.count(queryWrapper) > 0) {
            return messageSourceResolver.findI18nMessage("admin.settings.parameter.error_add");
        }
        SysParameter parameter = new SysParameter();
        parameterParam.transform(parameter);
        UserSpecificDetails userSpecificDetails = SecurityUtil.getAuthorizedUserDetails();
        String principalUsername = userSpecificDetails.getUsername();
        parameter.setCreator(principalUsername);
        if (this.save(parameter)) {
            return Commons.RESULT_SUCCESS;
        } else {
            return Commons.RESULT_FAILURE;
        }
    }

    @Override
    public boolean updateParameter(ParameterParam parameterParam) {
        if (parameterParam.getId() == null) {
            return false;
        }
        SysParameter parameter = new SysParameter();
        parameterParam.transform(parameter);
        UserSpecificDetails userSpecificDetails = SecurityUtil.getAuthorizedUserDetails();
        String principalUsername = userSpecificDetails.getUsername();
        parameter.setUpdater(principalUsername);
        return this.updateById(parameter);
    }

    @Override
    public boolean deleteParameter(Integer id) throws BusinessException {
        if (id != null) {
            SysParameter parameter = this.getById(id);
            if (parameter != null && parameter.getStatus() == 0) { // 关闭的状态才可以删除
                if ("systemInit".equals(parameter.getCreator())) { // 系统初始化的重要参数不可删除
                    String errorMessage = messageSourceResolver.findI18nMessage("admin.settings.parameter.error_del");
                    throw new BusinessException(errorMessage);
                }
                return this.removeById(id);
            } else {
                String errorMessage = messageSourceResolver.findI18nMessage("admin.settings.parameter.error_del.1");
                throw new BusinessException(errorMessage);
            }
        }
        return false;
    }

}
