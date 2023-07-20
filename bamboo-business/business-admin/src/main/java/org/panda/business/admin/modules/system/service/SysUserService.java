package org.panda.business.admin.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.panda.bamboo.common.exception.business.BusinessException;
import org.panda.business.admin.modules.system.api.param.*;
import org.panda.business.admin.modules.system.api.vo.UserVO;
import org.panda.business.admin.modules.system.service.dto.SysUserDto;
import org.panda.business.admin.modules.system.service.entity.SysUser;
import org.panda.tech.data.model.query.QueryResult;

/**
 * <p>
 * 系统用户 服务类
 * </p>
 *
 * @author bamboo-code-generator
 * @since 2023-06-20
 */
public interface SysUserService extends IService<SysUser> {

    SysUserDto getUserAndRoles(String username);

    QueryResult<UserVO> getUserByPage(UserQueryParam queryParam);

    UserVO getUserByToken(String token);

    SysUser getUserInfo(String username);

    String addUser(AddUserParam userParam);

    boolean updateUser(SysUser user);

    boolean updateAccount(UpdateAccountParam updateAccountParam);

    boolean deleteUser(String username) throws BusinessException;

    void updateUserRole(UpdateUserRoleParam userRoleParam);

    String resetPassword(ResetPassParam resetPassParam);

    String updatePassword(UpdatePassParam updatePassParam);
}
