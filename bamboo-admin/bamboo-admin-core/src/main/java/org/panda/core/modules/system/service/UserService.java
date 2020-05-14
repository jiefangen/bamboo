package org.panda.core.modules.system.service;

import com.github.pagehelper.Page;
import org.panda.core.modules.system.domain.dto.UserDTO;
import org.panda.core.modules.system.domain.po.UserPO;

public interface UserService {

    Page<UserPO> getUsers(String keyword);

    UserDTO getUserAndRoles(String username);

    String addUser(UserPO user);
}
