package org.panda.business.official.modules;

import io.swagger.annotations.Api;
import org.panda.tech.core.web.restful.RestfulResult;
import org.panda.tech.security.config.annotation.ConfigAnonymous;
import org.panda.tech.security.user.UsernamePassword;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 官网平台登录控制器
 *
 * @author fangen
 * @since 2023-05-27
 **/
@Api(tags = "官网平台登录控制器")
@RestController
public class LoginController {

    @PostMapping("/login")
    @ConfigAnonymous
    public RestfulResult login(@RequestBody UsernamePassword usernamePassword){
        User.withUsername(usernamePassword.getUsername())
                .password(usernamePassword.getPassword())
                .roles("ADMIN")
                .build();
        return RestfulResult.success();
    }

    @GetMapping("/logout")
    public RestfulResult logout(){
        return RestfulResult.success();
    }

}
