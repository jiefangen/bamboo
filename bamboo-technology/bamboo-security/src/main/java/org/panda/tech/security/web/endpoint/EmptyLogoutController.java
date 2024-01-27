package org.panda.tech.security.web.endpoint;

import org.panda.tech.security.config.annotation.ConfigAnonymous;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 空的登出控制器
 */
@Controller
public class EmptyLogoutController {

    @RequestMapping("/logout")
    @ConfigAnonymous
    public void logout() {
        // 存在的意义是让框架为登出请求匹配CorsConfiguration
        // 在配置了静态资源路径样式后，默认的/**处理器会被覆盖，/logout就没有了对应的处理方法，框架无法建立匹配关系
        // 登出请求被过滤器拦截处理，本方法实际上不会被调用
    }

}
