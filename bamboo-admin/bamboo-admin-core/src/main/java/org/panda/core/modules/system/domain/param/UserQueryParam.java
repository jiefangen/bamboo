package org.panda.core.modules.system.domain.param;

import lombok.Getter;
import lombok.Setter;
import org.panda.common.query.QueryParam;

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
