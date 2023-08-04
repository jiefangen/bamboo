package org.panda.business.admin.common.util;

import org.apache.commons.lang3.StringUtils;
import org.panda.tech.core.web.context.SpringWebContext;
import org.panda.tech.core.web.util.WebHttpUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * 系统通用性工具类
 *
 * @author fangen
 **/
public class CommonUtil {
    /**
     * 语言编码KEY
     */
    public static final String LANGUAGE_KEY = "language";
    /**
     * 中文语言
     */
    public static final String LANGUAGE_CHINESE = "zh";
    /**
     * 英文语言
     */
    public static final String LANGUAGE_ENGLISH = "en";

    /**
     * 获取终端全局语言编码
     */
    public static String getGlobalLanguage() {
        HttpServletRequest request = SpringWebContext.getRequest();
        String language;
        if (WebHttpUtil.isAjaxRequest(request)) { // 从Cookie中获取终端语言
            language = WebHttpUtil.getCookieValue(SpringWebContext.getRequest(), LANGUAGE_KEY);
        } else { // 从Header中获取终端语言
            language = WebHttpUtil.getHeader(SpringWebContext.getRequest(), LANGUAGE_KEY);
        }
        if (StringUtils.isEmpty(language)) { // 默认语言编码
            return LANGUAGE_CHINESE;
        }
        return language;
    }

}
