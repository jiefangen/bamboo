package org.panda.business.helper.app.infrastructure.security.auth;

import org.panda.bamboo.core.util.SpringUtil;
import org.panda.tech.auth.mgt.SubjectManager;
import org.panda.tech.auth.subject.Subject;
import org.panda.tech.core.web.context.SpringWebContext;
import org.springframework.context.ApplicationContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 项目安全管理工具
 *
 * @author fangen
 * @since JDK 11
 */
public class SubjectUtil {

    private SubjectUtil() {
    }

    private static SubjectManager SUBJECT_MANAGER;

    private static SubjectManager getSubjectManager(HttpServletRequest request) {
        if (SUBJECT_MANAGER == null) {
            ApplicationContext context = SpringWebContext.getApplicationContext(request);
            SUBJECT_MANAGER = SpringUtil.getFirstBeanByClass(context, SubjectManager.class);
        }
        return SUBJECT_MANAGER;
    }

    public static Subject getSubject(HttpServletRequest request, HttpServletResponse response) {
        return getSubjectManager(request).getSubject(request, response, HelperUser.class);
    }

    public static Subject getSubject(HttpServletResponse response) {
        HttpServletRequest request = SpringWebContext.getRequest();
        return getSubject(request, response);
    }

    public static Subject getSubject() {
        HttpServletRequest request = SpringWebContext.getRequest();
        HttpServletResponse response = SpringWebContext.getResponse();
        return getSubject(request, response);
    }

    public static HelperUser getHelperUser() {
        return getSubject().getUser();
    }

    public static Integer getUserId() {
        HelperUser helperUser = getHelperUser();
        return helperUser == null ? null : helperUser.getId();
    }

}
