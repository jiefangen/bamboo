package org.panda.bamboo.authorization.controller;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author jvfagan
 * @since JDK 1.8  2019/5/8
 **/
@RestController
@RequestMapping(value = "v1/oauth")
public class AuthenticationController {

    @RequestMapping(value = {"/user"}, produces = "application/json", method = RequestMethod.GET )
    public Map<String, Object> user(OAuth2Authentication user){
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("user", user.getUserAuthentication().getPrincipal());
        userInfo.put("authorities", AuthorityUtils.authorityListToSet(user.getUserAuthentication().getAuthorities()));
        return userInfo;
    }
}
