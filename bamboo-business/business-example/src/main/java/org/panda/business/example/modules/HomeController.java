package org.panda.business.example.modules;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import org.panda.tech.core.spec.log.util.DynamicLogUtil;
import org.panda.tech.core.web.context.SpringWebContext;
import org.panda.tech.core.web.controller.HomeControllerSupport;
import org.panda.bamboo.common.model.restful.RestfulResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Api(tags = "匿名服务问候语")
@Controller
public class HomeController extends HomeControllerSupport {

    @GetMapping(value = "/application")
    @ResponseBody
    public RestfulResult<Map<String, Object>> application() {
        HttpServletRequest request = SpringWebContext.getRequest();
        DynamicLogUtil.writerContent(getClass(), "application request start...");
        RestfulResult<Map<String, Object>> result = RestfulResult.success(getApplicationMap(request));
        DynamicLogUtil.writerContent(getClass(), "application info: {}", JSONObject.toJSONString(result, true));
        return result;
    }
}
