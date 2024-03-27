package org.panda.tech.core.web.mvc.view.exception.resolver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 可解决异常处理至视图页面的解决器
 * 视业务发展扩展
 */
@Component
public class ViewResolvableExceptionResolver {

    @Autowired
    private ViewErrorPathProperties pathProperties;

    public String getBusinessErrorPath() {
        return this.pathProperties.getBusiness();
    }

}
