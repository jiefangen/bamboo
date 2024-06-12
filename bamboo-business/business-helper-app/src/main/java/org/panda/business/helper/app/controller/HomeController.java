package org.panda.business.helper.app.controller;

import io.swagger.annotations.Api;
import org.panda.tech.core.web.controller.HomeControllerSupport;
import org.panda.tech.core.web.restful.RestfulResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Api(tags = "匿名服务问候语")
@Controller
public class HomeController extends HomeControllerSupport {

    public RestfulResult<String> home() {
        return super.home();
    }

    public ModelAndView index(HttpServletRequest request) {
        return super.index(request);
    }
}
