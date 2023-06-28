package org.panda.bamboo.common.annotation.helper;

import org.apache.commons.lang3.StringUtils;
import org.panda.bamboo.common.annotation.Caption;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Collection;
import java.util.Locale;

/**
 * 显示名称注解助手
 */
public class CaptionHelper {

    private CaptionHelper() {
    }

    private static String getCaptionValue(Caption[] captionAnnotations, Locale locale) {
        Caption caption = null;
        for (Caption captionAnnotation : captionAnnotations) {
            if (StringUtils.isBlank(captionAnnotation.locale())) {
                // 暂存默认语言的Caption注解
                caption = captionAnnotation;
            } else if (locale.toString().equals(captionAnnotation.locale())) {
                // 找到语言匹配的Caption注解
                caption = captionAnnotation;
                break;
            }
        }
        return caption == null ? null : caption.value();
    }

    public static String getCaption(Collection<Annotation> annotations, Locale locale) {
        Caption caption = null;
        for (Annotation annotation : annotations) {
            if (annotation instanceof Caption) {
                Caption captionAnnotation = (Caption) annotation;
                if (StringUtils.isBlank(captionAnnotation.locale())) {
                    // 暂存默认语言的Caption注解
                    caption = captionAnnotation;
                } else if (locale.toString().equals(captionAnnotation.locale())) {
                    // 找到语言匹配的Caption注解
                    caption = captionAnnotation;
                    break;
                }
            }
        }
        return caption == null ? null : caption.value();
    }

    public static String getCaption(Class<?> clazz, Locale locale) {
        Caption[] captionAnnotations = clazz.getAnnotationsByType(Caption.class);
        return getCaptionValue(captionAnnotations, locale);
    }

    public static String getCaption(Method method, Locale locale) {
        Caption[] captionAnnotations = method.getAnnotationsByType(Caption.class);
        return getCaptionValue(captionAnnotations, locale);
    }

    public static String getCaption(Parameter parameter, Locale locale) {
        Caption[] captionAnnotations = parameter.getAnnotationsByType(Caption.class);
        return getCaptionValue(captionAnnotations, locale);
    }

    public static String getCaption(Field field, Locale locale) {
        Caption[] captionAnnotations = field.getAnnotationsByType(Caption.class);
        return getCaptionValue(captionAnnotations, locale);
    }

}
