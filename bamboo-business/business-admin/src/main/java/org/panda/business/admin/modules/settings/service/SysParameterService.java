package org.panda.business.admin.modules.settings.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.panda.business.admin.modules.settings.api.param.ParameterQueryParam;
import org.panda.business.admin.modules.settings.service.entity.SysParameter;
import org.panda.tech.data.model.query.QueryResult;

import java.util.Optional;

/**
 * <p>
 * 系统参数 服务类
 * </p>
 *
 * @author bamboo-code-generator
 * @since 2023-07-26
 */
public interface SysParameterService extends IService<SysParameter> {

    Optional<String> getParamValueByKey(String paramKey);

    Optional<String> getParamValueByKey(String paramKey, String appRange);

    QueryResult<SysParameter> getParamByPage(ParameterQueryParam queryParam);

}
