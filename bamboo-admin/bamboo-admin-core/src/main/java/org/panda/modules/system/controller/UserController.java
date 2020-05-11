package org.panda.modules.system.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.panda.modules.system.domain.User;
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

    @GetMapping("/list")
    public PageInfo<User> list(@RequestParam(defaultValue = "1") int pageNo, @RequestParam(defaultValue = "10") int pageSize){
        PageHelper.startPage(pageNo,pageSize);
        Page<User> users = userService.getUsers("");
        PageInfo<User> pageInfo = new PageInfo<>(users);
        return pageInfo;
    }

    @PostMapping("/page")
    public PageInfo<User> page(@RequestBody UserQueryParam param){
        PageHelper.startPage(param.getPageNo(),param.getPageSize());
        Page<User> users = userService.getUsers(param.getKeyword());
        PageInfo<User> pageInfo = new PageInfo<>(users);
        return pageInfo;
    }

}
