package org.panda.business.admin.common.util;

import org.apache.commons.lang3.StringUtils;
import org.panda.bamboo.common.constant.basic.Strings;
import org.panda.tech.core.web.context.SpringWebContext;
import org.panda.tech.core.web.util.WebHttpUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

/**
 * 系统通用性工具类
 *
 * @author fangen
 **/
public class CommonUtil {
    /**
     * 语言编码标识
     */
    public static final String LANGUAGE = "language";
    /**
     * 语言编码标识
     */
    public static final String LANG = "lang";

    /**
     * 获取终端全局语言编码
     */
    public static String getGlobalLanguage() {
        HttpServletRequest request = SpringWebContext.getRequest();
        if (request == null) {
            return Strings.STR_NULL;
        }
        String language;
        if (WebHttpUtil.isAjaxRequest(request)) { // 从Cookie中获取终端语言
            language = WebHttpUtil.getCookieValue(request, LANGUAGE);
        } else { // 从Header中获取终端语言
            language = WebHttpUtil.getHeader(request, LANGUAGE);
            if (StringUtils.isBlank(language)) { // 如果还是无法获取，尝试使用lang标识获取
                language = WebHttpUtil.getHeader(request, LANG);
            }
        }
        return language;
    }

    public static Locale getLocaleLanguage() {
        String language = getGlobalLanguage();
        if (StringUtils.isBlank(language)) {
            return Locale.ROOT;
        }
        return Locale.forLanguageTag(language);
    }

}
