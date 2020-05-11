package org.panda.modules.system.domain.param;

import org.panda.query.QueryParam;

/**
 * 用户分页查询参数
 *
 * @author jvfagan
 * @since JDK 1.8  2020/5/10
 **/
public class UserQueryParam extends QueryParam {
    private String keyword;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
