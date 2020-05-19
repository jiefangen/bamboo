package org.panda.core.modules;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.panda.common.domain.ResultVO;
import org.panda.core.common.constant.SystemConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

    @PostMapping("/doLogin")
    public ResultVO doLogin(String username, String password){
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        try {
            subject.login(token);
            return ResultVO.getSucess();
        } catch (UnknownAccountException e) {
            return ResultVO.getFailure(SystemConstant.USER_INFO_ERROR, e.getMessage());
        } catch (IncorrectCredentialsException e){
            LOGGER.error(SystemConstant.PWD_WRONG);
            return ResultVO.getFailure(SystemConstant.USER_INFO_ERROR,SystemConstant.PWD_WRONG);
        }

    }

    /**
     * 系统用户登出
     */
    @GetMapping("/logout")
    public void logout(){
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            subject.logout();
        }
    }
}
