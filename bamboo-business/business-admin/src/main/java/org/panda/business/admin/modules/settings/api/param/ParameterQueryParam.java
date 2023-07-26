package org.panda.business.admin.modules.settings.api.param;

import lombok.Getter;
import lombok.Setter;
import org.panda.tech.data.model.query.QueryParam;

/**
 * 操作日志分页查询参数
 *
 * @author jiefangen
 **/
@Setter
@Getter
public class ParameterQueryParam extends QueryParam {
    /**
     * 参数名称
     */
    private String parameterName;
    /**
     * 参数键名
     */
    private String parameterKey;
    /**
     * 筛选开始时间
     */
    private String startDate;
    /**
     * 筛选结束时间
     */
    private String endDate;
}
