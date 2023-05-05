package org.panda.tech.core.web.mvc.controller;

import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 转调控制器支持
 */
@RequestMapping("/recall")
public abstract class RecallControllerSupport extends JumpControllerSupport {

    @Override
    protected void jump(HttpServletRequest request, HttpServletResponse response, String targetUrl,
            Map<String, Object> body) throws Exception {
        // do something

    }

}
