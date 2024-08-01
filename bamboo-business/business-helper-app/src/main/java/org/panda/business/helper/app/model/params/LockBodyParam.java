package org.panda.business.helper.app.model.params;

import lombok.Data;
import org.panda.tech.core.spec.debounce.annotation.LockKeyParam;

@Data
public class LockBodyParam {
    /**
     * 用户名
     */
    @LockKeyParam
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 应用的AppId
     */
    @LockKeyParam
    private String appid;
}
