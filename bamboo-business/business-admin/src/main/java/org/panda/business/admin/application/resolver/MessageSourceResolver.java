package org.panda.business.admin.application.resolver;

import org.panda.business.admin.common.util.CommonUtil;
import org.panda.tech.core.i18n.ModelPropertyCaptionResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * 国际化资源解析器
 *
 * @author fangen
 **/
@Component
public class MessageSourceResolver {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private ModelPropertyCaptionResolver propertyCaptionResolver;

    /**
     * 查找国际化信息
     *
     * @param code 国际化信息code
     * @return 国际化信息
     */
    public String findI18nMessage(String code) {
        return messageSource.getMessage(code, null, code, CommonUtil.getLocaleLanguage());
    }

    /**
     * 查找指定类属性中的国际化信息
     *
     * @param clazz 指定类
     * @param propertyName 属性
     * @return 国际化信息
     */
    public Optional<String> findI18nMessage(Class<?> clazz, String propertyName) {
        String message = propertyCaptionResolver.resolveCaption(clazz, propertyName, CommonUtil.getLocaleLanguage());
        return Optional.of(message);
    }

}
