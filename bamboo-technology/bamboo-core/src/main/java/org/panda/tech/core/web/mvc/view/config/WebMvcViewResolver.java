package org.panda.tech.core.web.mvc.view.config;

import org.jetbrains.annotations.NotNull;
import org.panda.bamboo.common.constant.basic.Strings;
import org.panda.tech.core.web.util.NetUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcProperties;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * MVC视图解决器
 */
@Component
public class WebMvcViewResolver {
    /**
     * 直接重定向的视图名称前缀
     */
    public static final String REDIRECT_VIEW_NAME_PREFIX = "redirect:";

    @Autowired
    private WebMvcProperties webMvcProperties;

    public void resolveView(HttpServletRequest request, HttpServletResponse response, String viewName,
            @NotNull Map<String, Object> model) throws IOException, ServletException {
        boolean redirect = viewName.startsWith(REDIRECT_VIEW_NAME_PREFIX);
        String path = redirect ? viewName.substring(REDIRECT_VIEW_NAME_PREFIX.length()) : viewName;
        if (!path.startsWith(Strings.SLASH)) {
            path = Strings.SLASH + path;
        }
        if (redirect) {
            path = NetUtil.mergeParams(path, model, Strings.ENCODING_UTF8);
            response.sendRedirect(path);
        } else {
            WebMvcProperties.View view = this.webMvcProperties.getView();
            path = view.getPrefix() + path + view.getSuffix();
            path = path.replaceAll(Strings.DOUBLE_SLASH, Strings.SLASH); // 避免出现双斜杠//
            if (model != null) {
                model.forEach(request::setAttribute);
            }
            request.getRequestDispatcher(path).forward(request, response);
        }
    }

}
