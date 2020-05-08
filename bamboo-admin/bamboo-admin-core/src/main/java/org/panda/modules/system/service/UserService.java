package org.panda.modules.system.service;

import org.panda.modules.system.domain.User;

/**
 * @author jvfagan
 * @since JDK 1.8  2020/5/5
 **/
public interface UserService {

    User getUserByUsername(String username);
}
