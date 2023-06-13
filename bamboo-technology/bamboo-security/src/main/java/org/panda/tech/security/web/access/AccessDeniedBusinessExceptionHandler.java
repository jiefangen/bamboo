package org.panda.tech.security.web.access;

import org.panda.bamboo.common.exception.business.auth.NoOperationAuthorityException;
import org.panda.tech.core.web.restful.RestfulResult;
import org.panda.tech.core.web.util.WebHttpUtil;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 访问拒绝后的业务异常处理器
 */
public class AccessDeniedBusinessExceptionHandler extends AccessDeniedHandlerImpl {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException, ServletException {
        // 异常处理器
        if (WebHttpUtil.isAjaxRequest(request)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            if (accessDeniedException.getCause() instanceof NoOperationAuthorityException) {
                NoOperationAuthorityException exception = (NoOperationAuthorityException) accessDeniedException.getCause();
                Object obj = RestfulResult.failure(exception.getCode(), exception.getMessage());
                WebHttpUtil.buildJsonResponse(response, obj);
            }
        } else {
            super.handle(request, response, accessDeniedException);
        }
    }

}
