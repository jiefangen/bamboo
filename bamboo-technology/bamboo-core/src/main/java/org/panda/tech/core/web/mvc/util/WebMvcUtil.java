package org.panda.tech.core.web.mvc.util;

import org.apache.commons.lang3.StringUtils;
import org.panda.bamboo.common.constant.basic.Strings;
import org.panda.bamboo.common.util.SpringUtil;
import org.panda.bamboo.common.util.lang.ArrayUtil;
import org.panda.tech.core.web.config.WebConstants;
import org.panda.tech.core.web.context.SpringWebContext;
import org.panda.tech.core.web.util.NetUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

/**
 * WebMvc工具类
 */
public class WebMvcUtil {

    private WebMvcUtil() {
    }

    /**
     * 获取web项目应用范围内的ApplicationContext实例
     *
     * @param request HTTP请求
     * @return ApplicationContext实例
     */
    public static ApplicationContext getApplicationContext(HttpServletRequest request) {
        try {
            return RequestContextUtils.findWebApplicationContext(request);
        } catch (IllegalStateException e) {
            return null;
        }
    }

    /**
     * 获取web项目应用范围内的ApplicationContext实例
     *
     * @return ApplicationContext实例
     */
    public static ApplicationContext getApplicationContext() {
        return getApplicationContext(SpringWebContext.getRequest());
    }

    /**
     * 先尝试从Spring的LocaleResolver中获取区域，以便以自定义的方式获取区域
     *
     * @param request 请求
     * @return 区域
     */
    public static Locale getLocale(HttpServletRequest request) {
        LocaleResolver localeResolver = SpringUtil
                .getFirstBeanByClass(getApplicationContext(request), LocaleResolver.class);
        if (localeResolver != null) {
            return localeResolver.resolveLocale(request);
        } else {
            return request.getLocale();
        }
    }

    public static boolean isResponseBody(HandlerMethod handlerMethod) {
        if (handlerMethod == null) {
            return false;
        }
        return handlerMethod.getReturnType().getParameterType() != ModelAndView.class
                && (handlerMethod.getMethodAnnotation(ResponseBody.class) != null
                || handlerMethod.getBeanType().getAnnotation(RestController.class) != null);
    }

    public static String getRequestMappingUrl(HandlerMethod handlerMethod) {
        String url = getPath(handlerMethod.getBeanType().getAnnotation(RequestMapping.class));
        if (StringUtils.isBlank(url)) {
            url = Strings.EMPTY;
        }
        String path = getPath(handlerMethod.getMethodAnnotation(RequestMapping.class));
        if (StringUtils.isNotBlank(path)) {
            url += path;
        }
        return NetUtil.standardizeUrl(url);
    }

    private static String getPath(RequestMapping requestMapping) {
        return requestMapping == null ? null : ArrayUtil.get(requestMapping.value(), 0);
    }

    public static boolean isInternalReq(HttpServletRequest request) {
        String internalJwt = request.getHeader(WebConstants.HEADER_AUTH_JWT);
        if (internalJwt != null) {
            return true;
        }
        String userAgent = request.getHeader(HttpHeaders.USER_AGENT);
        return userAgent == null || userAgent.toLowerCase().startsWith("java");
    }

}
