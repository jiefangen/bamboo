package org.panda.modules.system.service;

import com.github.pagehelper.Page;
import org.panda.modules.system.domain.dto.UserDTO;
import org.panda.modules.system.domain.po.UserPO;

/**
 * @author jvfagan
 * @since JDK 1.8  2020/5/5
 **/
public interface UserService {

    Page<UserPO> getUsers(String keyword);

    UserDTO getUserAndRoles(String username);
}
