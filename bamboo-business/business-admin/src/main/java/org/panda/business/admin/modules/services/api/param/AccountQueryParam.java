package org.panda.business.admin.modules.services.api.param;

import lombok.Getter;
import lombok.Setter;
import org.panda.tech.data.model.query.QueryParam;

/**
 * 账户分页查询参数
 **/
@Setter
@Getter
public class AccountQueryParam extends QueryParam {
    private static final long serialVersionUID = 6250496610119264454L;
    /**
     * 模糊筛选条件
     */
    private String keyword;
    /**
     * 账户名
     */
    private String username;
    /**
     * 商户号
     */
    private String merchantNum;
}
