package org.panda.business.admin.v1.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.panda.bamboo.common.exception.business.BusinessException;
import org.panda.business.admin.v1.modules.system.api.param.AddUserParam;
import org.panda.business.admin.v1.modules.system.api.param.UpdateUserRoleParam;
import org.panda.business.admin.v1.modules.system.api.param.UserQueryParam;
import org.panda.business.admin.v1.modules.system.api.vo.UserVO;
import org.panda.business.admin.v1.modules.system.service.dto.SysUserDto;
import org.panda.business.admin.v1.modules.system.service.entity.SysUser;
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

    boolean addUser(AddUserParam userParam);

    boolean updateUser(SysUser user);

    boolean deleteUser(String username) throws BusinessException;

    void updateUserRole(UpdateUserRoleParam userRoleParam);

    String resetPassword(String username, String oldPassword, String newPassword);
}
