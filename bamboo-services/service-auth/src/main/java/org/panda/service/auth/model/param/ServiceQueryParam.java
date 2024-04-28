package org.panda.service.auth.model.param;

import lombok.Getter;
import lombok.Setter;
import org.panda.tech.data.model.query.QueryParam;

/**
 * 服务分页查询参数
 **/
@Setter
@Getter
public class ServiceQueryParam extends QueryParam {
    private static final long serialVersionUID = -4222775140755784243L;
    /**
     * 模糊筛选条件
     */
    private String keyword;
}
