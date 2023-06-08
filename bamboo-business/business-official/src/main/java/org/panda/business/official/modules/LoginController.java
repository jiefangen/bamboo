package org.panda.business.official.modules;

import io.swagger.annotations.Api;
import org.panda.tech.core.config.app.AppConstants;
import org.panda.tech.core.web.jwt.InternalJwtResolver;
import org.panda.tech.core.web.restful.RestfulResult;
import org.panda.tech.security.config.annotation.ConfigAnonymous;
import org.panda.tech.security.user.DefaultUserSpecificDetails;
import org.panda.tech.security.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 官网平台登录控制器
 *
 * @author fangen
 * @since 2023-05-27
 **/
@Api(tags = "官网平台登录控制器")
@RestController
public class LoginController {

    @Autowired
    private InternalJwtResolver jwtResolver;

    @Value(AppConstants.EL_SPRING_APP_NAME)
    private String appName;

    @GetMapping("/")
    @ConfigAnonymous
    public RestfulResult login(){
        // 安全认证登录成功后的业务处理
        if (SecurityUtil.isAuthorized()) {
            DefaultUserSpecificDetails userSpecificDetails = SecurityUtil.getAuthorizedUserDetails();
            // 登录成功，生成用户toke返回，用于前后端交互凭证
            String token = jwtResolver.generate(appName, userSpecificDetails);
            Map<String, String> userMap = new HashMap<>(3);
            userMap.put("username", userSpecificDetails.getUsername());
            userMap.put("token", token);
            return RestfulResult.success(userMap);
        }
        return RestfulResult.failure();
    }

    @PostMapping("/login")
    public RestfulResult doLogin(@RequestParam String username, @RequestParam String password) {
        // 只做安全认证登录引导，无需处理任何逻辑
        return RestfulResult.success(username + password);
    }

    @GetMapping("/logout")
    public RestfulResult logout() {
        return RestfulResult.success();
    }

}
