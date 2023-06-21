package org.panda.business.admin.v1.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.panda.business.admin.v1.modules.system.api.param.UserQueryParam;
import org.panda.business.admin.v1.modules.system.api.vo.UserVO;
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
public interface ISysUserService extends IService<SysUser> {

    QueryResult<UserVO> getUserByPage(UserQueryParam queryParam);

//    UserPO getUserInfo(String username);
//
//    UserDTO getUserAndRoles(String username);
//
//    String addUser(UserPO user);
//
//    int updateUser(UserPO user);
//
//    int deleteUser(String username) throws SystemException;
//
//    boolean checkRoleUpdatedPass();
//
//    void updateUserRole(UserDTO userDTO);
}
