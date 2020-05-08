package org.panda.modules.system.controller;

import org.panda.modules.system.domain.User;
import org.panda.modules.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jvfagan
 * @since JDK 1.8  2020/5/5
 **/
@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/getUserByUsername")
    public User getUserByUsername(String username){
        return userService.getUserByUsername(username);
    }

}
