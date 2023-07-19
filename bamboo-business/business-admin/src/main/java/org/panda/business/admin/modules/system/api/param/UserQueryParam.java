package org.panda.business.admin.modules.system.api.param;

import lombok.Getter;
import lombok.Setter;
import org.panda.tech.data.model.query.QueryParam;

/**
 * 用户分页查询参数
 *
 * @author jiefangen
 **/
@Setter
@Getter
public class UserQueryParam extends QueryParam {
    /**
     * 模糊筛选条件
     */
    private String keyword;
}
