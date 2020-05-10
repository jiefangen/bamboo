package org.panda.modules.system.dao;

import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;
import org.panda.modules.system.domain.User;

public interface UserDao {

    Page<User> findUsers(@Param("keyword") String keyword);
}
