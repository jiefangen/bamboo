package org.panda.service.payment.controller;

import io.swagger.annotations.Api;
import org.panda.bamboo.common.constant.basic.Strings;
import org.panda.bamboo.common.util.lang.StringUtil;
import org.panda.service.payment.core.domain.model.PaymentResult;
import org.panda.tech.core.web.restful.RestfulResult;
import org.panda.tech.core.web.util.NetUtil;
import org.panda.tech.core.web.util.WebHttpUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

@Api(tags = "微服务启动语")
@Controller
@RequestMapping(value = "/home")
public class HomeController {

    @Value("${spring.application.name}")
    private String name;
    @Value("${spring.profiles.active}")
    private String env;
    @Value("${server.port}")
    private String port;

    private String getApplicationDesc() {
        return "The " + StringUtil.firstToUpperCase(name) + " Microservice";
    }

    @GetMapping
    @ResponseBody
    public RestfulResult<String> home() {
        return RestfulResult.success(getApplicationDesc());
    }

    @GetMapping(value = "/index")
    public ModelAndView index(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("name", name);
        modelAndView.addObject("env", env);
        modelAndView.addObject("port", port);
        modelAndView.addObject("appName", getApplicationDesc());
        modelAndView.addObject("localHost", NetUtil.getLocalHost());
        modelAndView.addObject("remoteAddress", WebHttpUtil.getRemoteAddress(request));
        return modelAndView;
    }

    @GetMapping(value = "/voucher")
    public ModelAndView voucher(@RequestParam(required = false) String orderNo,
                                @RequestParam(required = false) String gatewayPaymentNo,
                                @RequestParam(required = false) BigDecimal amount) {
        ModelAndView modelAndView = new ModelAndView("voucher");
        PaymentResult result = new PaymentResult(orderNo, gatewayPaymentNo, amount, Strings.STR_NULL);
        if (result != null && result.isSuccessful()) {
            modelAndView.addObject("appName", getApplicationDesc());
            modelAndView.setStatus(HttpStatus.resolve(result.getResponseStatus()));
            modelAndView.addObject("orderNo", result.getOrderNo());
            modelAndView.addObject("gatewayPaymentNo", result.getGatewayPaymentNo());
            modelAndView.addObject("amount", result.getAmount());
        } else {
            modelAndView.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return modelAndView;
    }

}
