package org.panda.core.modules;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.panda.common.domain.ResultVO;
import org.panda.core.common.constant.SystemConstant;
import org.springframework.web.bind.annotation.GetMapping;
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
    public ResultVO login(String username, String password){
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        try {
            subject.login(token);
            return ResultVO.getSucess();
        } catch (UnknownAccountException e) {
            return ResultVO.getFailure(SystemConstant.USER_INFO_ERROR, e.getMessage());
        } catch (IncorrectCredentialsException e){
            return ResultVO.getFailure(SystemConstant.USER_INFO_ERROR,SystemConstant.PWD_WRONG);
        }

    }

    /**
     * 系统用户登出
     */
    @GetMapping("/logout")
    public ResultVO logout(){
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            subject.logout();
        }
        return ResultVO.getSucess();
    }
}
