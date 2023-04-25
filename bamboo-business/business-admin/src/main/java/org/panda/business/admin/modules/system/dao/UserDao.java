package org.panda.business.admin.modules.system.dao;

import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;
import org.panda.business.admin.modules.system.model.dto.UserDTO;
import org.panda.business.admin.modules.system.model.po.UserPO;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.Set;

@Repository
public interface UserDao {

    Page<UserDTO> findUsers(@Param("keyword") String keyword);

    UserPO findByUsername(@Param("username") String username);

    void insertUser(@Param("user") UserPO user);

    int updateUser(@Param("user") UserPO user);

    int deleteUser(@Param("username") String username);

    void updateUserRole(@Param("userId") BigInteger userId, @Param("roleCodes") Set<String> roleCodes);
}
