package org.panda.business.helper.app.controller;

import io.swagger.annotations.Api;
import org.panda.bamboo.common.constant.basic.Strings;
import org.panda.business.helper.app.model.params.LockBodyParam;
import org.panda.tech.core.spec.debounce.annotation.LockKeyParam;
import org.panda.tech.core.spec.debounce.annotation.RequestLock;
import org.panda.tech.core.spec.log.ActionType;
import org.panda.tech.core.spec.log.annotation.WebOperationLog;
import org.panda.tech.core.web.controller.HomeControllerSupport;
import org.panda.tech.core.web.restful.RestfulResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
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

    @GetMapping("/lock/param")
    @ResponseBody
    @RequestLock(expire = 5)
    public RestfulResult<?> lockParam(@LockKeyParam @RequestParam String username,
                                      @LockKeyParam @RequestParam String password) {
        return RestfulResult.success(username + Strings.COLON + password);
    }

    @PostMapping("/lock/body")
    @ResponseBody
    @RequestLock(prefix = "lockBody", expire = 15)
    @WebOperationLog(actionType = ActionType.OTHER, intoStorage = true)
    public RestfulResult<?> lockBody(@RequestBody LockBodyParam bodyParam) {
        return RestfulResult.success(bodyParam);
    }

}
