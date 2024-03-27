package org.panda.tech.core.web.mvc.view.util;

import org.apache.commons.lang3.StringUtils;
import org.panda.bamboo.common.constant.basic.Strings;
import org.panda.tech.core.web.util.NetUtil;
import org.panda.tech.core.web.util.WebHttpUtil;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Web视图层工具类
 */
public class WebViewUtil {

    private WebViewUtil() {
    }

    public static void forward(ServletRequest request, ServletResponse response, String url)
            throws ServletException, IOException {
        request.getRequestDispatcher(url).forward(request, response);
    }

    /**
     * 直接重定向至指定URL。请求将被重置，POST请求参数将丢失，浏览器地址栏显示的URL将更改为指定URL。 URL如果为绝对路径，则必须以http://或https://开头
     *
     * @param request  请求
     * @param response 响应
     * @param url      URL
     * @throws IOException 如果重定向时出现IO错误
     */
    public static void redirect(HttpServletRequest request, HttpServletResponse response, String url)
            throws IOException {
        String location = url;
        if (!NetUtil.isHttpUrl(location, true)) {
            if (!location.startsWith(Strings.SLASH)) {
                location = Strings.SLASH + location;
            }
            String webRoot = request.getContextPath();
            if (!location.startsWith(webRoot)) {
                location = webRoot + location;
            }
        }
        response.sendRedirect(location);
    }

    /**
     * 获取相对于web项目的前一个请求的URL
     *
     * @param request             请求
     * @param containsQueryString 是否需要包含请求参数
     * @return 前一个请求的URL
     */
    public static String getRelativePreviousUrl(HttpServletRequest request, boolean containsQueryString) {
        String referrer = request.getHeader("Referer");
        if (StringUtils.isNotBlank(referrer)) {
            String root = WebHttpUtil.getProtocolAndHost(request);
            String contextPath = request.getContextPath();
            if (!contextPath.equals(Strings.SLASH)) {
                root += contextPath;
            }
            if (referrer.startsWith(root)) {
                String url = referrer.substring(root.length());
                if (!containsQueryString) {
                    int index = url.indexOf("?");
                    if (index > 0) {
                        url = url.substring(0, index);
                    }
                }
                return url;
            }
        }
        return null;
    }

}
