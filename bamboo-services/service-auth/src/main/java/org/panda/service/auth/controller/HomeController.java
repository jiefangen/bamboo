package org.panda.service.auth.controller;

import io.swagger.annotations.Api;
import org.panda.tech.core.web.controller.HomeControllerSupport;
import org.panda.tech.core.web.restful.RestfulResult;
import org.panda.tech.security.config.annotation.ConfigAnonymous;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Api(tags = "匿名服务问候语")
@Controller
public class HomeController extends HomeControllerSupport {

    @ConfigAnonymous
    public RestfulResult<String> home() {
        RestfulResult<String> result = super.home();
        return RestfulResult.success(result.getData() + " Enable");
    }

    @ConfigAnonymous
    public ModelAndView index(HttpServletRequest request) {
        return super.index(request);
    }
}
