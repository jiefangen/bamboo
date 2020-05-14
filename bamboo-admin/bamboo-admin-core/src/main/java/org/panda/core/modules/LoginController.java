package org.panda.core.modules;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系统登录接口
 *
 * @author jiefangen
 * @since JDK 1.8  2020/5/13
 **/
@RestController
public class LoginController {
    @PostMapping("/doLogin")
    public String login(String username, String password){
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        String message = "Login Successful!";
        try {
            subject.login(token);
        } catch (UnknownAccountException e) {
            message = e.getMessage();
        } catch (IncorrectCredentialsException e){
            message = "Wrong password,Login failed!";
        }
        return message;
    }

}
