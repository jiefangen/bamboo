package org.panda.tech.core.web.mvc.view.exception.resolver;

import org.panda.bamboo.common.util.date.TemporalUtil;
import org.panda.tech.core.web.util.WebHttpUtil;
import org.panda.tech.core.web.context.function.WebContextPathPredicate;
import org.springframework.beans.TypeMismatchException;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * 视图层默认异常解决器
 */
public class ViewDefaultExceptionResolver extends DefaultHandlerExceptionResolver {

    private ViewErrorPathProperties pathProperties;
    private WebContextPathPredicate webContextPathPredicate;

    public ViewDefaultExceptionResolver(ViewErrorPathProperties pathProperties,
            WebContextPathPredicate webContextPathPredicate) {
        this.pathProperties = pathProperties;
        this.webContextPathPredicate = webContextPathPredicate;
        setOrder(Ordered.HIGHEST_PRECEDENCE + 4);
    }

    @Override
    protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
            Exception ex) {
        ModelAndView mav = super.doResolveException(request, response, handler, ex);
        if (mav == null && supports(request, ex)) {
            String path = this.pathProperties.getInternal();
            if (this.webContextPathPredicate.test(path)) {
                mav = new ModelAndView(path);
                mav.addObject("errorTime", TemporalUtil.format(LocalDateTime.now()));
            }
        }
        return mav;
    }

    private boolean supports(HttpServletRequest request, Exception ex) {
        // 只处理非ajax请求和非可解决异常
        return !WebHttpUtil.isAjaxRequest(request);
//        return !WebHttpUtil.isAjaxRequest(request) && !ResolvableExceptionResolver.supports(ex); // 非必要处理
    }

    @Override
    protected ModelAndView handleTypeMismatch(TypeMismatchException ex, HttpServletRequest request,
            HttpServletResponse response, Object handler) throws IOException {
        if (supports(request, ex)) {
            String path = this.pathProperties.getBadRequest();
            if (this.webContextPathPredicate.test(path)) {
                return new ModelAndView(path, "exception", ex);
            }
        }
        return super.handleTypeMismatch(ex, request, response, handler);
    }

    @Override
    // 处理请求无对应控制器方法的异常，不能处理控制器方法抛出的404错误
    protected ModelAndView handleNoHandlerFoundException(NoHandlerFoundException ex, HttpServletRequest request,
            HttpServletResponse response, Object handler) throws IOException {
        if (!WebHttpUtil.isAjaxRequest(request)) { // 非ajax请求才跳转错误页面，否则采用默认处理
            String path = this.pathProperties.getNotFound();
            if (this.webContextPathPredicate.test(path)) {
                return new ModelAndView(path);
            }
        }
        return super.handleNoHandlerFoundException(ex, request, response, handler);
    }

}
