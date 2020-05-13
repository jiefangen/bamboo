package org.panda.modules.system.dao;

import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;
import org.panda.modules.system.domain.po.UserPO;

public interface UserDao {

    Page<UserPO> findUsers(@Param("keyword") String keyword);

    UserPO findByUsername(@Param("username") String username);
}
