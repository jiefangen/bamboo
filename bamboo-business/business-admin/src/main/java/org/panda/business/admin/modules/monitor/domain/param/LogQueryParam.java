package org.panda.business.admin.modules.monitor.domain.param;

import lombok.Getter;
import lombok.Setter;
import org.panda.business.admin.common.domain.QueryParam;

/**
 * 日志分页查询参数
 *
 * @author jiefangen
 * @since JDK 11  2022/5/7
 **/
@Setter
@Getter
public class LogQueryParam extends QueryParam {
    private String keyword;
}
