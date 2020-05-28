package org.panda.core.modules;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.panda.common.domain.ResultVO;
import org.panda.core.common.constant.SystemConstant;
import org.panda.core.common.util.TokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

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
    public ResultVO doLogin(@RequestBody JSONObject jsonObject){
        String username = jsonObject.getString("username");
        String password = jsonObject.getString("password");
        if(StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            return ResultVO.getFailure();
        }

        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username, password);
        try {
            subject.login(usernamePasswordToken);
            // 登录成功，生成用户toke返回，用于前后端交互凭证
            String token= TokenUtil.sign(username);
            JSONObject json = new JSONObject();
            json.put("token", token);
            return ResultVO.getSucess(json);
        } catch (UnknownAccountException e) {
            return ResultVO.getFailure(SystemConstant.USER_INFO_ERROR, e.getMessage());
        } catch (IncorrectCredentialsException e){
            LOGGER.warn(SystemConstant.PWD_WRONG);
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
