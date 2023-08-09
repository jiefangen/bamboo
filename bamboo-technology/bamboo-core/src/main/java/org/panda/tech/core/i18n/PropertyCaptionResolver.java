package org.panda.tech.core.i18n;

import java.util.Locale;

/**
 * 属性显示名称解决器
 */
public interface PropertyCaptionResolver {

    String resolveCaption(Class<?> clazz, String propertyName, Locale locale);

}
