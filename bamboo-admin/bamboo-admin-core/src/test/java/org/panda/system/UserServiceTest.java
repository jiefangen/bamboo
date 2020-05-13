package org.panda.system;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.panda.modules.system.dao.UserDao;
import org.panda.modules.system.domain.dto.UserDTO;
import org.panda.modules.system.domain.po.RolePO;
import org.panda.modules.system.domain.po.UserPO;
import org.panda.modules.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Set;

/**
 * 用户管理测试类
 *
 * @author jvfagan
 * @since JDK 1.8  2020/5/11
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @Test
    public void test(){
        UserDTO user = userService.getUserAndRoles("jiefangen");
        List<RolePO> roles = user.getRoles();
        Assert.assertTrue(roles != null);
    }
}
