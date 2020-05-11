package org.panda.modules.system.service.impl;

import com.github.pagehelper.Page;
import org.panda.modules.system.dao.UserDao;
import org.panda.modules.system.domain.User;
import org.panda.modules.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author jvfagan
 * @since JDK 1.8  2020/5/5
 **/
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public Page<User> getUsers(String keyword) {

        return userDao.findUsers(keyword);
    }
}
