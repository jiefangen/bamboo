package org.panda.business.admin.application.resolver;

import org.panda.business.admin.common.utils.CommonUtils;
import org.panda.tech.core.i18n.resolver.MessageResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 国际化资源解析器
 *
 * @author fangen
 **/
@Component
public class MessageSourceResolver {

    @Autowired
    private MessageResolver messageResolver;

    /**
     * 查找国际化信息
     *
     * @param code 国际化信息code
     * @return 国际化信息
     */
    public String findI18nMessage(String code, Object... args) {
        return messageResolver.resolveMessage(code, CommonUtils.getLocaleLanguage(), args);
    }
}
