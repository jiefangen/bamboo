package org.panda.business.admin.modules.system.api.param;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * 更新用户角色参数
 *
 * @author fangen
 **/
@Setter
@Getter
public class UpdateUserRoleParam {
    /**
     * 需要更新的用户ID
     */
    @NotNull
    private Integer id;
    /**
     * 用户的角色集
     */
    private Set<String> roleCodes;
}
