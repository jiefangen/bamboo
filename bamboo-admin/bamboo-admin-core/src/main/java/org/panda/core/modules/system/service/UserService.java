package org.panda.core.modules.system.service;

import com.github.pagehelper.Page;
import org.panda.core.modules.system.domain.dto.UserDTO;
import org.panda.core.modules.system.domain.po.UserPO;

public interface UserService {

    Page<UserPO> getUsers(String keyword);

    UserPO getUserInfo(String username);

    UserDTO getUserAndRoles(String username);

    boolean addUser(UserPO user);

    int updateUser(UserPO user);

    int deleteUser(String id);
}
