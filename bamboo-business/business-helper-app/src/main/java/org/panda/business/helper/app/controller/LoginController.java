package org.panda.business.helper.app.controller;

import com.auth0.jwt.exceptions.TokenExpiredException;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.panda.bamboo.common.util.LogUtil;
import org.panda.business.helper.app.common.constant.GlobalConstants;
import org.panda.business.helper.app.common.utils.TokenUtil;
import org.panda.tech.core.spec.log.ActionType;
import org.panda.tech.core.spec.user.UsernamePassword;
import org.panda.tech.core.web.config.annotation.WebOperationLog;
import org.panda.tech.core.web.restful.RestfulResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 系统登录接口
 *
 * @author fangen
 * @since 2020/5/13
 **/
@Api(tags = "系统用户登录")
@RestController
public class LoginController {

    @PostMapping("/doLogin")
    @WebOperationLog(content = "/doLogin", actionType= ActionType.LOGIN, intoStorage = true)
    public RestfulResult<?> doLogin(@RequestBody UsernamePassword usernamePassword){
        String username = usernamePassword.getUsername();
        String password = usernamePassword.getPassword();
        if(StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            return RestfulResult.failure(GlobalConstants.PARAMETERS_INCOMPLETE);
        }

        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username, password);
        try {
            subject.login(usernamePasswordToken);
            // 登录成功，生成用户toke返回，用于前后端交互凭证
            String token= TokenUtil.sign(username);
            Map<String, String> loginRes = new HashMap<>();
            loginRes.put("name", username);
            loginRes.put("token", token);
            return RestfulResult.success(loginRes);
        } catch (UnknownAccountException e) {
            return RestfulResult.failure(GlobalConstants.USER_INFO_ERROR, e.getMessage());
        } catch (IncorrectCredentialsException e) {
            LogUtil.warn(getClass(), GlobalConstants.PWD_WRONG);
            return RestfulResult.failure(GlobalConstants.USER_INFO_ERROR, GlobalConstants.PWD_WRONG);
        } catch (AccountException e) {
            return RestfulResult.failure(GlobalConstants.USER_INFO_ERROR, e.getMessage());
        }
    }

    @GetMapping("/doLogin")
    public RestfulResult<?> doLogin(HttpServletRequest request){
        String token = request.getHeader(GlobalConstants.AUTH_HEADER);
        if (StringUtils.isNotEmpty(token)) {
            try {
                TokenUtil.verify(token);
            } catch (Exception e) {
                if (e instanceof TokenExpiredException) {
                    LogUtil.warn(getClass(), e.getMessage());
                    return RestfulResult.failure(GlobalConstants.TOKEN_EXPIRED, e.getMessage());
                }
            }
        }
        return RestfulResult.failure(GlobalConstants.LOGGED_OUT, GlobalConstants.LOGGED_OUT_REASON);
    }

    @GetMapping("/logout")
    @WebOperationLog(content = "/logout", actionType= ActionType.QUIT, intoStorage = true)
    public RestfulResult<?> logout(){
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            subject.logout();
        }
        return RestfulResult.success();
    }
}
