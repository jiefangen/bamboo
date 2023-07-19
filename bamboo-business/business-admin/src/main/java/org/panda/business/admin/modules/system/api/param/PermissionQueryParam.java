package org.panda.business.admin.modules.system.api.param;

import lombok.Getter;
import lombok.Setter;
import org.panda.tech.data.model.query.QueryParam;

/**
 * 权限分页查询参数
 *
 * @author jiefangen
 **/
@Setter
@Getter
public class PermissionQueryParam extends QueryParam {
    /**
     * 模糊筛选条件
     */
    private String keyword;
}
