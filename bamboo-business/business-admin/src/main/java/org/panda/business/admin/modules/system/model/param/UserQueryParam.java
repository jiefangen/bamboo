package org.panda.business.admin.modules.system.model.param;

import lombok.Getter;
import lombok.Setter;
import org.panda.business.admin.common.domain.QueryParam;

/**
 * 用户分页查询参数
 *
 * @author jiefangen
 * @since JDK 1.8  2020/5/10
 **/
@Setter
@Getter
public class UserQueryParam extends QueryParam {
    private String keyword;
}
