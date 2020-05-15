package org.panda.core.modules.system.dao;

import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;
import org.panda.core.modules.system.domain.po.UserPO;

public interface UserDao {

    Page<UserPO> findUsers(@Param("keyword") String keyword);

    UserPO findByUsername(@Param("username") String username);

    void insertUser(@Param("user") UserPO user);

    int updateUser(@Param("user") UserPO user);
}
