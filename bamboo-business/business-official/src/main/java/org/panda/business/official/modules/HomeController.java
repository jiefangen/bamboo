package org.panda.business.official.modules;

import io.swagger.annotations.Api;
import org.panda.business.official.common.constant.Authority;
import org.panda.tech.security.config.annotation.ConfigAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * opener
 *
 * @author fangen
 **/
@Api(tags = "开场白")
@RestController
public class HomeController {

    @GetMapping("/home")
    public String home() {
        return "Welcome to bamboo official system!";
    }

    @GetMapping("/unauthorizedUrl")
    @ConfigAuthority(type = Authority.TYPE_MANAGER, rank = Authority.RANK_0, permission = Authority.PER_ACTUATOR)
    public String unauthorizedUrl() {
        return "No login authentication 403";
    }

    @GetMapping("/index/{param}")
    @ConfigAuthority
    public String index(@PathVariable String param) {
        return param + " :: Spring Boot :: (v2.6.6)";
    }
}
