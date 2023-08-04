package org.panda.business.admin.modules.settings.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.panda.bamboo.common.exception.business.BusinessException;
import org.panda.business.admin.modules.settings.api.param.ParameterParam;
import org.panda.business.admin.modules.settings.api.param.ParameterQueryParam;
import org.panda.business.admin.modules.settings.service.entity.SysParameter;
import org.panda.tech.data.model.query.QueryResult;

/**
 * <p>
 * 系统参数 服务类
 * </p>
 *
 * @author bamboo-code-generator
 * @since 2023-07-26
 */
public interface SysParameterService extends IService<SysParameter> {

    QueryResult<SysParameter> getParamByPage(ParameterQueryParam queryParam);

    String addParameter(ParameterParam parameterParam);

    boolean updateParameter(ParameterParam parameterParam);

    boolean deleteParameter(Integer id) throws BusinessException;
}
