package org.panda.tech.core.config.app.security.executor.strategy.client;

import org.panda.tech.core.config.app.security.model.AppServiceModel;
import org.panda.tech.core.web.config.WebConstants;
import org.panda.tech.core.web.restful.RestfulResult;
import org.springframework.web.bind.annotation.*;

/**
 * 认证鉴权中心
 *
 * @author fangen
 **/
//@Component
public interface AuthServerClient {

    @PostMapping("/auth/login")
    RestfulResult<String> login(@RequestParam("service") String service, @RequestParam("username") String username,
                                @RequestParam("password") String password);

    @PostMapping("/auth/login")
    RestfulResult<String> loginByCredentials(@RequestHeader(WebConstants.HEADER_SECRET_KEY) String secretKey,
                                             @RequestHeader(WebConstants.HEADER_AUTH_CREDENTIALS) String credentials,
                                             @RequestParam("service") String service);

    @GetMapping(value = "/auth/access/validate")
    RestfulResult validate(@RequestHeader(WebConstants.HEADER_AUTH_JWT) String authToken,
                           @RequestParam("service") String service);

    @PostMapping("/auth/service/authorize")
    RestfulResult authorize(@RequestBody AppServiceModel appServiceModel);

}
