package org.panda.modules.system.dao;

import org.apache.ibatis.annotations.Param;
import org.panda.modules.system.domain.User;

public interface UserDao {
    /**
     * 根据用户名查询
     * @param username 用户名
     * @return 用户
     */
    User findByUsername(@Param("username") String username);
}
