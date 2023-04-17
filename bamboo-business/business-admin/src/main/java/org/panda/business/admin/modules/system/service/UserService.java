package org.panda.business.admin.modules.system.service;

import com.github.pagehelper.Page;
import org.panda.business.admin.common.exception.SystemException;
import org.panda.business.admin.modules.system.domain.dto.UserDTO;
import org.panda.business.admin.modules.system.domain.po.UserPO;

public interface UserService {

    Page<UserDTO> getUsers(String keyword);

    UserPO getUserInfo(String username);

    UserDTO getUserAndRoles(String username);

    String addUser(UserPO user);

    int updateUser(UserPO user);

    int deleteUser(String username) throws SystemException;

    boolean checkRoleUpdatedPass();

    void updateUserRole(UserDTO userDTO);
}
