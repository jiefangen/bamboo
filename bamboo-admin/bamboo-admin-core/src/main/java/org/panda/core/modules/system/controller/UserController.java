package org.panda.core.modules.system.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.panda.core.common.util.EncrypterUtil;
import org.panda.core.modules.system.domain.param.UserQueryParam;
import org.panda.core.modules.system.domain.po.UserPO;
import org.panda.core.modules.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户管理
 *
 * @author jiefangen
 * @since JDK 1.8  2020/5/5
 **/
@RestController
@RequestMapping("/system/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/list")
    @RequiresRoles(value={"SYSTEM","ADMIN"},logical= Logical.OR)
    public PageInfo<UserPO> list(@RequestParam(defaultValue = "1") int pageNo, @RequestParam(defaultValue = "10") int pageSize){
        PageHelper.startPage(pageNo,pageSize);
        Page<UserPO> users = userService.getUsers("");
        PageInfo<UserPO> pageInfo = new PageInfo<>(users);
        return pageInfo;
    }

    @PostMapping("/page")
//    @RequiresPermissions(value = "permis[get]")
    public PageInfo<UserPO> page(@RequestBody UserQueryParam param){
        param.initPage();
        Page<UserPO> users = userService.getUsers(param.getKeyword());
        PageInfo<UserPO> pageInfo = new PageInfo<>(users);
        return pageInfo;
    }

    @PostMapping("/add")
    public String add(@RequestBody UserPO user){
        if (user == null) {
            return "";
        }
        // TODO 用户名与密码非空校验

        EncrypterUtil encrypterUtil = new EncrypterUtil();
        encrypterUtil.encrypterPwd(user);
        String msg = userService.addUser(user);
        return msg;
    }

}
