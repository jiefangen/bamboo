package org.panda.modules.system.dao;

import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;
import org.panda.modules.system.domain.dto.UserDTO;
import org.panda.modules.system.domain.po.UserPO;

import java.math.BigInteger;
import java.util.Set;

public interface UserDao {

    Page<UserDTO> findUsers(@Param("keyword") String keyword);

    UserPO findByUsername(@Param("username") String username);

    void insertUser(@Param("user") UserPO user);

    int updateUser(@Param("user") UserPO user);

    int deleteUser(@Param("username") String username);

    void updateUserRole(@Param("userId") BigInteger userId, @Param("roleCodes") Set<String> roleCodes);
}
