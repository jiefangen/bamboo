package org.panda.business.admin.v1.modules.monitor.api.param;

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
public class LogQueryParam extends QueryParam {
    /**
     * 模糊筛选条件
     */
    private String keyword;
}
