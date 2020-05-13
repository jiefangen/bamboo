package org.panda.modules.system.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.panda.modules.system.domain.po.UserPO;
import org.panda.modules.system.domain.param.UserQueryParam;
import org.panda.modules.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户管理
 *
 * @author jvfagan
 * @since JDK 1.8  2020/5/5
 **/
@RestController
@RequestMapping("/system/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/doLogin")
    public String doLogin(@RequestParam("username") String username, @RequestParam("password") String password){
        return baseLogin(username, password);
    }

    @PostMapping("/login")
    public String login(String username, String password){
       return baseLogin(username, password);
    }

    private String baseLogin(String username, String password){
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        String message = "Login Successful!";
        try {
            subject.login(token);
        } catch (UnknownAccountException e) {
            token.clear();
            message = e.getMessage();
        } catch (IncorrectCredentialsException e){
            token.clear();
            message = "Wrong password,Login failed!";
        }
        return message;
    }

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
        PageHelper.startPage(param.getPageNo(),param.getPageSize());
        Page<UserPO> users = userService.getUsers(param.getKeyword());
        PageInfo<UserPO> pageInfo = new PageInfo<>(users);
        return pageInfo;
    }

}
