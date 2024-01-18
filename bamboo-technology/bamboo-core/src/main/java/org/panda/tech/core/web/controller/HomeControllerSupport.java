package org.panda.tech.core.web.controller;

import org.panda.bamboo.common.constant.basic.Strings;
import org.panda.bamboo.common.util.lang.StringUtil;
import org.panda.tech.core.config.app.AppConstants;
import org.panda.tech.core.web.restful.RestfulResult;
import org.panda.tech.core.web.util.NetUtil;
import org.panda.tech.core.web.util.WebHttpUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * 服务端通用Home业务控制器支持
 */
@RequestMapping(value = "/home")
public abstract class HomeControllerSupport {

    @Value(AppConstants.EL_SPRING_APP_NAME)
    private String name;
    @Value(AppConstants.EL_SPRING_PROFILES_ACTIVE)
    private String env;
    @Value(AppConstants.EL_SERVER_PORT)
    private String port;

    @GetMapping
    @ResponseBody
    public RestfulResult<String> home() {
        return RestfulResult.success(getApplicationDesc());
    }

    @GetMapping(value = "/index")
    public ModelAndView index(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView(getIndexViewName());
        modelAndView.addObject("appName", name);
        modelAndView.addObject("env", env);
        modelAndView.addObject("port", port);
        modelAndView.addObject("appDesc", getApplicationDesc());
        modelAndView.addObject("localHost", NetUtil.getLocalHost());
        modelAndView.addObject("remoteAddress", WebHttpUtil.getRemoteAddress(request));
        return modelAndView;
    }

    protected String getApplicationDesc() {
        if (name.contains(Strings.MINUS)) {
            String[] names = name.split(Strings.MINUS);
            return StringUtil.firstToUpperCase(names[0]) + Strings.SPACE + StringUtil.firstToUpperCase(names[1]);
        }
        return StringUtil.firstToUpperCase(name);
    }

    protected String getIndexViewName() {
        return "index";
    }

}
