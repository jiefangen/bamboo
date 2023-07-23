package org.panda.business.admin.modules.monitor.api.param;

import lombok.Getter;
import lombok.Setter;
import org.panda.tech.data.model.query.QueryParam;

/**
 * 在线用户分页查询参数
 *
 * @author jiefangen
 **/
@Setter
@Getter
public class OnlineQueryParam extends QueryParam {
    /**
     * 模糊筛选条件
     */
    private String keyword;
}
